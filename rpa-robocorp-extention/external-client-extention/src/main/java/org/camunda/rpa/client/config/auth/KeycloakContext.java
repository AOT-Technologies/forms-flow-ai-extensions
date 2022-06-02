package org.camunda.rpa.client.config.auth;

/**
 * Keycloak context holding access token.
 * 
 * @author Shibin Thomas
 */
public class KeycloakContext {

    String accessToken;

    private long expiresAt;

    String refreshToken;

    public KeycloakContext(String accessToken, long expiresInMillis, String refreshToken) {
        this.accessToken = accessToken;
        expiresAt = System.currentTimeMillis() + expiresInMillis - 2000;
        this.refreshToken = refreshToken;
    }

    /**
     * Checks if the token needs a refresh or not
     * @return
     */
    public boolean needsRefresh() {
        return System.currentTimeMillis() >= expiresAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() { return accessToken; }
}
