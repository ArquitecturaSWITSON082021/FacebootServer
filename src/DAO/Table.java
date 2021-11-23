/*
 * DO NOT REMOVE THIS HEADER.
 * FacebootNet project, it works as a network library for the Faceboot application.
 * This application was created at ITSON in August-December 2021 semester of Software Engineering.
 */
package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author orlandocamacho
 */
public class Table {
    
    protected String nameTable;
    private Connection connection;
    private Statement sentence;
    private ResultSet answer;
    
    public Table(String nameTable){
        this.nameTable = nameTable;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public void query(String sql) throws Exception {
        try {
            this.sentence = this.connection.createStatement();
            this.answer = this.sentence.executeQuery(sql);
        } catch (SQLException se) {
            throw new Exception("Cannot query the database", se);
        }
    }
    
    public ResultSet getRow() throws Exception {
        try {
            if(this.answer.next()){
                return this.answer;
            }
            this.answer.close();
            this.sentence.close();
            return null;
        } catch (SQLException se) {
            throw new Exception("Cannot query the database", se);
        }
    }
    
    public void update(String sql) throws Exception  {
        try {
            this.sentence = this.connection.createStatement();
            this.sentence.executeUpdate(sql);
            this.sentence.close();
        } catch (SQLException se) {
            throw new Exception("Cannot update database", se);
        }
        
    }
    
}
