/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Dao.AbstractDao;
import models.User;

/**
 *
 * @author Ivy
 */
public class UsersDao extends AbstractDao<User> {
    
    public UsersDao(){
        this.model_class = User.class;
        this.table_name = "User";
    }
    
}
