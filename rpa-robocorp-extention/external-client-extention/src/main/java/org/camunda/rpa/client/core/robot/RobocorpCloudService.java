package org.camunda.rpa.client.core.robot;

import org.camunda.rpa.client.data.entity.RobotHandlerAudit;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.beans.factory.annotation.Value;
import org.camunda.rpa.client.core.pipe.RobotDirectoryScanner;
import org.camunda.rpa.client.data.RobotInput;
import org.camunda.rpa.client.data.entity.RobotHandlerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import reactor.core.publisher.Mono;
import org.springframework.http.*;

/**
 * This class will enable running robots in Robocorp cloud author : Shibin
 * Thomas
 */
@Service
public class RobocorpCloudService implements IRobotService {

	private static final Logger LOG = LoggerFactory.getLogger(RobocorpCloudService.class);

	@Autowired
	private WebClient webClient;

	@Autowired
	private RobotDirectoryScanner robotDirectoryScanner;

	@Value("${robot.cloud.api-key}")
	private String apiKey;

	@Value("${robot.cloud.api-url}")
	private String processApi;

	@Override
	public boolean runRobot(List<RobotInput> robotInputs, RobotHandlerAudit audit) {
		try {
			return invokeRobot(robotInputs, audit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean invokeRobot(List<RobotInput> robotInputs, RobotHandlerAudit audit) throws JSONException {
		RobotHandlerConfig config = audit.getHandlerConfig();
		String response = startRobotProcess(robotInputs, config);
		JSONObject jsonObject = new JSONObject(response);
		String robotRunId = jsonObject.getString("id");
		String workItemId = new JSONArray(jsonObject.getString("workItemIds")).getString(0);

		boolean success = false;

		String output;
		for (; true;) {

			try {
				output = getWorkItems(robotRunId, config);
				Thread.sleep(5000);
				if (output.contains("FAILED")) {
					break;
				}
				success = isRobotCompleted(output);
				if (success) {
					setResponseUri(output, workItemId, config, robotInputs, audit);
					break;
				}
			} catch (Exception e) {
				break;
			}
		}
		return success;
	}

	private String startRobotProcess(List<RobotInput> robotInputs, RobotHandlerConfig config) throws JSONException {
		String formattedInput = buildInput(robotInputs).toString();
		String uri = getRobotProcessUrl(config) + "/runs";
		ResponseEntity<String> response = webClient.method(HttpMethod.POST).uri(uri)
				.header("Authorization", getApiKey()).accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(formattedInput), String.class).retrieve().toEntity(String.class).block();

		LOG.error(response.getBody());
		return response.getBody();
	}

	private JSONObject buildInput(List<RobotInput> robotInputs) throws JSONException {
		JSONObject formattedInput = new JSONObject();
		for (int i = 0; i < robotInputs.size(); i++) {
			formattedInput.put(robotInputs.get(i).getField(), robotInputs.get(i).getValue());
		}
		return formattedInput;
	}

	private String getWorkItems(String robotRunId, RobotHandlerConfig config) {
		String uri = getRobotProcessUrl(config) + "/runs/" + robotRunId + "/work-items";
		ResponseEntity<String> response = webClient.method(HttpMethod.GET).uri(uri).header("Authorization", getApiKey())
				.accept(MediaType.APPLICATION_JSON).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.empty(), String.class).retrieve().toEntity(String.class).block();

		return response.getBody();
	}

	private void setResponseUri(String output, String workItemId, RobotHandlerConfig config,
			List<RobotInput> robotInputs, RobotHandlerAudit audit) throws JSONException, IOException {
		String jsonData = new JSONObject(output).getString("data");
		String data = jsonData != null ? new JSONArray(jsonData).getString(0) : null;
		String files = data != null ? new JSONObject(data).getString("files") : null;
		String fileIds = files != null ? new JSONArray(files).getString(0) : null;
		if (fileIds != null) {
			JSONObject jsonObject = new JSONObject(fileIds);
			String fileId = jsonObject.getString("id");
			String fileName = jsonObject.getString("name");
			String uri = getRobotProcessUrl(config) + "/work-items/" + workItemId + "/files/" + fileId + "/download";
			audit.setResponseUri(uri);
		}
	}

	private boolean isRobotCompleted(String result) {
		return result.contains("COMPLETED");
	}

	private String getApiKey() {
		return this.apiKey;
	}

	private String getRobotProcessUrl(RobotHandlerConfig config) {
		return this.processApi + "/" + config.getWorkspaceId() + "/processes/" + config.getProcessId();
	}
}
