package Dao;

import models.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ivy
 */
public class DaoProvider {

    private static boolean IsInitialized = false;
    public static UsersDao Users;


    public static boolean Initialize() throws Exception {
        Users = new UsersDao();
        IsInitialized = true;
        return true;
    }

}
