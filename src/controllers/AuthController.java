/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import Dao.DaoProvider;
import FacebootNet.Engine.PacketBuffer;
import FacebootNet.Packets.Client.CLoginPacket;
import FacebootNet.Packets.Client.CRegisterPacket;
import FacebootNet.Packets.Server.SLoginPacket;
import FacebootNet.Packets.Server.SRegisterPacket;
import java.text.SimpleDateFormat;
import java.util.Date;
import models.User;

/**
 *
 * @author Ivy
 */
public class AuthController {

    public static byte[] DoLogin(PacketBuffer req) throws Exception {
        CLoginPacket login = CLoginPacket.Deserialize(req.Serialize());
        SLoginPacket response = new SLoginPacket(login.GetRequestIndex());

        User user = DaoProvider.Users.FindFirstUserByEmailPassword(login.Email, login.Password);
        response.ErrorCode = user == null ? 1 : 0;
        response.TokenId = user == null ? "" : "AAA";

        if (user != null) {
            response.UserId = user.getId();
            response.UserName = user.getName() + " " + user.getLastName();
            response.UserEmail = user.getEmail();
            response.UserBornDate = user.getBornDate().toGMTString();
            response.UserGender = user.getGender();
            response.UserPhone = user.getPhone();
        }

        return response.Serialize();
    }
    
    public static byte[] DoRegister(PacketBuffer req) throws Exception {
        CRegisterPacket register = CRegisterPacket.Deserialize(req.Serialize());
        SRegisterPacket response = new SRegisterPacket(register.GetRequestIndex());

        Date bornDate = new SimpleDateFormat("dd/MM/yyyy").parse(register.BornDate);
        
        User user = new User();
        user.setName(register.UserName);
        user.setLastName(register.LastName);
        user.setEmail(register.Email);
        user.setPhone(register.Phone);
        user.setGender(register.Gender);
        user.setBornDate(bornDate);
        user.setPasswd(register.Password);
        boolean result = user.Save();
        
        response.ErrorCode = result ? 0 : 1;

        if (result) {
            response.UserId = user.getId();
            response.UserName = register.UserName;
        }

        return response.Serialize();
    }
}
