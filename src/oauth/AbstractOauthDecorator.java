/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oauth;

import providers.HttpProvider;

/**
 *
 * @author Ivy
 */
public class AbstractOauthDecorator extends HttpProvider {
    
    protected int oAuthType;
    protected String oAuthEndpointUrl;
    protected String oAuthDialogUrl;
    protected String clientId;
    protected String clientSecret;
    protected String redirectUri;

    public AbstractOauthDecorator(int oAuthType, String url) {
        super(url);
        this.oAuthType = oAuthType;
    }
    
    public String getoAuthEndpointUrl() {
        return oAuthEndpointUrl;
    }
    
    public String getoAuthDialogUrl() {
        return oAuthDialogUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
    
    
    
}
