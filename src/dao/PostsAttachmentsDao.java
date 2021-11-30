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
import models.PostAttachment;
import org.hibernate.Query;

/**
 *
 * @author Ivy
 */
public class PostsAttachmentsDao extends AbstractDao<PostAttachment> {
    
    public PostsAttachmentsDao(){
        this.model_class = PostAttachment.class;
        this.table_name = "PostAttachment";
    }
    
    public List<PostAttachment> FindPostsAttachments(Post post) throws Exception{
        Query q;
        q = MysqlSingleton
                .OpenSession()
                .createQuery("SELECT p FROM PostAttachment p WHERE p.post = :post")
                .setParameter("post", post);
        
        List<PostAttachment> attachments = q.list();
        if (attachments.size() > 0)
            return attachments;
        
        return null;
    }
    
}
