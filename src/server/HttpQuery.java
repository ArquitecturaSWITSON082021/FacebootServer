/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;

/**
 *
 * @author Ivy
 */
public class HttpQuery {

    public ArrayList<HttpQueryParam> _params;

    public HttpQuery(String queryUri) {
        _params = new ArrayList<>();
        String[] args = queryUri.split("&");
        for (String arg : args) {
            String[] data = arg.split("=");
            if (data.length > 2 || data.length <= 0) {
                continue;
            }

            String name = data[0];
            String value = data.length > 1 ? java.net.URLDecoder.decode(data[1]) : "";
            _params.add(new HttpQueryParam(name, value));
        }
    }
    
    public String FindValueByName(String name){
        for(HttpQueryParam p : _params){
            if (p.name.equals(name))
                return p.value;
        }
        return null;
    }
}
