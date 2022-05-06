package org.camunda.rpa.client.core.robot;

import org.camunda.rpa.client.core.io.parser.RobotOutputXMLParser;
import org.camunda.rpa.client.core.io.ro.RobotOutput;
import org.camunda.rpa.client.core.pipe.RobotDirectoryScanner;
import org.camunda.rpa.client.data.RobotInput;
import org.camunda.rpa.client.core.RobotPipelineManager;
import org.camunda.rpa.client.data.constants.ExternalClientConstants;
import org.camunda.rpa.client.data.entity.RobotHandlerConfig;
import org.camunda.rpa.client.exception.RobotClientRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * This class is implemented to run the robot by making a script with formatted input
 * 
 * author : Shibin Thomas
 */
@Service
public class RCCService implements IRobotService {

    private static final Logger LOG = LoggerFactory.getLogger(RCCService.class);

    private static final String format = " --variable ";

    @Autowired
    private RobotPipelineManager robotPipelineManager;

    @Autowired
    private RobotDirectoryScanner robotDirectoryScanner;

    @Autowired
    private RobotOutputXMLParser xmlParser;

    @Override
    public boolean runRobot(List<RobotInput> robotInputs, RobotHandlerConfig config) throws RobotClientRuntimeException{
        String[] scripts;
        String workingDirName = config.getWorkingDirName();
        if(robotInputs != null) {
            String[] formattedInput = buildInput(robotInputs);
            scripts = makeScriptWithInput(formattedInput, config.getRobotName(), workingDirName, config.getTaskFileName());
        } else {
            scripts = makeScriptWithoutInput(config.getRobotName(), workingDirName, config.getTaskFileName());
        }
        if(scripts == null || scripts.length == 0){
            throw new RobotClientRuntimeException("No script to run robots ", 500);
        }
        runScript(scripts);

        return checkRobotOutputStatus(config.getRobotName(), workingDirName);
    }
    
    /**
     * Get List of robot Inputs and  return it as response.
     * @param robotInputs
     * @return
     */
    private String[] buildInput(List<RobotInput> robotInputs){

        String[] formattedInput = new String[robotInputs.size()];

        for(int i = 0; i < robotInputs.size(); i++){
            formattedInput[i] = robotInputs.get(i).getField()+":"+robotInputs.get(i).getValue();
        }
        return formattedInput;
    }

    /**
     *
     * @param robotName
     * @param workingDirName
     * @param taskFileName
     * @return
     */
    private String[] makeScriptWithoutInput(String robotName, String workingDirName, String taskFileName){

        return buildScript(null, robotName, workingDirName, taskFileName);
    }
    
    /**
     * This method is responsible to make script with available inputs and robot name
     * @param inputs
     * param robotName
     * param workingDirName
     * param taskFileName
     * @return
     */
    private String[] makeScriptWithInput(String[] inputs, String robotName, String workingDirName, String taskFileName){

        StringBuilder sb = new StringBuilder();
        for(String input : inputs){
            sb.append(format).append(input);
        }
        return buildScript(sb.toString(), robotName, workingDirName, taskFileName);
    }
    
    /**
     * This method is responsible to build the script
     * @param variables
     * param robotName
     * param taskFileName
     * @return
     */
    private String[] buildScript(String variables, String robotName, String workingDirName, String taskFileName){
         return robotPipelineManager.buildRobocorpRCCScript(variables, robotName, workingDirName, taskFileName);
    }

    /**
     * This method is responsible to run the script
     * @param scripts
     */
    private void runScript(String[] scripts){
        robotPipelineManager.runRobocorpRccScript(scripts);
    }

    /**
     * Robot output status will be validated based on the fail count
     * fail count > 0 if any of the task failed to run
     * fail count = 0 and pass > 0 and skip = 0 if all task completed successfully
     * @param robotName
     * @param workingDirName
     * @return
     * @throws RobotClientRuntimeException
     */
    private boolean checkRobotOutputStatus(String robotName, String workingDirName) throws RobotClientRuntimeException{
        File outputDir = robotDirectoryScanner.getRobotWorkingDirectory(robotName, workingDirName);
        File[] outputFiles = outputDir.listFiles();
        LOG.debug("Working directory = "+workingDirName);
        if(outputFiles != null) {
            for (File file : outputFiles) {
                if(file.getName().equals(ExternalClientConstants.ROBOT_OUTPUT_XML_FILE)){
                    RobotOutput robotOutput =  xmlParser.parse(file);
                    if(robotOutput.getFail() > 0){
                        LOG.error("Robot Failed completing All the tasks - "+robotOutput.getMessages().toString());
                        throw new RobotClientRuntimeException("Robot Failed completing All the tasks - "+
                                robotOutput.getMessages().toString(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value());
                    } else if(robotOutput.getSkip() > 0){
                        LOG.warn(robotOutput.getSkip()+" Tasks didn't complete");
                        return true;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
