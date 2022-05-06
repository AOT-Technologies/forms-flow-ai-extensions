package org.camunda.rpa.client.core;

import org.camunda.rpa.client.core.data.RobotHandlerAuditService;
import org.camunda.rpa.client.data.entity.RobotHandlerAudit;
import org.camunda.rpa.client.data.entity.RobotHandlerConfig;
import org.camunda.rpa.client.data.repository.RepoFinder;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.rpa.client.exception.RobotClientDataException;
import org.camunda.rpa.client.exception.RobotClientRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * RobotDataManager is the gateway / manager for com.camunda.rpa.client.core.data.*
 * This class includes methods for managing the audit, handler configuration etc..
 * author : Shibin Thomas
 */
@Component
public class RobotDataManager {

    private static final Logger LOG = LoggerFactory.getLogger(RobotDataManager.class);

    @Autowired
    private RepoFinder repoFinder;
    @Autowired
    private RobotHandlerAuditService auditService;

    /**
     * initAudit can make a new entry on Audit table
     * This method use Handler configuration to map robot with the audit.
     *
     * @param externalTask
     * @param handlerId
     * @return
     */
    public RobotHandlerAudit initAudit(ExternalTask externalTask, Integer handlerId) {
        LOG.debug("Initializing audit for handler = " + handlerId);
        Optional<RobotHandlerConfig> optional = repoFinder.getConfigRepository().findById(handlerId);
        RobotHandlerConfig config = optional.orElse(null);
        return auditService.createAudit(externalTask, config);
    }

    /**
     * finalizeAudit will complete the audit process by invoking com.camunda.rpa.client.core.data.RobotHandlerAuditService
     * updateAuditSuccess / updateAuditFailure by a flag.
     * status flag will be true if the robot was ran successfully
     * status flag will be true in case of a failure. On failure error message will be saved in audit.
     *
     * @param audit
     * @param status
     * @param errorDetails
     */
    public void finalizeAudit(RobotHandlerAudit audit, boolean status, String errorDetails) {

        if (status) {
            auditService.updateAuditSuccess(audit);
        } else {
            auditService.updateAuditFailure(audit, errorDetails);
        }
    }

    /**
     * Get All the robot configuration data
     * @return
     */
    public List<RobotHandlerConfig> listRobotConfigData() {
        return (ArrayList<RobotHandlerConfig>) repoFinder.getConfigRepository().findAll();
    }

    /**
     * Update the robot configuration data
     * @param robotHandlerConfig
     * @return
     */
    public RobotHandlerConfig updateRobotHandlerConfigData(RobotHandlerConfig robotHandlerConfig) throws RobotClientDataException {

        Integer handlerId = robotHandlerConfig != null ? robotHandlerConfig.getHandlerId() : null;
        if (handlerId != null) {
            if (repoFinder.getConfigRepository().findById(robotHandlerConfig.getHandlerId()).isPresent()) {
                return repoFinder.getConfigRepository().save(robotHandlerConfig);
            } else {
                throw new RobotClientDataException("No suitable data found for handler id = " + handlerId);
            }
        } else {
            throw new RobotClientDataException("No handler id present in the data");
        }
    }

    /**
     * Add a new robot configuration entry
     * @param robotHandlerConfig
     * @return
     */
    public RobotHandlerConfig saveRobotHandlerConfigData(RobotHandlerConfig robotHandlerConfig) throws RobotClientDataException {

        if (robotHandlerConfig == null) {
            throw new RobotClientDataException("No config data provided");
        }
        return repoFinder.getConfigRepository().save(robotHandlerConfig);
    }
}
