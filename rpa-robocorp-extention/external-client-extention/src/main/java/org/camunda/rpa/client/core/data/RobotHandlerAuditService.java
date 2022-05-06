package org.camunda.rpa.client.core.data;

import org.camunda.rpa.client.core.RobotIOManager;
import org.camunda.rpa.client.data.constants.RobotResponseType;
import org.camunda.rpa.client.data.repository.RepoFinder;
import org.camunda.rpa.client.exception.RobotClientRuntimeException;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.rpa.client.data.constants.RobotStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.camunda.rpa.client.data.entity.RobotHandlerAudit;
import org.camunda.rpa.client.data.entity.RobotHandlerConfig;

/**
 * Service class aimed to create / update an audit entry.
 * 
 * @author Sneha Suresh
 * 
 */
@Service
public class RobotHandlerAuditService implements IDataService {

	private static final Logger LOG = LoggerFactory.getLogger(RobotHandlerAuditService.class);

	@Autowired
	private RobotIOManager robotIOManager;

	@Autowired
	private RepoFinder repoFinder;

	/**
	 * This method is intended to make a new entry on Audit table
	 * @param externalTask 
	 * @param config
	 * @return
	 */
	public RobotHandlerAudit createAudit(ExternalTask externalTask, RobotHandlerConfig config) {
		if (externalTask == null)
			throw new RobotClientRuntimeException("ExternalTask data cannot be null", 500);
		RobotHandlerAudit robotHandlerAudit = new RobotHandlerAudit();
		robotHandlerAudit.setHandlerConfig(config);
		robotHandlerAudit.setTaskId(externalTask.getId());
		robotHandlerAudit.setStatus(RobotStatus.IN_PROGRESS);
		if (RobotResponseType.FILE.equals(config.getResponseType())  || RobotResponseType.FILE_MULTI.equals(config.getResponseType())) {
			config.setWorkingDirName(robotIOManager.getOutputDirName(config.getRobotName()));
		}
		return repoFinder.getAuditRepository().save(robotHandlerAudit);
	}

	/**
	 * The method updateAudit can make an update on Audit table
	 * @param robotHandlerAudit
	 */
	public void updateAuditSuccess(RobotHandlerAudit robotHandlerAudit) {
		robotHandlerAudit.setStatus(RobotStatus.SUCCESS);
		repoFinder.getAuditRepository().save(robotHandlerAudit);
	}

	/**
	 * The method updateAuditFailure can make an update on Audit table
	 * @param robotHandlerAudit
	 * @param errorDetails
	 */
	public void updateAuditFailure(RobotHandlerAudit robotHandlerAudit, String errorDetails) {
		robotHandlerAudit.setDetails(errorDetails);
		robotHandlerAudit.setStatus(RobotStatus.FAILED);
		repoFinder.getAuditRepository().save(robotHandlerAudit);
	}

}
