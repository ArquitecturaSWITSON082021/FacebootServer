/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ciphers;

/**
 *
 * @author Ivy
 */
public interface ICipher {
    public byte[] Encrypt(byte[] data);
    public String Encrypt(String data);
    public byte[] Decrypt(byte[] data);
    public String Decrypt(String data);
}
