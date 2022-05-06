package org.camunda.rpa.client.config.formsflow.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.client.interceptor.ClientRequestContext;
import org.camunda.rpa.client.config.auth.KeycloakContextProvider;
import org.camunda.rpa.client.config.auth.KeycloakRequestProvider;
import org.camunda.rpa.client.config.formsflow.FormioProperties;
import org.camunda.rpa.client.config.formsflow.FormsflowProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

@Component
public class FormioRequestProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakRequestProvider.class);

    private static FormioContextProvider formioContextProvider = null;

    private FormioRequestProvider(WebClient webClient, FormsflowProperties formsflowProperties, ObjectMapper objectMapper){
        formioContextProvider = new FormioContextProvider(webClient, formsflowProperties.getFormio(), objectMapper);
    }

    public void intercept(WebClient.RequestHeadersSpec requestHeadersSpec) {
        requestHeadersSpec.header("x-jwt-token", formioContextProvider.createFormioRequestAccessToken());
    }
}
