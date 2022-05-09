package org.camunda.rpa.client.data.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.camunda.rpa.client.data.constants.RobotResponseType;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sneha Suresh
 * 
 */
@Entity
@Table(name = "rpa_client_handler_config")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RobotHandlerConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "handler_id")
	private Integer handlerId;

	@Column(name = "robot_name", length = 250)
	private String robotName;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	// topic name of the External Service Task
	@Column(name = "topic_name", nullable = false)
	private String topicName;

	// Task file name eg: task.robot / task.py
	@Column(name = "task_file_name")
	private String taskFileName;

	// our business logic may need variables
	@Column(name = "variable_names")
	private String variableNames;

	// only filter for External Tasks with this process definition key
	@Column(name = "process_definition_key", nullable = false)
	private String processDefinitionKey;

	@Enumerated(EnumType.STRING)
	@Column(name = "response_type", nullable = false, length = 20)
	private RobotResponseType responseType;

	@Transient
	private String workingDirName;

	@Column(name = "description", length = 500)
	private String description;

	@Column(name = "created_date")
	@CreationTimestamp
	private LocalDateTime createdDate;

	@Column(name = "workspace_id")
	private String workspaceId;

	@Column(name = "process_id")
	private String processId;
}
