package org.camunda.rpa.client.config.auth;

import org.camunda.rpa.client.config.KeycloakConfiguration;
import org.camunda.bpm.client.interceptor.ClientRequestContext;
import org.camunda.bpm.client.interceptor.ClientRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

/**
 * Keycloak Request Provider 
 */
@Component
public class KeycloakRequestProvider implements ClientRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakRequestProvider.class);

    private static KeycloakContextProvider keycloakContextProvider = null;

    public void init(KeycloakConfiguration configuration, WebClient webClient){
        LOGGER.debug("Initialized KeycloakRequestProvider");
        keycloakContextProvider = new KeycloakContextProvider(configuration, webClient);
    }

    public void intercept(ClientRequestContext requestContext) {
        requestContext.addHeader(AUTHORIZATION, "Bearer " + keycloakContextProvider.createApiRequestAccessToken());
    }

}
