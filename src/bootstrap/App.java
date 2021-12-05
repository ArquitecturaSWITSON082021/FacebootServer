package bootstrap;


import ciphers.HashProvider;
import dao.DaoProvider;
import database.MysqlSingleton;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import managers.AttachmentManager;
import models.User;
import oauth.FacebookOauthDecorator;
import org.hibernate.Query;
import org.hibernate.Session;
import providers.ConfigProvider;
import server.HttpsOauthCodeCallback;
import server.HttpsOauthServer;
import server.TcpServer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ivy
 */
public class App {
    
    public static TcpServer tcpServer;
    
    public static void main(String args[]) throws Exception {
        
        // Run initialization routines.
        if (!ConfigProvider.Initialize()){
            System.out.println("[-] Could not initialize config. Make sure you've .env file on main path.");
            return;
        }
        DaoProvider.Initialize();
        DaoProvider.UsersOauth.FindFirstUserByAccountId(0, "");
        HashProvider.Initialize();
        
        // Spawn HTTPS server, for oAuth purposes.
        HttpsOauthServer http = new HttpsOauthServer(5000);
        http.OnAuthCodeCallback = new HttpsOauthCodeCallback();
        http.start();
        
        // Spawn main Faceboot TCP server.
        tcpServer = new TcpServer();
        tcpServer.start(4000);
        
        System.out.println("[+] Faceboot server initialized gracefully.");
    }
}
