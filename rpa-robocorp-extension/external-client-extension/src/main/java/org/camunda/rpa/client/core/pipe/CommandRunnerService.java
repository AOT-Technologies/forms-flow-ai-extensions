package org.camunda.rpa.client.core.pipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;

/**
 * This class is responsible for managing service layer of command runner.
 * 
 * @author Shibin Thomas
 */
@Service
public class CommandRunnerService {

    private static final Logger LOG = LoggerFactory.getLogger(CommandRunnerService.class);

    public void run(String[] commands) {

        LOG.debug("running commands using command runner service");

        String os = System.getProperty("os.name");
        String[] command = null;

        if("Linux".equals(os)){
            command = new String[]{ "/bin/bash", };
        } else {
            command = new String[]{ "cmd", };
        }
        Process p = null;
        PrintWriter stdin = null;
        try {
            p = Runtime.getRuntime().exec(command);
            new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
            new Thread(new SyncPipe(p.getInputStream(), System.out)).start();

            stdin = new PrintWriter(p.getOutputStream());

            for(String cmd : commands){
                stdin.println(cmd);
            }

            stdin.close();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(stdin != null){
                stdin.close();
            }
            if(p != null)
                p.destroy();
        }
    }
}
