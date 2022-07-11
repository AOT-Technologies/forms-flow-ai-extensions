package org.camunda.rpa.client.config;

import org.camunda.rpa.client.data.entity.RobotHandlerConfig;
import org.camunda.rpa.client.data.repository.RepoFinder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.camunda.bpm.client.spring.impl.client.ClientConfiguration;
import org.camunda.bpm.client.spring.impl.subscription.SubscriptionConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Client Properties for Camunda RPA Client Spring Boot application.
 * author:Shibin Thomas
 */
@Data
@ConfigurationProperties(prefix = "client")
public class ClientProperties extends ClientConfiguration {

    @Autowired
    @JsonIgnore
    private RepoFinder repoFinder;

    @PostConstruct
    protected void init(){
        List<RobotHandlerConfig> configList = (List<RobotHandlerConfig>) repoFinder.getConfigRepository().findAll();
        if(configList.size() > 0){
            configList.forEach(config -> {
                SubscriptionConfiguration subscriptionConfiguration = new SubscriptionConfiguration();
                subscriptionConfiguration.setTopicName(config.getTopicName());
                String variableNamesStr = config.getVariableNames();
                if(variableNamesStr != null && variableNamesStr.length() > 0) {
                    List<String> variableNames = Arrays.asList(variableNamesStr.replace(" ","").split(","));
                    subscriptionConfiguration.setVariableNames(variableNames);
                }
                subscriptionConfiguration.setProcessDefinitionKey(config.getProcessDefinitionKey());
                subscriptions.put(config.getTopicName(), subscriptionConfiguration);
            });
        }
    }

    private Map<String, SubscriptionConfiguration> subscriptions = new HashMap<>();

    private KeycloakConfiguration auth;

    public SubscriptionConfiguration findSubscriptionPropsByTopicName(String topic) {
        return subscriptions.get(topic);
    }

    public KeycloakConfiguration getAuthenticationProperties() {
        return auth;
    }
}
