/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.AbstractDao;
import database.MysqlSingleton;
import java.util.List;
import models.UserToken;
import org.hibernate.Query;

/**
 *
 * @author Ivy
 */
public class TokensDao extends AbstractDao<UserToken> {
    
    public TokensDao(){
        this.model_class = UserToken.class;
        this.table_name = "UserToken";
    }
    
    public UserToken FindFirstTokenByUuid(String uuid) throws Exception{
        Query q;
        q = MysqlSingleton
                .OpenSession()
                .createQuery("SELECT u FROM UserToken u WHERE u.uuid = :uuid")
                .setParameter("uuid", uuid);
        
        List<UserToken> tokens = q.list();
        if (tokens.size() > 0)
            return tokens.get(0);
        
        return null;
    }
    
}
