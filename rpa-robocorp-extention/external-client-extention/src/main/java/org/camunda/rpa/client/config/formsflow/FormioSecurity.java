package org.camunda.rpa.client.config.formsflow;

import lombok.Data;

@Data
public class FormioSecurity {

    private String username;
    private String password;
    private String accessTokenUri;
}
