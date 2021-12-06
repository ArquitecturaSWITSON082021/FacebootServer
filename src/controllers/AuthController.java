/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import FacebootNet.Engine.ErrorCode;
import static FacebootNet.Engine.ErrorCode.InternalServerError;
import dao.DaoProvider;
import FacebootNet.Engine.PacketBuffer;
import FacebootNet.Packets.Client.CAttemptOauthPacket;
import FacebootNet.Packets.Client.CLoginOauthPacket;
import FacebootNet.Packets.Client.CLoginPacket;
import FacebootNet.Packets.Server.SAttemptOauthPacket;
import FacebootNet.Packets.Server.SLoginPacket;
import ciphers.HashProvider;
import java.util.Date;
import models.User;
import models.UserOauth;
import models.UserToken;
import oauth.FacebookOauthDecorator;
import oauth.IBaseOauthDecorator;
import oauth.TwitterOauthDecorator;
import providers.ConfigProvider;
import server.TcpPeer;

/**
 *
 * @author Ivy
 */
public class AuthController {

    public static byte[] DoLogin(TcpPeer peer, String ipAddress, byte[] packet, boolean isOauth) throws Exception {
        User user = null;
        SLoginPacket response = new SLoginPacket(0);
        if (isOauth) {
            CLoginOauthPacket login = CLoginOauthPacket.Deserialize(packet);
            System.out.printf("oauth_Type=%d, id=%s\n", login.OauthType, login.AccountId);
            UserOauth userOauth = dao.DaoProvider.UsersOauth.FindFirstUserByAccountId(login.OauthType, login.AccountId);
            if (userOauth == null || userOauth.getId() <= 0) {
                response.ErrorCode = ErrorCode.InvalidCredentials;
            }

            user = dao.DaoProvider.Users.FindFirstById(userOauth.getUserId());
        } else {
            CLoginPacket login = CLoginPacket.Deserialize(packet);
            String passwd = HashProvider.sha256.Encrypt(login.Password);
            if (login.Password == null || passwd == null || login.Password.length() <= 0) {
                response.ErrorCode = ErrorCode.InternalServerError;
                return response.Serialize();
            }

            user = DaoProvider.Users.FindFirstUserByEmailPassword(login.Email, passwd);
        }

        if (user == null || user.getId() <= 0) {
            response.ErrorCode = ErrorCode.InvalidCredentials;
            return response.Serialize();
        }

        // Craft token if everything is OK
        String uuid = HashProvider.sha256.Encrypt(String.format("$@!#@!_token_%d_%s", new Date().getTime(), ipAddress));
        UserToken token = DaoProvider.Tokens.Craft();
        token.setUser(user);
        token.setVigency(new Date());
        token.setUuid(uuid);
        token.setIpAddress(ipAddress);

        if (!token.Save()) {
            response.ErrorCode = ErrorCode.InternalServerError;
            return response.Serialize();
        }

        response.TokenId = token.getUuid();
        response.UserId = user.getId();
        response.UserName = user.getName() + " " + user.getLastName();
        response.UserEmail = user.getEmail();
        response.UserBornDate = user.getBornDate().toGMTString();
        response.UserGender = user.getGender();
        response.UserPhone = user.getPhone();
        peer.authenticate(token);

        return response.Serialize();
    }

    public static byte[] DoAttemptOauth(String uuid, byte[] packet) throws Exception {
        CAttemptOauthPacket request = CAttemptOauthPacket.Deserialize(packet);
        SAttemptOauthPacket response = new SAttemptOauthPacket(request.GetRequestIndex());
        IBaseOauthDecorator Oauth;
        response.OauthType = request.OauthType;
        if (request.OauthType == FacebootNet.Engine.OauthType.Facebook) {
            Oauth = new FacebookOauthDecorator();
        } else if (request.OauthType == FacebootNet.Engine.OauthType.Twitter){
            Oauth = new TwitterOauthDecorator();
        } else {
            throw new Exception("Not implemented yet.");
        }

        
        response.OauthUrl = Oauth.GetOauthUrl(uuid);
        return response.Serialize();
    }
}
