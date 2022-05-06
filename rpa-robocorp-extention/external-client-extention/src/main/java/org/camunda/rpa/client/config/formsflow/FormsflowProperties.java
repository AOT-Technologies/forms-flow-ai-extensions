package org.camunda.rpa.client.config.formsflow;

import lombok.Data;
import org.camunda.rpa.client.config.formsflow.FormioProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "formsflow")
public class FormsflowProperties {

    private boolean formioEnabled;
    private FormioProperties formio;
}
