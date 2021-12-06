/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import FacebootNet.Packets.Client.CLoginOauthPacket;
import FacebootNet.Packets.Server.SOauthPacket;
import bootstrap.App;
import controllers.AuthController;
import java.util.concurrent.Callable;
import models.User;
import models.UserOauth;
import oauth.FacebookOauthDecorator;
import oauth.IBaseOauthDecorator;
import oauth.OauthUserInfo;
import oauth.TwitterOauthDecorator;

/**
 *
 * @author Ivy
 */
public class HttpsOauthCodeCallback {

    public void Execute(String method, String ipAddress, HttpQuery query) {
        try {
            String state = query.FindValueByName("state");
            if (state == null || state.length() <= 0) {
                throw new Exception("Invalid state.");
            }

            String[] args = state.split("\\$");
            int oauthType = Integer.parseInt(args[0], 10);
            String socketUuid = args[1];
            if (socketUuid == null || socketUuid.length() <= 0) {
                throw new Exception("Invalid socket uuid!");
            }

            TcpPeer peer = App.tcpServer.findPeerByUuid(socketUuid);
            if (peer == null) {
                throw new Exception("Socket not found given uuid.");
            }

            System.out.printf("[*] Attempting oAuth login. oauth_type=%d, socketUuid=%s\n", oauthType, socketUuid);

            OauthUserInfo oAuthResult = null;
            IBaseOauthDecorator oAuth;
            if (oauthType == FacebootNet.Engine.OauthType.Facebook) {
                String code = query.FindValueByName("code");
                if (code == null || code.length() <= 0) {
                    throw new Exception("Invalid code.");
                }

                oAuth = new FacebookOauthDecorator();
                oAuthResult = oAuth.GetUserInfo(code, "");
            } else {
                String oauth_token = query.FindValueByName("oauth_token");
                if (oauth_token == null || oauth_token.length() <= 0) {
                    throw new Exception("Invalid oauth_token.");
                }

                String oauth_verifier = query.FindValueByName("oauth_verifier");
                if (oauth_verifier == null || oauth_verifier.length() <= 0) {
                    throw new Exception("Invalid oauth_verifier.");
                }

                oAuth = new TwitterOauthDecorator();
                oAuthResult = oAuth.GetUserInfo(oauth_token, oauth_verifier);
            }

            if (oAuthResult == null) {
                System.out.println("[-] Failed to perform oAuth login.");
                return;
            }

            UserOauth userOauth = dao.DaoProvider.UsersOauth.FindFirstUserByAccountId(oauthType, oAuthResult.id);
            // Account does not exists, attempt to create a new one.
            if (userOauth == null) {
                SOauthPacket response = new SOauthPacket(0);
                oAuthResult.mapPacket(oauthType, response);
                peer.write(response);
                System.out.println("[+] User account not found, attempting to register with that data on client side.");
            } else {
                // TODO: Perform login code here.
                CLoginOauthPacket login = new CLoginOauthPacket(0);
                login.OauthType = oauthType;
                login.AccountId = oAuthResult.id;
                byte[] result = AuthController.DoLogin(peer, ipAddress, login.Serialize(), true);
                if (result != null) {
                    peer.write(result);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
