/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import FacebootNet.Engine.ErrorCode;
import dao.DaoProvider;
import FacebootNet.Engine.PacketBuffer;
import FacebootNet.Packets.Client.CLoginPacket;
import FacebootNet.Packets.Server.SLoginPacket;
import ciphers.HashProvider;
import models.User;

/**
 *
 * @author Ivy
 */
public class AuthController {

    public static byte[] DoLogin(PacketBuffer req) throws Exception {
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

        response.TokenId = "AAA";

        response.UserId = user.getId();
        response.UserName = user.getName() + " " + user.getLastName();
        response.UserEmail = user.getEmail();
        response.UserBornDate = user.getBornDate().toGMTString();
        response.UserGender = user.getGender();
        response.UserPhone = user.getPhone();

        return response.Serialize();
    }

}
