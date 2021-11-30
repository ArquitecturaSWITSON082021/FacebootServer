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
public class HashProvider {
    
    public static Sha256 sha256;
    
    public static void Initialize(){
        sha256 = new Sha256();
    }
}
