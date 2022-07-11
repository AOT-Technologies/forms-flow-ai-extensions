package org.camunda.rpa.client.core.io.ro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RobotOutput implements IRO{

    private Integer pass;
    private Integer fail;
    private Integer skip;

    private List<RobotOutputMessage> messages;

    @Override
    public String toString() {
        return "RobotOutput{" +
                "pass=" + pass +
                ", fail=" + fail +
                ", skip=" + skip +
                ", messages=" + messages +
                '}';
    }
}
