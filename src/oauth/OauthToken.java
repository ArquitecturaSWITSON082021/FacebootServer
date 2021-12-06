/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oauth;

/**
 *
 * @author Ivy
 */
public class OauthToken {
    
    public String Token;
    public String Secret;

    @Override
    public String toString() {
        return "OauthToken{" + "Token=" + Token + ", Secret=" + Secret + '}';
    }
    
    
}
