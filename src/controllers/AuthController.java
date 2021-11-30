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
import FacebootNet.Packets.Client.CLoginPacket;
import FacebootNet.Packets.Server.SLoginPacket;
import ciphers.HashProvider;
import java.util.Date;
import models.User;
import models.UserToken;

/**
 *
 * @author Ivy
 */
public class AuthController {

    public static byte[] DoLogin(PacketBuffer req, String ipAddress) throws Exception {
        CLoginPacket login = CLoginPacket.Deserialize(req.Serialize());
        SLoginPacket response = new SLoginPacket(login.GetRequestIndex());

        String passwd = HashProvider.sha256.Encrypt(login.Password);
        if (login.Password == null || passwd == null || login.Password.length() <= 0) {
            response.ErrorCode = ErrorCode.InternalServerError;
            return response.Serialize();
        }

        User user = DaoProvider.Users.FindFirstUserByEmailPassword(login.Email, passwd);

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
        
        if (!token.Save()){
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

        return response.Serialize();
    }

}
