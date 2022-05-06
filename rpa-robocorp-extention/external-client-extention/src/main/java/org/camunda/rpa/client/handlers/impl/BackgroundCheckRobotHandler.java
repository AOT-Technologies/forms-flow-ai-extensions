package org.camunda.rpa.client.handlers.impl;

import org.camunda.rpa.client.core.RobotDataManager;
import org.camunda.rpa.client.data.RobotInput;
import org.camunda.rpa.client.core.RobotIOManager;
import org.camunda.rpa.client.core.RobotManager;
import org.camunda.rpa.client.data.entity.RobotHandlerAudit;
import org.camunda.rpa.client.data.entity.RobotHandlerConfig;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.rpa.client.handlers.IRobotHandler;
import org.camunda.rpa.client.handlers.TaskHandlerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible to handle the background-check robot
 *
 * @author Shibin Thomas
 *
 */
@Component
@ExternalTaskSubscription("background-check")
public class BackgroundCheckRobotHandler  extends TaskHandlerManager implements IRobotHandler {

	private static final Logger LOG = LoggerFactory.getLogger(BackgroundCheckRobotHandler.class);

	private static final Integer HANDLER_ID = 1;

	@Autowired
	private RobotIOManager robotIOManager;

	@Autowired
	private RobotDataManager robotDataManager;

	@Autowired
	private RobotManager robotManager;

	/**
	 * This method is used to manage and run the robot
	 *
	 * @param externalTask
	 * @param externalTaskService
	 *
	 */
	@Override
	public void subscribe(ExternalTask externalTask, ExternalTaskService externalTaskService) {

		LOG.debug("START - executing background check service");

		RobotHandlerAudit robotHandlerAudit = startAudit(externalTask);

		try {
			Map<String, Object> additionalVariables = new HashMap<>();
			additionalVariables.put("working-dir", robotHandlerAudit.getHandlerConfig().getWorkingDirName());

			List<RobotInput> robotInputs = buildInputWithAdditionalVariables(externalTask,
					robotHandlerAudit.getHandlerConfig(), additionalVariables);

			boolean status = runRobot(robotInputs, robotHandlerAudit.getHandlerConfig());

			VariableMap variableMap = null;
			if(status) {
				variableMap = collectResponse(robotHandlerAudit, externalTask.getVariable("formUrl"));
			}
			externalTaskService.complete(externalTask, variableMap);

			completeAudit(robotHandlerAudit, true, null);

		} catch (Exception ex) {
			ex.printStackTrace();
			handleFailure(externalTask, externalTaskService, robotHandlerAudit, ex.getMessage());
		} finally {
			doCleanup(robotHandlerAudit);
		}

		LOG.debug("END - executing background check service");
	}

	/**
	 * Create a new entry in the Audit table to acknowledge that the robot started successfully.
	 * This method invokes initAudit() to update the robot current status
	 * @param externalTask
	 *
	 */
	@Override
	public RobotHandlerAudit startAudit(ExternalTask externalTask) {
		return robotDataManager.initAudit(externalTask, getHandlerId());
	}

	/**
	 * This method will build input with additional variables for robot by invoking buildInput() method.
	 * @param externalTask
	 * @param config
	 * @param additionalVariables
	 *
	 */
	@Override
	public List<RobotInput> buildInputWithAdditionalVariables(ExternalTask externalTask, RobotHandlerConfig config,
															  Map<String, Object> additionalVariables) {
		return robotIOManager.buildInput(externalTask, config, additionalVariables);
	}

	/**
	 * This is an overriden method used to build the Input.
	 * @param externalTask
	 * @param config
	 *
	 */
	@Override
	public List<RobotInput> buildInput(ExternalTask externalTask, RobotHandlerConfig config) {

		return null;
	}

	/**
	 * This method invokes runRobot() to find out the robot type, build a script with input parameters and run using script manager
	 * @param robotInputs
	 * @param config
	 *
	 */
	@Override
	public boolean runRobot(List<RobotInput> robotInputs, RobotHandlerConfig config) {
		return robotManager.runRobot(robotInputs, config);
	}

	/**
	 * Once robot completed its task successfully, this method invokes and collect the response.
	 * @param robotHandlerAudit
	 * @return
	 *
	 */
	@Override
	public VariableMap collectResponse(RobotHandlerAudit robotHandlerAudit, String formUrl) {
		return robotIOManager.getOutputData(robotHandlerAudit, formUrl);
	}

	/**
	 * Complete the audit process and update the status in audit table
	 * @param audit
	 * @param status
	 * @param errorDetails
	 *
	 */
	@Override
	public void completeAudit(RobotHandlerAudit audit, boolean status, String errorDetails) {
		robotDataManager.finalizeAudit(audit, status, errorDetails);
	}

	@Override
	public void handleFailure(ExternalTask externalTask, ExternalTaskService externalTaskService, RobotHandlerAudit robotHandlerAudit, String message) {
		completeAudit(robotHandlerAudit, false, message);
		externalTaskService.handleFailure(externalTask, message, message, 0, 0);
	}

	@Override
	public void doCleanup(RobotHandlerAudit robotHandlerAudit) {
		robotIOManager.clearAndRemoveOutputDirectory(robotHandlerAudit);
	}

	/**
	 * @return
	 *
	 */
	@Override
	public Integer getHandlerId() {
		return HANDLER_ID;
	}
}
