/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Dao.AbstractDao;
import database.MysqlSingleton;
import java.util.List;
import models.User;
import org.hibernate.Query;

/**
 *
 * @author Ivy
 */
public class UsersDao extends AbstractDao<User> {
    
    public UsersDao(){
        this.model_class = User.class;
        this.table_name = "User";
    }
    
    public User FindFirstUserByEmailPassword(String email, String passwd) throws Exception{
        Query q;
        q = MysqlSingleton
                .OpenSession()
                .createQuery("SELECT u FROM User u WHERE (u.email = :email OR u.phone = :phone) AND u.passwd = :passwd")
                .setParameter("email", email)
                .setParameter("phone", email)
                .setParameter("passwd", passwd);
        
        List<User> users = q.list();
        if (users.size() > 0)
            return users.get(0);
        
        return null;
    }
    
}
