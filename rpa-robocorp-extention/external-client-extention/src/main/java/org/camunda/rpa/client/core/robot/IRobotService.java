package org.camunda.rpa.client.core.robot;

import org.camunda.rpa.client.data.RobotInput;
import org.camunda.rpa.client.data.entity.RobotHandlerAudit;
import org.camunda.rpa.client.data.entity.RobotHandlerConfig;

import java.util.List;

/**
 * author : Shibin Thomas
 */
public interface IRobotService {

    boolean runRobot(List<RobotInput> robotInputs, RobotHandlerAudit audit);
}
