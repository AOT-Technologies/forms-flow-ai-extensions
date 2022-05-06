package org.camunda.rpa.client.data.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import org.camunda.rpa.client.data.constants.RobotStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sneha Suresh
 *
 */
@Entity
@Table(name = "rpa_client_audit_handler")
@NoArgsConstructor
@Data
public class RobotHandlerAudit implements IEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private RobotStatus status;

	@Column(name = "task_id", nullable = false, length = 50)
	private String taskId;

	@Column(name = "details")
	private String details;

	@OneToOne(optional=false)
	@JoinColumn(name = "handler_id", nullable=false, updatable=false)
	private RobotHandlerConfig handlerConfig;

	@Column(name = "created_date")
	@CreationTimestamp
	private LocalDateTime createdDate;

	@Column(name = "modified_date")
	@UpdateTimestamp
	private LocalDateTime modifiedDate;
}
