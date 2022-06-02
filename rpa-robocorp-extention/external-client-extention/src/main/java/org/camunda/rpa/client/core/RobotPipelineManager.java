package org.camunda.rpa.client.core;

import org.camunda.rpa.client.core.pipe.CommandRunnerService;
import org.camunda.rpa.client.core.pipe.HoloTreeDictionary;
import org.camunda.rpa.client.core.pipe.RobotDirectoryScanner;
import org.camunda.rpa.client.exception.RobotClientRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
/**
 *  rcc  run --interactive --task scripting -- --variable answer:https://www.google.com tasks.robot
 * author : Shibin Thomas
 */
@Component
public class RobotPipelineManager {

    private static final Logger LOG = LoggerFactory.getLogger(RobotPipelineManager.class);

    @Autowired
    private CommandRunnerService commandRunnerService;
    @Autowired
    private RobotDirectoryScanner robotDirectoryScanner;


    public String[] buildRobocorpRCCScript(String input, String robotName, String workingDirName, String taskFileName){

        String[] scripts = new String[2];

        File baseDir = robotDirectoryScanner.getRobotLocationMap().get(robotName);
        if(baseDir == null){
            throw new RobotClientRuntimeException("No appropriate robot directories identified", 500);
        }
        scripts[0] = "cd "+baseDir.getAbsolutePath();

        scripts[1] = "rcc run "+
                " --space "+HoloTreeDictionary.getHoloTreeInfo().get(robotName)+
                " --interactive -- "+
                (workingDirName != null ? " --outputdir output/"+workingDirName+" " : " ")+
                (input == null?"":input)+
                (taskFileName != null? " "+taskFileName:" tasks.robot");

        return scripts;
    }

    public void runRobocorpRccScript(String[] scripts){
        LOG.debug("Running robot scripts using rcc");
        commandRunnerService.run(scripts);
    }

}
