package org.camunda.rpa.client.core;

import org.camunda.rpa.client.config.ClientProperties;
import org.camunda.rpa.client.config.RobotHandlerProperties;
import org.camunda.rpa.client.data.RobotInput;
import org.camunda.rpa.client.data.TaskDataInput;
import org.camunda.rpa.client.core.io.RobotInputService;
import org.camunda.rpa.client.core.io.RobotResponseService;
import org.camunda.rpa.client.data.constants.RobotResponseType;
import org.camunda.rpa.client.data.constants.RobotType;
import org.camunda.rpa.client.data.entity.RobotHandlerAudit;
import org.camunda.rpa.client.data.entity.RobotHandlerConfig;
import org.camunda.bpm.client.spring.impl.subscription.SubscriptionConfiguration;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.engine.variable.VariableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Manager class for Input / Response service classes. author : Shibin Thomas
 */
@Component
public class RobotIOManager {

	private static final Logger LOG = LoggerFactory.getLogger(RobotIOManager.class);

	@Autowired
	private RobotInputService robotInputService;

	@Autowired
	private RobotResponseService robotResponseService;

	@Autowired
	private ClientProperties clientProperties;

	@Autowired
	private RobotHandlerProperties handlerProperties;

	public List<RobotInput> buildInput(ExternalTask externalTask, RobotHandlerConfig config,
			Map<String, Object> additionalVariables) {

		SubscriptionConfiguration subscriptionConfiguration = clientProperties.getSubscriptions()
				.get(config.getTopicName());
		TaskDataInput input = new TaskDataInput(externalTask, subscriptionConfiguration.getVariableNames(),
				additionalVariables);

		return robotInputService.buildInputVariables(input);
	}

	/**
	 * This function will generate a random number and assign it as the file name
	 * The output directory name will derive from the random number
	 * 
	 * @param robotName
	 * @return workingDirName
	 */
	public synchronized String getOutputDirName(String robotName) {
		String workingDirName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
		boolean status = robotResponseService.createOutputDirectory(robotName, workingDirName);

		return status ? workingDirName : "-1";
	}

	/**
	 * This function will generate response for camunda Response type is
	 * configurable and based on the configuration the response will be generated.
	 * 
	 * @param audit
	 * @return
	 */
	public VariableMap getOutputData(RobotHandlerAudit audit, String formUrl) {

		RobotHandlerConfig config = audit.getHandlerConfig();
		VariableMap variableMap = null;
		RobotType robotType = handlerProperties.getRobotType();

		if (RobotResponseType.FILE.equals(config.getResponseType()) && RobotType.ROBOCORP_RCC.equals(robotType)) {
			variableMap = robotResponseService.readRCCRobotFileResponse(config.getRobotName(),
					config.getWorkingDirName(), formUrl);
		}
		if (RobotResponseType.FILE.equals(config.getResponseType()) && RobotType.ROBOCORP_CLOUD.equals(robotType)) {
			try {
				variableMap = robotResponseService.readRobocorpCloudResponseFile(audit, formUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return variableMap;
	}

	/**
	 * This function will clear the robot current output directory and it's data
	 * 
	 * @param audit
	 * @return
	 */
	public boolean clearAndRemoveOutputDirectory(RobotHandlerAudit audit) {

		RobotHandlerConfig config = audit.getHandlerConfig();
		if (RobotResponseType.FILE.equals(config.getResponseType())) {
			return robotResponseService.clearAndRemoveOutputDirectory(config.getRobotName(),
					config.getWorkingDirName());
		} else {
			return false;
		}
	}
}
