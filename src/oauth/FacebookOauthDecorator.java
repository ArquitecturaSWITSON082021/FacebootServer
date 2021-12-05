/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oauth;

import models.User;
import models.UserOauth;
import oauth.AbstractOauthDecorator;
import org.json.JSONObject;
import providers.ConfigProvider;
import sun.net.www.http.HttpClient;

/**
 *
 * @author Ivy
 */
public class FacebookOauthDecorator extends AbstractOauthDecorator implements IBaseOauthDecorator  {
    
    public FacebookOauthDecorator(){
        super(FacebootNet.Engine.OauthType.Facebook, ConfigProvider.FindValueByName("OAUTH_FB_ENDPOINT"));
        oAuthEndpointUrl = EndpointUrl;
        oAuthDialogUrl = ConfigProvider.FindValueByName("OAUTH_FB_DIALOG");
        clientId = ConfigProvider.FindValueByName("OAUTH_FB_CID");
        clientSecret = ConfigProvider.FindValueByName("OAUTH_FB_CST");
        redirectUri =  ConfigProvider.FindValueByName("OAUTH_FB_REDIRECT");
    }
    
    @Override
    public String GetOauthUrl(String connection_id)  throws Exception{
        return oAuthDialogUrl.replace("$client_id", clientId)
                             .replace("$state", String.format("%d$%s", oAuthType, connection_id))
                             .replace("$redirect_uri", redirectUri);
    }
    
    @Override
    public String GetUserAccessToken(String code) throws Exception{
        String url = String.format("/oauth/access_token?client_id=%s&client_secret=%s&redirect_uri=%s&code=%s",
                clientId, clientSecret, redirectUri, code);
        JSONObject json = new JSONObject(Get(url));
        String accessToken = json.getString("access_token");
        if (accessToken == null || accessToken.length() <= 0)
            throw new Exception("Got invalid access token.");
        return accessToken;
    }
    
    @Override
    public OauthUserInfo GetUserInfo(String code) throws Exception{
        String access_token = GetUserAccessToken(code);
        String url = String.format("/v12.0/me?access_token=%s&fields=id,name,email,first_name,last_name,hometown,picture.type(large){url,is_silhouette},gender,birthday",
                access_token);
        JSONObject json = new JSONObject(Get(url));
        OauthUserInfo user = new OauthUserInfo();
        user.id = json.getString("id");
        if (user.id == null || user.id.length() <= 0)
            throw new Exception("Invalid user info response.");
        
        user.name = json.getString("name");
        user.email = json.getString("email");
        user.firstName = json.getString("first_name");
        user.lastName = json.getString("last_name");
        user.gender = json.getString("gender");
        user.birthday = json.getString("birthday");
        
        if (json.has("picture")){
            String pfpUrl = json.getJSONObject("picture").getJSONObject("data").getString("url");
            user.profilePicture = GetByteArray(pfpUrl);
        }
        
        return user;
    }
    
    @Override
    public models.User GetUserByOauthCode(String code) throws Exception {
        OauthUserInfo info = GetUserInfo(code);
        UserOauth userOauth =  dao.DaoProvider.UsersOauth.FindFirstUserByAccountId(oAuthType, info.id);
        if (userOauth == null || userOauth.getUserId() <= 0)
            return null;
        User user = dao.DaoProvider.Users.FindFirstById(userOauth.getUserId());
        if (user == null || user.getId() <= 0)
            return null;
        return user;
    }
}
