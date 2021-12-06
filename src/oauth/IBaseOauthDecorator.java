/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oauth;

/**
 *
 * @author Ivy
 */
public interface IBaseOauthDecorator {
    
    public String GetOauthUrl(String connection_id) throws Exception;

    // Returns an user access token, given code.
    public OauthToken GetUserAccessToken(String code, String secret) throws Exception;
    
    // Returns an user oauth info, based on the given code.
    public OauthUserInfo GetUserInfo(String code, String secret) throws Exception;
    
    public models.User GetUserByOauthCode(String code, String secret) throws Exception;
}
