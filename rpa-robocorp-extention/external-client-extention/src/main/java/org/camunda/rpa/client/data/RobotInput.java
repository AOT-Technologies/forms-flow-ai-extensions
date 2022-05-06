package org.camunda.rpa.client.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RobotInput {

    private String field;
    private Object value;
}
