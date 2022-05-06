package org.camunda.rpa.client.core;

import org.camunda.rpa.client.config.RobotHandlerProperties;
import org.camunda.rpa.client.core.robot.IRobotService;
import org.camunda.rpa.client.data.RobotInput;
import org.camunda.rpa.client.core.robot.RCCService;
import org.camunda.rpa.client.core.robot.RobocorpCloudService;
import org.camunda.rpa.client.data.constants.RobotType;
import org.camunda.rpa.client.data.entity.RobotHandlerConfig;
import org.camunda.rpa.client.exception.RobotClientRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RobotManager manages robot services from com.camunda.rpa.client.core.robot.*
 * This class will identify the service to run robots and manages them.
 * author : Shibin Thomas
 */
@Component
public class RobotManager {

    private static final Logger LOG = LoggerFactory.getLogger(RobotManager.class);

    @Autowired
    private RobotHandlerProperties handlerProperties;
    @Autowired
    private RCCService rccService;
    @Autowired
    private RobocorpCloudService robocorpCloudService;

    /**
     * runRobot run the robot service by using a user defined property to identify.
     * @param robotInputs
     * @param config
     */
    public boolean runRobot(List<RobotInput> robotInputs, RobotHandlerConfig config){
        IRobotService robotService = getRobotService();
        return robotService.runRobot(robotInputs, config);
    }

    // get robot services by using user defined {robotType} property - refer RobotType enum class
    private IRobotService getRobotService(){

        IRobotService robotService;
        RobotType robotType = handlerProperties.getRobotType();

        if (robotType == RobotType.ROBOCORP_CLOUD) {
            robotService = robocorpCloudService;
        } else if(RobotType.ROBOCORP_RCC.equals(robotType)){
            robotService = rccService;
        } else {
            throw new RobotClientRuntimeException("No suitable service identified for robot type ="+robotType, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return robotService;
    }
}
