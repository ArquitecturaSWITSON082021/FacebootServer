/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.AbstractDao;
import database.MysqlSingleton;
import java.util.List;
import models.UserOauth;
import org.hibernate.Query;

/**
 *
 * @author Ivy
 */
public class UsersOauthDao extends AbstractDao<UserOauth> {
    
    public UsersOauthDao(){
        this.model_class = UserOauth.class;
        this.table_name = "UserOauth";
    }
    
    public UserOauth FindFirstUserByAccountId(int oauth_type, String account_id) throws Exception{
        Query q;
        q = MysqlSingleton
                .OpenSession()
                .createQuery("SELECT u FROM UserOauth u WHERE u.oauthType = :oauthType and u.accountId = :accountId")
                .setParameter("oauthType", oauth_type)
                .setParameter("accountId", account_id);
        
        List<UserOauth> rows = q.list();
        if (rows.size() > 0)
            return rows.get(0);
        
        return null;
    }
}
