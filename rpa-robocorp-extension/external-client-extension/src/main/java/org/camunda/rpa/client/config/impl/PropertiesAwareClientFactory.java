package org.camunda.rpa.client.config.impl;

import org.camunda.rpa.client.config.KeycloakConfiguration;
import org.camunda.rpa.client.config.ClientProperties;
import org.camunda.rpa.client.config.auth.KeycloakRequestProvider;
import org.camunda.bpm.client.spring.impl.client.ClientConfiguration;
import org.camunda.bpm.client.spring.impl.client.ClientFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Shibin Thomas
 */
public class PropertiesAwareClientFactory extends ClientFactory {

    @Autowired
    private WebClient webClient;

    @Autowired
    protected ClientProperties clientProperties;

    @Autowired
    private KeycloakRequestProvider keycloakRequestProvider;

    @Override
    public void afterPropertiesSet() throws Exception {
        applyPropertiesFrom(clientProperties);
        addKeycloakAuthInterceptor();
        super.afterPropertiesSet();
    }

    protected void addKeycloakAuthInterceptor() {
        KeycloakConfiguration keycloakConfiguration = clientProperties.getAuthenticationProperties();
        if (keycloakConfiguration != null) {

            keycloakRequestProvider.init(keycloakConfiguration, webClient);

            getRequestInterceptors().add(keycloakRequestProvider);
        }
    }

    public void applyPropertiesFrom(ClientProperties clientConfigurationProps) {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        if (clientConfigurationProps.getBaseUrl() != null) {
            clientConfiguration.setBaseUrl(clientConfigurationProps.getBaseUrl());
        }
        if (clientConfigurationProps.getWorkerId() != null) {
            clientConfiguration.setWorkerId(clientConfigurationProps.getWorkerId());
        }
        if (clientConfigurationProps.getMaxTasks() != null) {
            clientConfiguration.setMaxTasks(clientConfigurationProps.getMaxTasks());
        }
        if (clientConfigurationProps.getUsePriority() != null && !clientConfigurationProps.getUsePriority()) {
            clientConfiguration.setUsePriority(false);
        }
        if (clientConfigurationProps.getDefaultSerializationFormat() != null) {
            clientConfiguration.setDefaultSerializationFormat(clientConfigurationProps.getDefaultSerializationFormat());
        }
        if (clientConfigurationProps.getDateFormat() != null) {
            clientConfiguration.setDateFormat(clientConfigurationProps.getDateFormat());
        }
        if (clientConfigurationProps.getLockDuration() != null) {
            clientConfiguration.setLockDuration(clientConfigurationProps.getLockDuration());
        }
        if (clientConfigurationProps.getAsyncResponseTimeout() != null) {
            clientConfiguration.setAsyncResponseTimeout(clientConfigurationProps.getAsyncResponseTimeout());
        }
        if (clientConfigurationProps.getDisableAutoFetching() != null &&
                clientConfigurationProps.getDisableAutoFetching()) {
            clientConfiguration.setDisableAutoFetching(true);
        }
        if (clientConfigurationProps.getDisableBackoffStrategy() != null &&
                clientConfigurationProps.getDisableBackoffStrategy()) {
            clientConfiguration.setDisableBackoffStrategy(true);
        }
        setClientConfiguration(clientConfiguration);
    }

}
