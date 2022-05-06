package org.camunda.rpa.client.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RobocorpCloudData {

	private String processRunID;
	private String robotRunId;
	private String artifactId;

}
