package org.camunda.rpa.client.core.io.ro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RobotOutputMessage {

    private String level;
    private String message;

    @Override
    public String toString() {
        return "{" +
                "level='" + level + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
