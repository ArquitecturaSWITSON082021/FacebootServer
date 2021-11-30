/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.MysqlSingleton;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Ivy
 */
public class AbstractDao<T> {

    public Class<T> model_class;
    public String table_name = "TBD";

    public T FindFirstById(int id) throws Exception {
        Session session = MysqlSingleton.OpenSession();
        Transaction tx = session.beginTransaction();
        T row = (T)session.get(model_class, id);
        tx.commit();
        session.close();
        return row;
    }

    public List<T> Find() throws Exception {
        Session session = MysqlSingleton.OpenSession();
        Transaction tx = session.beginTransaction();
        List<T> list = session.createQuery("FROM " + table_name).list();
        tx.commit();
        session.close();
        return list;
    }
    
    public T Craft() throws Exception {
        return model_class.newInstance();
    }
}
