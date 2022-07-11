package org.camunda.rpa.client.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.camunda.bpm.client.task.ExternalTask;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDataInput {

    private ExternalTask externalTask;

    private List<String> variableNames;

    private Map<String, Object> additionalVariables;
}
