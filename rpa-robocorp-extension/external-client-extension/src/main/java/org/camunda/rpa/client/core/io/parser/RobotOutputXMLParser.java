package org.camunda.rpa.client.core.io.parser;

import org.camunda.rpa.client.core.io.ro.RobotOutput;
import org.camunda.rpa.client.core.io.ro.RobotOutputMessage;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * XML parser to parse robot output.xml file
 * output.xml will be produced once the robot completes it's execution
 * author : Shibin Thomas
 */
@Component
public class RobotOutputXMLParser implements ParserService{

    private static final Logger LOG = LoggerFactory.getLogger(RobotOutputXMLParser.class);

    /**
     * To parse output.xml file produced by the robot
     * @param file
     * @return
     */
    @Override
    public RobotOutput parse(File file) {

        LOG.debug("START : Parsing output file to find the status");

        RobotOutput robotOutput = null;
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(file);

            LOG.debug("Root element :" + document.getRootElement().getName());

            Element robotElement = document.getRootElement();

            Element statistics = robotElement.getChild("statistics");
            Element total = statistics.getChild("total");
            Element stat = total.getChild("stat");
            String pass = stat.getAttributeValue("pass");
            String fail = stat.getAttributeValue("fail");
            String skip = stat.getAttributeValue("skip");

            Element errors = robotElement.getChild("errors");
            List<Element> messages = errors.getChildren("msg");
            List<RobotOutputMessage> messageList = new ArrayList<>();
            if(messages != null){
                for(Element msg : messages){
                    String level = msg.getAttributeValue("level");
                    String message = msg.getText();
                    RobotOutputMessage robotOutputMessage = new RobotOutputMessage(level, message);
                    messageList.add(robotOutputMessage);
                }
            }
            robotOutput = new RobotOutput(Integer.valueOf(pass), Integer.valueOf(fail), Integer.valueOf(skip), messageList);
            if(robotOutput.getFail() > 0){
                List<Element> suites = robotElement.getChildren("suite");
                List<String> errorMessages = findErrorDetails(suites);
                if(errorMessages.size() > 0) {
                    for(String message : errorMessages){
                        RobotOutputMessage robotOutputMessage = new RobotOutputMessage("FAIL", message);
                        messageList.add(robotOutputMessage);
                    }
                }
            }
        } catch (JDOMException | IOException ex){
            ex.printStackTrace();
        }
        LOG.debug("END : Parsing output file to find the status");
        return robotOutput;
    }

    private List<String> findErrorDetails(List<Element> suites){

        List<String> errorMessages = new ArrayList<>();
        for(Element suite : suites) {
            List<Element> tests = suite.getChildren("test");
            for (Element test : tests) {
                Element status = test.getChild("status");
                if (status.getAttributeValue("status").equals("FAIL")) {
                    errorMessages.add(status.getText());
                }
            }
        }
        return errorMessages;
    }
}
