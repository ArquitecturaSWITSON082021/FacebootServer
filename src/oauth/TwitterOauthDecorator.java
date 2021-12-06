/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oauth;

import ciphers.CipherProvider;
import ciphers.HashProvider;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
public class TwitterOauthDecorator extends AbstractOauthDecorator implements IBaseOauthDecorator {

    public TwitterOauthDecorator() {
        super(FacebootNet.Engine.OauthType.Twitter, ConfigProvider.FindValueByName("OAUTH_TW_ENDPOINT"));
        oAuthEndpointUrl = EndpointUrl;
        oAuthDialogUrl = ConfigProvider.FindValueByName("OAUTH_TW_DIALOG");
        clientId = ConfigProvider.FindValueByName("OAUTH_TW_CID");
        clientSecret = ConfigProvider.FindValueByName("OAUTH_TW_CST");
        redirectUri = ConfigProvider.FindValueByName("OAUTH_TW_REDIRECT");
    }
    
    private String getCompositeKey(String requestToken){
        return URLEncoder.encode(clientSecret) + "&" + URLEncoder.encode(requestToken);
    }

    private String buildBaseString(String method, String baseUri, HashMap<String, String> params) {
        String _params = "";
        if (params.containsKey("oauth_token")){
            _params = String.format("include_email=%s&oauth_consumer_key=%s&oauth_nonce=%s&oauth_signature_method=%s&oauth_timestamp=%s&oauth_token=%s&oauth_version=%s",
                params.get("include_email"),
                params.get("oauth_consumer_key"),
                params.get("oauth_nonce"),
                params.get("oauth_signature_method"),
                params.get("oauth_timestamp"),           
                params.get("oauth_token"),
                params.get("oauth_version"));
        }else {
            _params = String.format("oauth_callback=%s&oauth_consumer_key=%s&oauth_nonce=%s&oauth_signature_method=%s&oauth_timestamp=%s&oauth_version=%s",
                params.get("oauth_callback"),
                params.get("oauth_consumer_key"),
                params.get("oauth_nonce"),
                params.get("oauth_signature_method"),
                params.get("oauth_timestamp"),
                params.get("oauth_version"));
        }
        _params = URLEncoder.encode(_params);
        return method + "&" + URLEncoder.encode(baseUri) + "&" + _params;
    }

    @Override
    public String GetOauthUrl(String connection_id) throws Exception {
        HashMap<String, String> oAuthParams = new HashMap<>();
        String timestamp = String.valueOf(new Date().getTime() / 1000);
        String redirectUrl = URLEncoder.encode(redirectUri  + "?state=" + String.format("%d$%s", oAuthType, connection_id));
        String oAuthMethod = "/oauth/request_token";
        String baseUri = oAuthEndpointUrl + oAuthMethod;
        oAuthParams.put("oauth_callback", redirectUrl);
        oAuthParams.put("oauth_consumer_key", clientId);
        oAuthParams.put("oauth_nonce", HashProvider.md5.Encrypt(timestamp));
        oAuthParams.put("oauth_signature_method", "HMAC-SHA1");
        oAuthParams.put("oauth_timestamp", timestamp);
        oAuthParams.put("oauth_version", "1.0");
        String baseString = buildBaseString("POST", baseUri, oAuthParams);
        String compositeKey = getCompositeKey("");
        String signature = Base64.encode(CipherProvider.hmacSha1.Encrypt(baseString.getBytes(), compositeKey.getBytes()));
        oAuthParams.put("oauth_signature", URLEncoder.encode(signature));
        
        String oAuthHeader = "";
        for (Map.Entry<String, String> param
                : oAuthParams.entrySet()) {
            oAuthHeader += param.getKey() + "=\"" + param.getValue() + "\", ";
        }
        
        oAuthHeader = oAuthHeader.substring(0, oAuthHeader.length() - 2);
        HashMap<String, String> requestTokenHeaders = new HashMap<>();
        requestTokenHeaders.put("Authorization", "OAuth " + oAuthHeader);
        requestTokenHeaders.put("Expect", "");
        requestTokenHeaders.put("Content-Type", "application/json");
        String response = Post(oAuthMethod, requestTokenHeaders);
        String[] args = response.split("&");
        if (args.length > 1){
        String oAuthToken = args[0].split("=")[1];       
        return oAuthEndpointUrl + "/oauth/authorize?oauth_token=" + oAuthToken;
        }else{
            System.out.println("[-] Failed to create new token. Error: " + args[0]);
            return null;
        }
    }

    @Override
    public OauthToken GetUserAccessToken(String code, String secret) throws Exception {
        HashMap<String, String> oAuthParams = new HashMap<>();
        String timestamp = String.valueOf(new Date().getTime() / 1000);
        String oAuthMethod = "/oauth/access_token?oauth_verifier=" + secret + "&oauth_token=" + code;
        String baseUri = oAuthEndpointUrl + oAuthMethod;
        oAuthParams.put("oauth_consumer_key", clientId);
        oAuthParams.put("oauth_nonce", HashProvider.md5.Encrypt(timestamp));
        oAuthParams.put("oauth_signature_method", "HMAC-SHA1");
        oAuthParams.put("oauth_timestamp", timestamp);
        oAuthParams.put("oauth_version", "1.0");
        String baseString = buildBaseString("POST", baseUri, oAuthParams);
        String compositeKey = getCompositeKey("");
        String signature = Base64.encode(CipherProvider.hmacSha1.Encrypt(baseString.getBytes(), compositeKey.getBytes()));
        oAuthParams.put("oauth_signature", URLEncoder.encode(signature));
        
        String oAuthHeader = "";
        for (Map.Entry<String, String> param
                : oAuthParams.entrySet()) {
            oAuthHeader += param.getKey() + "=\"" + param.getValue() + "\", ";
        }
        
        oAuthHeader = oAuthHeader.substring(0, oAuthHeader.length() - 2);
        HashMap<String, String> requestTokenHeaders = new HashMap<>();
        requestTokenHeaders.put("Authorization", "OAuth " + oAuthHeader);
        requestTokenHeaders.put("Expect", "");
        requestTokenHeaders.put("Content-Type", "application/json");
        String response = Post(oAuthMethod, requestTokenHeaders);
        String[] args = response.split("&");
        OauthToken token = new OauthToken();
        token.Token = args[0].split("=")[1];
        token.Secret = args[1].split("=")[1];
        return token;
    }

    @Override
    public OauthUserInfo GetUserInfo(String code, String secret) throws Exception {
        OauthToken access_token = GetUserAccessToken(code, secret);
        HashMap<String, String> oAuthParams = new HashMap<>();
        String timestamp = String.valueOf(new Date().getTime() / 1000);
        String oAuthMethod = "/1.1/account/verify_credentials.json";
        String baseUri = oAuthEndpointUrl + oAuthMethod;
        oAuthParams.put("include_email", "true");
        oAuthParams.put("oauth_consumer_key", clientId);
        oAuthParams.put("oauth_nonce", HashProvider.md5.Encrypt(timestamp));
        oAuthParams.put("oauth_signature_method", "HMAC-SHA1");
        oAuthParams.put("oauth_timestamp", timestamp);
        oAuthParams.put("oauth_token", URLEncoder.encode(access_token.Token));
        oAuthParams.put("oauth_version", "1.0");
        String baseString = buildBaseString("GET", baseUri, oAuthParams);
        String compositeKey = getCompositeKey(access_token.Secret);
        String signature = Base64.encode(CipherProvider.hmacSha1.Encrypt(baseString.getBytes(), compositeKey.getBytes()));
        oAuthParams.put("oauth_signature", URLEncoder.encode(signature));
        String oAuthHeader = "";
        for (Map.Entry<String, String> param
                : oAuthParams.entrySet()) {
            oAuthHeader += param.getKey() + "=\"" + param.getValue() + "\", ";
        }
        
        oAuthHeader = oAuthHeader.substring(0, oAuthHeader.length() - 2);
        HashMap<String, String> requestTokenHeaders = new HashMap<>();
        requestTokenHeaders.put("Authorization", "OAuth " + oAuthHeader);
        String response = Get(oAuthMethod + "?include_email=true", requestTokenHeaders);
        JSONObject json = new JSONObject(response);
        OauthUserInfo user = new OauthUserInfo();
        user.id = json.getString("id_str");
        if (user.id == null || user.id.length() <= 0) {
            throw new Exception("Invalid user info response.");
        }

        user.name = json.getString("name");
        user.email = json.getString("email");
        user.firstName = json.getString("name");
        user.lastName = "";
        user.gender = "";
        user.birthday = "";

        if (json.has("profile_image_url_https")) {
            String pfpUrl = json.getString("profile_image_url_https");
            user.profilePicture = GetByteArray(pfpUrl);
        }

        return user;
    }

    @Override
    public models.User GetUserByOauthCode(String code, String secret) throws Exception {
        OauthUserInfo info = GetUserInfo(code, secret);
        UserOauth userOauth = dao.DaoProvider.UsersOauth.FindFirstUserByAccountId(oAuthType, info.id);
        if (userOauth == null || userOauth.getUserId() <= 0) {
            return null;
        }
        User user = dao.DaoProvider.Users.FindFirstById(userOauth.getUserId());
        if (user == null || user.getId() <= 0) {
            return null;
        }
        return user;
    }
}
