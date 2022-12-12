package org.camunda.rpa.client.config.impl;

import org.camunda.bpm.client.spring.impl.client.ClientPostProcessor;
import org.camunda.bpm.client.spring.impl.subscription.SubscriptionPostProcessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Shibin Thomas
 */
@Configuration
public class ClientAutoConfiguration {

    @Bean
    public static SubscriptionPostProcessor subscriptionPostprocessor() {
        return new SubscriptionPostProcessor(PropertiesAwareSpringTopicSubscription.class);
    }

    @Bean
    public static ClientPostProcessor clientPostProcessor() {
        return new ClientPostProcessor(PropertiesAwareClientFactory.class);
    }
}
