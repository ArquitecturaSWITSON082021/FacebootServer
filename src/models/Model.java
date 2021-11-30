/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import database.MysqlSingleton;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Ivy
 */
public class Model<T> {

    protected String _table_name = "TBD";
    protected Integer id;
    protected Date updatedAt;
    protected Date createdAt;
    protected Date deletedAt;
    protected String tableName;

    public Integer getId() {
        return this.id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    protected void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    protected void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeletedAt() {
        return this.deletedAt;
    }

    protected void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public boolean Save() {
        Session session = null;
        Transaction tx = null;
        try {
            session = MysqlSingleton.OpenSession();
            tx = session.beginTransaction();
            if (this.id != null && this.id > 0){
                updatedAt = new Date();
                session.update(this);
            }else{
                createdAt = new Date();
                updatedAt = new Date();
                session.save(this);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("[DBMS EX] " + e.getMessage());
            return false;
        }
        return id > 0;
    }
    
    public boolean Delete() {
        Session session = null;
        Transaction tx = null;
        try {
            session = MysqlSingleton.OpenSession();
            tx = session.beginTransaction();
            deletedAt = new Date();
            if (this.id > 0)
                session.update(this);
            else
                throw new Exception("Cannot attempt to delete model if is not in DBMS yet.");
            tx.commit();
            session.close();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("[DBMS EX] " + e.getMessage());
            return false;
        }
        return true;
    }

}
