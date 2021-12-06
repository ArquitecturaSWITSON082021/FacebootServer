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
public class CipherProvider {
    
    public static HmacSha1 hmacSha1;
    
    public static void Initialize(){
        hmacSha1 = new HmacSha1();
    }
}
