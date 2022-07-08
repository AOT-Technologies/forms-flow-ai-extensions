package org.camunda.rpa.client.config.impl;

import org.camunda.rpa.client.config.ClientProperties;
import org.camunda.bpm.client.spring.impl.subscription.SpringTopicSubscriptionImpl;
import org.camunda.bpm.client.spring.impl.subscription.SubscriptionConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;

import java.util.function.Predicate;

/**
 * @author Shibin Thomas
 */
public class PropertiesAwareSpringTopicSubscription extends SpringTopicSubscriptionImpl {

    @Autowired
    protected ClientProperties clientProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        mergeSubscriptionWithProperties();
        super.afterPropertiesSet();
    }

    @Override
    protected Predicate<ApplicationEvent> isEventThatCanStartSubscription() {
        return event -> event instanceof ApplicationStartedEvent;
    }

    protected void mergeSubscriptionWithProperties() {
        SubscriptionConfiguration merge = getSubscriptionConfiguration();

        String topicName = merge.getTopicName();
        SubscriptionConfiguration subscriptionProperties =
                clientProperties.findSubscriptionPropsByTopicName(topicName);

        if (subscriptionProperties != null) {
            if (subscriptionProperties.getAutoOpen() != null) {
                merge.setAutoOpen(subscriptionProperties.getAutoOpen());
            }
            if (subscriptionProperties.getLockDuration() != null) {
                merge.setLockDuration(subscriptionProperties.getLockDuration());
            }
            if (subscriptionProperties.getVariableNames() != null) {
                merge.setVariableNames(subscriptionProperties.getVariableNames());
            }
            if (subscriptionProperties.getBusinessKey() != null) {
                merge.setBusinessKey(subscriptionProperties.getBusinessKey());
            }
            if (subscriptionProperties.getProcessDefinitionId() != null) {
                merge.setProcessDefinitionId(subscriptionProperties.getProcessDefinitionId());
            }
            if (subscriptionProperties.getProcessDefinitionIdIn() != null) {
                merge.setProcessDefinitionIdIn(subscriptionProperties.getProcessDefinitionIdIn());
            }
            if (subscriptionProperties.getProcessDefinitionKey() != null) {
                merge.setProcessDefinitionKey(subscriptionProperties.getProcessDefinitionKey());
            }
            if (subscriptionProperties.getProcessDefinitionKeyIn() != null) {
                merge.setProcessDefinitionKeyIn(subscriptionProperties.getProcessDefinitionKeyIn());
            }
            if (subscriptionProperties.getProcessDefinitionVersionTag() != null) {
                merge.setProcessDefinitionVersionTag(subscriptionProperties.getProcessDefinitionVersionTag());
            }
            if (subscriptionProperties.getProcessVariables() != null) {
                merge.setProcessVariables(subscriptionProperties.getProcessVariables());
            }
            if (subscriptionProperties.getWithoutTenantId() != null) {
                merge.setWithoutTenantId(subscriptionProperties.getWithoutTenantId());
            }
            if (subscriptionProperties.getTenantIdIn() != null) {
                merge.setTenantIdIn(subscriptionProperties.getTenantIdIn());
            }
            if (subscriptionProperties.getIncludeExtensionProperties() != null) {
                merge.setIncludeExtensionProperties(subscriptionProperties.getIncludeExtensionProperties());
            }

            setSubscriptionConfiguration(merge);
        }
    }

}
