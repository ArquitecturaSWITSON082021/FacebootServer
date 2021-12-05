/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Ivy
 */
public class HttpQueryParam {
    
    public String name;
    public String value;
    
    public HttpQueryParam(String name, String value){
        this.name = name;
        this.value = value;
    }
}
