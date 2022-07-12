package org.camunda.rpa.client;

import org.camunda.rpa.client.config.ClientProperties;
import org.camunda.rpa.client.config.formsflow.FormsflowProperties;
import org.camunda.rpa.client.config.RobotHandlerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Camunda RPA Client Spring Boot application.
 */
@EnableAsync
@SpringBootApplication
@EnableConfigurationProperties({ClientProperties.class, RobotHandlerProperties.class, FormsflowProperties.class})
public class CamundaRpaClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundaRpaClientApplication.class, args);
	}
}
