package org.camunda.rpa.client.handlers;

import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class TaskHandlerManager implements ExternalTaskHandler,TaskHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TaskHandlerManager.class);

    private static final Map<String, String> activeTaskIdMap = new HashMap<>();

    /**
     * Not a function to override
     * @param externalTask
     * @param externalTaskService
     */
    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String taskId = externalTask.getId();
        String topicName = externalTask.getTopicName();

        // Allowing multiple robots to run in parallel in the starting will create installation chaos
        if(activeTaskIdMap.containsKey(topicName)){
            LOG.info("Active tasks running for topicName :"+topicName);
            return;
        } else {
            LOG.debug("Locking topicName = "+topicName+" for installation");
            activeTaskIdMap.put(topicName, taskId);
        }

        Thread taskThread = new Thread(() -> {
            try {
                LOG.debug("START: Handler will be invoked to run robots");
                this.subscribe(externalTask, externalTaskService);
                LOG.debug("END: Handler will be invoked to run robots");
            } finally {
                activeTaskIdMap.remove(topicName);
                LOG.info("Removed the taskId = "+taskId+", topicName :"+topicName);
            }
        });
        taskThread.start();
    }

    /**
     * Not a function to override
     * @param externalTask
     * @param externalTaskService
     */
    /*@Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String taskId = externalTask.getId();
        String topicName = externalTask.getTopicName();

        // Allowing multiple robots to run in parallel in the starting will create installation chaos
        int limit = (installLock.containsKey(topicName) && !installLock.get(topicName)) ? 2 : 0;
        if(activeTaskIdMap.containsKey(topicName)){
            List<String> taskIds = activeTaskIdMap.get(topicName);
            LOG.info("Active tasks running for topicName :"+topicName+" is "+taskIds.size());
            if (taskIds.contains(taskId) || taskIds.size() > limit){
                return;
            }
        } else {
            LOG.debug("Locking topicName = "+topicName+" for installation");
            installLock.put(topicName, true);
            activeTaskIdMap.put(topicName, new ArrayList<>());
        }

        List<String> taskIds = activeTaskIdMap.get(topicName);
        if (!taskIds.contains(taskId) && taskIds.size() <= limit) {
            LOG.info("New tak id added for processing " + taskId+" for topic - "+topicName);
            taskIds.add(taskId);
            Thread taskThread = new Thread(() -> {
                try {
                    LOG.debug("START: Handler will be invoked to run robots");
                    this.subscribe(externalTask, externalTaskService);
                    LOG.debug("END: Handler will be invoked to run robots");
                } finally {
                    taskIds.remove(taskId);
                    if(installLock.get(topicName)) {
                        installLock.put(topicName, false);
                    }
                    LOG.info("Removed the taskId = "+taskId+", active tasks running for topicName :"+topicName+" is "+taskIds.size());
                }
            });
            taskThread.start();
        }
    }*/
}
