/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.AbstractDao;
import database.MysqlSingleton;
import java.util.List;
import models.Post;
import org.hibernate.Query;

/**
 *
 * @author Ivy
 */
public class PostsDao extends AbstractDao<Post> {
    
    public PostsDao(){
        this.model_class = Post.class;
        this.table_name = "Post";
    }
    
}
