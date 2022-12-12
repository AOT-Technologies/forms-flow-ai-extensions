package org.camunda.rpa.client.config.formsflow.auth;


public class FormioContext {

    String accessToken;

    private long expiresAt;

    public FormioContext(String accessToken, long expiresAt) {
        this.accessToken = accessToken;
        this.expiresAt = expiresAt + System.currentTimeMillis();
    }

    /**
     * Checks if the token needs a refresh or not
     * @return
     */
    public boolean needsRefresh() {
        return System.currentTimeMillis() >= expiresAt;
    }

    public String getAccessToken() { return accessToken; }
}
