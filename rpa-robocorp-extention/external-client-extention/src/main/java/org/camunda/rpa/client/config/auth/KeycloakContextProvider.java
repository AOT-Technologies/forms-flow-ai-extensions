package org.camunda.rpa.client.config.auth;

import org.camunda.rpa.client.config.KeycloakConfiguration;
import org.camunda.rpa.client.exception.IdentityProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.nio.charset.Charset;
import java.util.*;

import static org.camunda.rpa.client.util.ObjectUtil.*;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

/**
 * Keycloak context provider.
 * <p>
 * Manages access tokens for then Keycloak REST API.
 */
public class KeycloakContextProvider {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakContextProvider.class);

    private KeycloakContext context;

    WebClient webClient;

    KeycloakConfiguration keycloakConfiguration;

    /**
     * Creates a new Keycloak context provider
     * @param keycloakConfiguration the Keycloak configuration
     * @param webClient REST template
     */
    public KeycloakContextProvider(KeycloakConfiguration keycloakConfiguration, WebClient webClient) {
        this.keycloakConfiguration = keycloakConfiguration;
        this.webClient = webClient;
    }

    /**
     * Requests an access token for the configured Keycloak client.
     * @return new Keycloak context holding the access token
     */
    private KeycloakContext openAuthorizationContext() {

        String authToken = keycloakConfiguration.getClientId() + ":" + keycloakConfiguration.getClientSecret();
        String encodedAuthToken = encodeToBase64(authToken);

        try {
            Map<String, Object> response = webClient.post().uri(keycloakConfiguration.getTokenUri())
                    .header(AUTHORIZATION, "Basic " + encodedAuthToken)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(BodyInserters.fromFormData("grant_type", keycloakConfiguration.getGrantType()))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            String accessToken = convertKeyToString(response, "access_token");
            String refreshToken = convertKeyToString(response, "refresh_token");
            long expiresInMillis = convertKeyToLong(response, "expires_in") * 1000;
            return new KeycloakContext(accessToken, expiresInMillis, refreshToken);

        } catch (Exception rce) {
            LOG.error(rce.getMessage());
            throw new IdentityProviderException("Unable to get access token from Keycloak server", rce);
        }
    }


    /**
     * Refreshs an access token for the configured Keycloak client.
     * @return the refreshed Keycloak context holding the access token
     */
    private KeycloakContext refreshToken() {

        String authToken = keycloakConfiguration.getClientId() + ":" + keycloakConfiguration.getClientSecret();
        String encodedAuthToken = encodeToBase64(authToken);

        Map<String, List<String>> targetMap = new HashMap<>();
        MultiValueMap<String, String> formData = new MultiValueMapAdapter(targetMap);
        formData.put("grant_type", Collections.singletonList("refresh_token"));
        formData.put("refresh_token", Collections.singletonList(context.getRefreshToken()));

        try {
            Map<String, Object> response = webClient.post().uri(keycloakConfiguration.getTokenUri())
                    .header(AUTHORIZATION, "Basic " + encodedAuthToken)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            String accessToken = convertKeyToString(response, "access_token");
            String refreshToken = convertKeyToString(response, "refresh_token");
            long expiresInMillis = convertKeyToLong(response, "expires_in") * 1000;
            return new KeycloakContext(accessToken, expiresInMillis, refreshToken);
        } catch (Exception rce) {
            LOG.error(rce.getMessage());
            throw new IdentityProviderException("Unable to refresh access token from Keycloak server", rce);
        }
    }


    /**
     * Creates a valid request entity for the Keycloak management API.
     * @return request entity with  access token set
     */
    public String createApiRequestAccessToken() {
        if (context == null) {
            context = openAuthorizationContext();
        } else if (context.needsRefresh()) {
            if (context.getRefreshToken() == null) {
                LOG.error("Error : missingRefreshToken");
                context = openAuthorizationContext();
            } else {
                try {
                    context = refreshToken();
                } catch (IdentityProviderException ipe) {
                    context = openAuthorizationContext();
                }
            }
        }
        return context.getAccessToken();
    }


    /**
     * Invalidates the current authorization context forcing to request a new token.
     */
    public void invalidateToken() { context = null;
    }

    private String encodeToBase64(String decodedString) {
        byte[] stringAsBytes = decodedString.getBytes(Charset.defaultCharset());
        return Base64.getEncoder()
                .encodeToString(stringAsBytes);
    }
}
