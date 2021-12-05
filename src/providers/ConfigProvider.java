/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package providers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ivy
 */
public class ConfigProvider {
    
    private static ConfigProvider _config;
    private HashMap<String, String> params;
    
    public ConfigProvider(){
        if (_config == null)
            _config = this;
        
        params = new HashMap<>();
    }
    
    public static String FindValueByName(String name, String defaultValue){
        String v = _config.params.get(name);
        if (v == null)
            return defaultValue;
        return v;
    }
    
    public static String FindValueByName(String name){
        return FindValueByName(name, "");
    }
    
    public static boolean Initialize(){
        if (_config != null)
            return false;
        
        _config = new ConfigProvider();
        try {
            String configPath = System.getProperty("user.dir") + File.separatorChar + ".env"; 
            byte[] configFile = Files.readAllBytes(Paths.get(configPath));
            String[] lines = new String(configFile).split("\n");
            for(String line : lines){
                int idx = line.indexOf("=");
                if (idx == -1)
                    continue;
                String name = line.substring(0, idx);
                String value = line.substring(idx + 1).replaceAll("\r", "");
                _config.params.put(name, value);
            }
            return true;
        } catch (Exception ex) {
        }
        return false;
    }
}
