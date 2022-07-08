package org.camunda.rpa.client.core.io.ro;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sumathi Thirumani
 * @author Shibin Thomas
 */
@Data
@NoArgsConstructor
public class FormElement {
    private String op;
    private String path;
    private Object value;

    public FormElement(String elementId, Object value) {
        this.op = "replace";
        this.path = "/data/" + elementId;
        this.value = value;
    }
}
