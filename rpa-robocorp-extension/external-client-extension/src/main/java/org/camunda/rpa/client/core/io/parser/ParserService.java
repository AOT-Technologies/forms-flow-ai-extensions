package org.camunda.rpa.client.core.io.parser;

import org.camunda.rpa.client.core.io.ro.RobotOutput;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;

public interface ParserService {

    RobotOutput parse(File file) throws IOException, JDOMException;
}
