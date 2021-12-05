/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import FacebootNet.Engine.ErrorCode;
import FacebootNet.Engine.PacketBuffer;
import FacebootNet.Packets.Client.CRegisterPacket;
import FacebootNet.Packets.Server.SRegisterPacket;
import ciphers.HashProvider;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import models.User;
import models.UserOauth;

/**
 *
 * @author Ivy
 */
public class RegisterController {
    public static byte[] DoRegister(byte[] packet) throws Exception {
        CRegisterPacket register = CRegisterPacket.Deserialize(packet);
        SRegisterPacket response = new SRegisterPacket(register.GetRequestIndex());

        Date bornDate = new SimpleDateFormat("dd/MM/yyyy").parse(register.BornDate);
        Date today = new Date();

        Period period = Period.between(bornDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        if (period.getYears() < 18) {
            response.ErrorCode = ErrorCode.UserIllegalAge;
            return response.Serialize();
        }

        String passwd = HashProvider.sha256.Encrypt(register.Password);
        if (register.Password == null || passwd == null || register.Password.length() <= 0) {
            response.ErrorCode = ErrorCode.InternalServerError;
            return response.Serialize();
        }

        User user = new User();
        user.setName(register.UserName);
        user.setLastName(register.LastName);
        user.setEmail(register.Email);
        user.setPhone(register.Phone);
        user.setGender(register.Gender);
        user.setBornDate(bornDate);
        user.setPasswd(passwd);
        boolean result = user.Save();

        response.ErrorCode = result ? 0 : 1;

        if (!result) {
            return response.Serialize();
        }
        
        response.UserId = user.getId();
        response.UserName = register.UserName;
        
        if (register.Oauth == null || register.Oauth.Id == null){
            return response.Serialize();
        }
        
        UserOauth userOauth = dao.DaoProvider.UsersOauth.Craft();
        userOauth.setUserId(user.getId());
        userOauth.setOauthType(register.Oauth.OauthType);
        userOauth.setAccountId(register.Oauth.Id);
        userOauth.setAccountEmail(register.Oauth.Email);
        userOauth.setAccountFirstName(register.Oauth.FirstName);
        userOauth.setAccountLastName(register.Oauth.LastName);
        userOauth.setAccountGender(register.Oauth.Gender);

        result = userOauth.Save();
        response.ErrorCode = result ? 0 : 2;

        if (!result) {
            return response.Serialize();
        }
        
        return response.Serialize();
    }
}
