package org.camunda.rpa.client.config.formsflow;

import lombok.Data;

@Data
public class FormioProperties {

    private boolean enabled;
    private String url;
    private FormioSecurity security;
}
