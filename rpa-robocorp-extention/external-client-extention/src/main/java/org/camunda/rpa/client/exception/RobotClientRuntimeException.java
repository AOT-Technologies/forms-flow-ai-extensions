package org.camunda.rpa.client.exception;

import lombok.Data;

/**
 * To throw during the robot runtime
 */
@Data
public class RobotClientRuntimeException extends RuntimeException {
	private Integer status;

	public RobotClientRuntimeException(String message, Integer status) {
		super(message);
		this.status = status;
	}
}
