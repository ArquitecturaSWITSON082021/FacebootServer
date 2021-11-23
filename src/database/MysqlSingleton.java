/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.util.logging.Level;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Ivy
 */
public class MysqlSingleton {

    private static MysqlSingleton instance;
    private static SessionFactory sessionFactory;

    private MysqlSingleton(SessionFactory session) {
        MysqlSingleton.sessionFactory = session;
    }

    public static MysqlSingleton GetInstance() throws Exception {
        if (instance == null) {
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
            SessionFactory fact = null;
            try {
                fact = new Configuration().configure().buildSessionFactory();
            } catch (Throwable ex) {
                System.err.println("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }
            instance = new MysqlSingleton(fact);
        }
        return instance;
    }
    
    public Session openSession() throws Exception{
        return MysqlSingleton.sessionFactory.openSession();
    }

    public static Session OpenSession() throws Exception {
        return GetInstance().openSession();
    }
}
