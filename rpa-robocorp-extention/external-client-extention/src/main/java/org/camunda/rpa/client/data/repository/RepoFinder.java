package org.camunda.rpa.client.data.repository;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class RepoFinder {

    @Autowired
    private RobotHandlerAuditRepository auditRepository;

    @Autowired
    private RobotHandlerConfigRepository configRepository;
}
