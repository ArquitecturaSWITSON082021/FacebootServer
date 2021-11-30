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

/**
 *
 * @author Ivy
 */
public class RegisterController {
    public static byte[] DoRegister(PacketBuffer req) throws Exception {
        CRegisterPacket register = CRegisterPacket.Deserialize(req.Serialize());
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

        if (result) {
            response.UserId = user.getId();
            response.UserName = register.UserName;
        }

        return response.Serialize();
    }
}
