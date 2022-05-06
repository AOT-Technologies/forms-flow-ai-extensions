package org.camunda.rpa.client.handlers;

import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;

public interface TaskHandler {
    void subscribe(ExternalTask externalTask, ExternalTaskService externalTaskService);
}
