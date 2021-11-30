
import ciphers.HashProvider;
import dao.DaoProvider;
import database.MysqlSingleton;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import managers.AttachmentManager;
import models.User;
import org.hibernate.Query;
import org.hibernate.Session;
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
    public static void main(String args[]) throws Exception {
        DaoProvider.Initialize();
        HashProvider.Initialize();
        DaoProvider.Users.FindFirstById(0);
        /* User userDao = new User();
        List<User> users = Dao.users.Find();
        for(User e : users){
            System.out.println(e.getId() + " " + e.getName() + " " + e.getEmail());
        } */
        
        TcpServer server = new TcpServer();
        server.start(4000);
        
        /* User user = DaoProvider.Users.FindFirstById(1);
        System.out.println(user.getId() + " " + user.getName() + " " + user.getEmail());
        
        user = DaoProvider.Users.Craft();
        user.setName("David");
        user.setLastName("Perez");
        user.setGender("male");
        user.setEmail("brian123");
        user.setPasswd("sha256!!!");
        user.setBornDate(new Date());
        user.setPhone("01223456789");
        boolean result = user.Save();
        System.out.println(result); */
    }
}
