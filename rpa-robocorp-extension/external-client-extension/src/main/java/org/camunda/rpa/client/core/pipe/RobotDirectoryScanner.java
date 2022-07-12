package org.camunda.rpa.client.core.pipe;

import org.apache.commons.io.FileUtils;
import org.camunda.rpa.client.config.RobotHandlerProperties;
import org.camunda.rpa.client.data.constants.ExternalClientConstants;
import org.camunda.rpa.client.exception.RobotClientRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class will manage the creation / deletion of the robot directory and it's discovery
 * author : Shibin Thomas
 */
@Component
public class RobotDirectoryScanner {

    private static final Logger LOG = LoggerFactory.getLogger(RobotDirectoryScanner.class);

    @Autowired
    private RobotHandlerProperties handlerProperties;

    private  Map<String, File> robotLocationMap;

    /**
     *
     * @throws FileNotFoundException
     */
    @PostConstruct
    public void scanRobotDirectory() throws FileNotFoundException {
        robotLocationMap = new HashMap<>();
        String baseDir = handlerProperties.getBaseDir();
        File robotDir = null;
        if(baseDir == null || baseDir.length() == 0) {
            File file = ResourceUtils.getFile("classpath:robots");
            if (file.isDirectory()) {
                robotDir = new File(file.getAbsolutePath());
            }
        } else {
            robotDir = new File(baseDir);
            if(!robotDir.exists()){
                throw new FileNotFoundException("No directory exists for the robot base directory");
            }
        }
        if(robotDir != null) {
            searchRobotConfigFileAndMark(robotDir.listFiles());
        }
        Collection<String> coll = robotLocationMap.keySet();
        List<String> list;
        if (coll instanceof List)
            list = (List<String>)coll;
        else
            list = new ArrayList<>(coll);
        HoloTreeDictionary.setHoloTreeInfo(list);
    }

    /**
     * Search the list of directories and identify if it has a robot configuration file
     * @param dirs
     */
    private void searchRobotConfigFileAndMark(File[] dirs){

        if(dirs == null) return;

        for(File dir : dirs){
            if(dir.isDirectory()) {
                List<String> allFiles = Arrays.asList(Objects.requireNonNull(dir.list()));
                if (allFiles.size() > 0 && allFiles.contains(ExternalClientConstants.ROBOT_CONFIG_FILE)) {
                    String robotName = dir.getName();
                    robotLocationMap.put(robotName, dir);
                } else {
                    searchRobotConfigFileAndMark(dir.listFiles());
                }
            }
        }
    }

    /**
     * Fetches the final directory to get the response data
     * @param robotName
     * @param workingDirName
     * @return
     */
    public File getRobotFinalDirectory(String robotName, String workingDirName) throws RobotClientRuntimeException {

        File robotDir = getRobotLocationMap().get(robotName);
        if(robotDir == null){
            throw new RobotClientRuntimeException("No appropriate robot directories identified", 500);
        }
        return getRobotFinalDirectory(robotDir, workingDirName);
    }

    /**
     * Get the robot output directory which has the robot response
     * @param robotName
     * @param outputDirName
     * @return
     * @throws RobotClientRuntimeException
     */
    public File getRobotWorkingDirectory(String robotName, String outputDirName) throws RobotClientRuntimeException{

        File robotDir = getRobotLocationMap().get(robotName);
        if(robotDir == null){
            throw new RobotClientRuntimeException("No appropriate robot directories identified", 500);
        }
        return getRobotWorkingDirectory(robotDir, outputDirName);
    }


    /**
     * Get the robot output directory which has the robot response
     * Eg structure : *** 178278234 *******
     *  ***** final ******************** runtime *******
     * @param robotDir
     * @param workingDirName
     * @return
     */
    private File getRobotWorkingDirectory(File robotDir, String workingDirName){

        File outputDir = null;

        for(File file : Objects.requireNonNull(robotDir.listFiles())){
            if(file.isDirectory() && file.getName().equals(ExternalClientConstants.OUTPUT_DIR)){
                List<File> outputFileList = Arrays.asList(Objects.requireNonNull(file.listFiles()));
                List<File> matchingFileList = outputFileList.stream().filter(f -> (f.isDirectory() && f.getName().equals(workingDirName))).collect(Collectors.toList());
                if(matchingFileList.size() > 0) {
                    outputDir = matchingFileList.get(0);
                }
                break;
            }
        }
        return outputDir;
    }

    /**
     * Fetches the final directory to get the response data
     * @param robotDir
     * @param workingDirName
     * @return
     */
    private File getRobotFinalDirectory(File robotDir, String workingDirName){

        File finalDir = null;

        for(File file : Objects.requireNonNull(robotDir.listFiles())){
            if(file.isDirectory() && file.getName().equals(ExternalClientConstants.OUTPUT_DIR)){
                List<File> outputFileList = Arrays.asList(Objects.requireNonNull(file.listFiles()));
                List<File> matchingFileList = outputFileList.stream().filter(f -> (f.isDirectory() && f.getName().equals(workingDirName))).collect(Collectors.toList());
                if(matchingFileList.size() > 0) {
                    for (File finalFile : Objects.requireNonNull(matchingFileList.get(0).listFiles())) {
                        if (finalFile.getName().equals(ExternalClientConstants.RESPONSE_DIR)) {
                            finalDir = finalFile;
                            break;
                        }
                    }
                }
                break;
            }
        }
        return finalDir;
    }

    /**
     * Create the output directory for robots.
     * @param outputDir
     * @param outputDirName
     * @return
     */
    public boolean createOutputDirectory(File outputDir, String outputDirName){

        LOG.debug("Building output directories for the robot response");

        boolean creationStatus = false;

        try {

            File rootDir = new File(outputDir.getAbsolutePath()
                    + ExternalClientConstants.BACK_SLASH
                    + ExternalClientConstants.OUTPUT_DIR
                    + ExternalClientConstants.BACK_SLASH
                    + outputDirName);
            if (!rootDir.exists()) creationStatus = rootDir.mkdirs();

            File runtimeFile = new File(rootDir.getAbsolutePath()
                    + ExternalClientConstants.BACK_SLASH
                    + ExternalClientConstants.RUNTIME_DIR);
            File finalFile = new File(rootDir.getAbsolutePath()
                    + ExternalClientConstants.BACK_SLASH
                    + ExternalClientConstants.RESPONSE_DIR);
            if (!runtimeFile.exists()) creationStatus = creationStatus && runtimeFile.mkdirs();
            if (!finalFile.exists()) creationStatus = creationStatus && finalFile.mkdirs();
        } catch (Exception ex){
            ex.printStackTrace();
            throw new RobotClientRuntimeException("Exception during output directory creation", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return creationStatus;
    }

    /**
     * Deletes a directory
     * @param outputDir
     * @return
     */
    public boolean deleteDirectory(File outputDir){
        if(outputDir == null) return false;
        return FileUtils.deleteQuietly(outputDir);
    }

    /**
     * @return the map of robots and it's location
     */
    public  Map<String, File> getRobotLocationMap() {
        return robotLocationMap;
    }
}
