package org.camunda.rpa.client.config;

import lombok.Data;

/**
 * Keycloak Configuration
 */
@Data
public class KeycloakConfiguration {

    private String clientId;
    private String clientSecret;
    private String grantType;
    private String tokenUri;
}
