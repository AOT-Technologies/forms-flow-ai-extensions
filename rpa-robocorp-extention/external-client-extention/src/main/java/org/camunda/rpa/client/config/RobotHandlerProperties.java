package org.camunda.rpa.client.config;

import org.camunda.rpa.client.data.constants.RobotType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * robot handler properties
 * author:Shibin Thomas
 */
@Data
@ConfigurationProperties(prefix = "robot")
public class RobotHandlerProperties {

    /**
     * Base directory of the robot.
     * this can be left empty if working with normal deployment, default location will be then
     * resources/robots folder
     * In case of docker, this directory will be /opt/robots
     */
    private String baseDir;
    /**
     * Robot type will be based on the way how the robots run
     * available options are rcc / cloud
     */
    private RobotType robotType;

}
