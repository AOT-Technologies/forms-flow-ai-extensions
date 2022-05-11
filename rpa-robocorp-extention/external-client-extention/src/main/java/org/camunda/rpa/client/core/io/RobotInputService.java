package org.camunda.rpa.client.core.io;

import org.camunda.rpa.client.config.RobotHandlerProperties;
import org.camunda.rpa.client.data.RobotInput;
import org.camunda.rpa.client.data.TaskDataInput;
import org.camunda.rpa.client.data.constants.RobotType;
import org.camunda.rpa.client.data.entity.RobotHandlerConfig;
import org.camunda.bpm.client.task.ExternalTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for building input data for the robots
 * This receives data from handlers, which then extracted and send back as appropriate
 * robot input data.
 * author: Shibin Thomas
 */
@Service
public class RobotInputService {

    private static final Logger LOG = LoggerFactory.getLogger(RobotInputService.class);

    /**
     * This function will build input variables for the robot.
     * Handlers are invoking this service via the manager class
     * @param input
     * @return
     */
    public List<RobotInput> buildInputVariables(TaskDataInput input, RobotHandlerConfig config){

        ExternalTask externalTask = input.getExternalTask();
        List<String> variableNames = input.getVariableNames();

        List<RobotInput> robotInputs = new ArrayList<>();

        for (String field : variableNames) {
            Object value = externalTask.getVariable(field);
            addRobotInputData(robotInputs, config, field, value);
        }
        if(input.getAdditionalVariables() != null){
            for(Map.Entry<String, Object> entry : input.getAdditionalVariables().entrySet()){
                addRobotInputData(robotInputs, config, entry.getKey(), entry.getValue());
            }
        }
        return robotInputs;
    }

    private void addRobotInputData(List<RobotInput> robotInputs, RobotHandlerConfig config, String field, Object value){
    	 RobotType robotType = config.getRobotType();
        if(value instanceof String && robotType != RobotType.ROBOCORP_CLOUD){
            String strValue = (String) value;
            strValue = "\""+strValue+"\"";
            robotInputs.add(new RobotInput(field, strValue));
        } else {
            robotInputs.add(new RobotInput(field, value));
        }
    }
}
