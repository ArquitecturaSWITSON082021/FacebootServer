/*
 * DO NOT REMOVE THIS HEADER.
 * FacebootNet project, it works as a network library for the Faceboot application.
 * This application was created at ITSON in August-December 2021 semester of Software Engineering.
 */
package DAO;

import BussinesObject.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author orlandocamacho
 */
public class Users extends Table{
    
    public Users(String nameTable) {
        super(nameTable);
    }
    
    public User get(User user) throws Exception {
        String sql = "SELECT * FROM " + this.nameTable + " WHERE email = '" + user.getEmail() + "' AND password = '" + user.getPassword() + "'";
        query(sql);
        ResultSet row;
        if ((row = getRow()) != null)
       try {
            User userFound = new User(row.getString("name"), row.getString("lastName"), row.getString("email") ,row.getString("phone"), row.getString("password"), row.getString("birthday"), row.getString("gender"));
            return userFound;
        } catch (SQLException se) {
            throw new Exception("Failed to access the database", se);
        }
        return null;
    }
    
    public void add(User user) throws Exception {
        String sql = "INSERT INTO `faceboot`.`users` (`name`, `lastName`, `email`, `phone`, `password`, `birthday`, `gender`) VALUES ('" + user.getName() + "', '" + user.getLastName() + "', '" + user.getEmail() + "', '" + user.getPhone() + "', '" + user.getPassword() + "', '" + user.getBirthday().toString() + "', '" + user.getGender() + "'); ";
        update(sql);
    }
    
    public void update(User user) throws Exception {
        String sql = "UPDATE `faceboot`.`users` SET `name` = '" + user.getName() + "', `lastName` = '" + user.getLastName() + "', `email` = '" + user.getEmail() + "', `phone` = '" + user.getPhone() + "', `password` = '" + user.getPassword() + "', `birthday` = '" + user.getBirthday() + "', `gender` = '" + user.getGender() + "' WHERE (`email` = '" + user.getEmail() + "');";
        update(sql);
    }
    
    public void delete(User user) throws Exception {
        String sql = "DELETE FROM `feceboot`.`users` WHERE (`email` = '" + user.getEmail()+ "');";
        update(sql);
    }
    
    public List<User> list() throws Exception  {
        List<User> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + this.nameTable + ";";
            query(sql);
            ResultSet row;
            while ((row = getRow()) != null) {
                User user = new User(row.getString("name"), row.getString("lastName"), row.getString("email") ,row.getString("phone"), row.getString("password"), row.getString("birthday"), row.getString("gender"));
                list.add(user);
            }
        } catch (Exception se) {
            throw new Exception("Failed to access the database", se);
        }
        return list;
    }
}
