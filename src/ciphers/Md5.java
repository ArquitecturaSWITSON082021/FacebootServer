/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ciphers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Ivy
 */
public class Md5 implements IHash {

    @Override
    public byte[] Encrypt(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String Encrypt(String data) {
        byte[] result = this.Encrypt(data.getBytes());
        if (result == null) {
            return null;
        }

        return FacebootNet.Utils.BytesToHex(result).toLowerCase();
    }

    @Override
    public byte[] Decrypt(byte[] data) {
        throw new UnsupportedOperationException("MD5 does not support this method."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String Decrypt(String data) {
        throw new UnsupportedOperationException("MD5 does not support this method."); //To change body of generated methods, choose Tools | Templates.
    }

}
