package org.camunda.rpa.client.data.repository;

import org.camunda.rpa.client.data.entity.RobotHandlerConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sneha Suresh
 * 
 */
@Repository
public interface RobotHandlerConfigRepository extends CrudRepository<RobotHandlerConfig, Integer> {

}
