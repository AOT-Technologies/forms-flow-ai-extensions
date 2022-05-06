package org.camunda.rpa.client.handlers;

import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.rpa.client.data.RobotInput;
import org.camunda.rpa.client.data.entity.RobotHandlerAudit;
import org.camunda.rpa.client.data.entity.RobotHandlerConfig;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.engine.variable.VariableMap;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Shibin Thomas
 * 
 */
public interface IRobotHandler extends TaskHandler{

    RobotHandlerAudit startAudit(ExternalTask externalTask);
    List<RobotInput> buildInputWithAdditionalVariables(ExternalTask externalTask, RobotHandlerConfig config, Map<String, Object> additionalVariables);
    List<RobotInput> buildInput(ExternalTask externalTask, RobotHandlerConfig config);
    boolean runRobot(List<RobotInput> robotInputs, RobotHandlerConfig config);
    VariableMap collectResponse(RobotHandlerAudit robotHandlerAudit, String formUrl);
    void completeAudit(RobotHandlerAudit audit, boolean status, String errorDetails);
    void handleFailure(ExternalTask externalTask, ExternalTaskService externalTaskService, RobotHandlerAudit robotHandlerAudit, String message);
    void doCleanup(RobotHandlerAudit robotHandlerAudit);

    default Integer getHandlerId(){
        return -1;
    }
}
