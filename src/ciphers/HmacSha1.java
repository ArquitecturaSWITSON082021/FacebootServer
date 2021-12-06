/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ciphers;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Ivy
 */
public class HmacSha1 implements ICipher {

    @Override
    public byte[] Encrypt(byte[] data, byte[] password) {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secret = new SecretKeySpec(password, "HmacSHA1");
            mac.init(secret);
            return mac.doFinal(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String Encrypt(String data, String password) {
        return FacebootNet.Utils.BytesToHex(Encrypt(data.getBytes(), password.getBytes())).toLowerCase();
    }

    @Override
    public byte[] Decrypt(byte[] data, byte[] password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String Decrypt(String data, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
