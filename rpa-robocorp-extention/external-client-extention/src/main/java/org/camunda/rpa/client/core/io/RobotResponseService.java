package org.camunda.rpa.client.core.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.rpa.client.config.formsflow.FormsflowProperties;
import org.camunda.rpa.client.config.formsflow.auth.FormioRequestProvider;
import org.camunda.rpa.client.core.io.ro.Base64FileResponse;
import org.camunda.rpa.client.core.io.ro.FormElement;
import org.camunda.rpa.client.core.pipe.RobotDirectoryScanner;
import java.io.InputStream;
import org.camunda.rpa.client.data.constants.ExternalClientConstants;
import org.camunda.rpa.client.data.entity.RobotHandlerAudit;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.rpa.client.exception.RobotClientRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This class is responsible for delivering output data from robots The response
 * can be a file / multi files / string / array. author: Shibin Thomas
 */
@Service
public class RobotResponseService {

	private static final Logger LOG = LoggerFactory.getLogger(RobotResponseService.class);

	@Autowired
	private RobotDirectoryScanner robotDirectoryScanner;
	@Autowired
	private FormioRequestProvider formioRequestProvider;

	private final WebClient webClient;

	private final FormsflowProperties formsflowProperties;

	@Value("${robot.cloud.api-key}")
	private String apiKey;

	private RobotResponseService(WebClient webClient, FormsflowProperties formsflowProperties) {
		this.webClient = webClient;
		this.formsflowProperties = formsflowProperties;
	}

	/**
	 * Read a single file from output directory and return it as response.
	 * 
	 * @param robotName
	 * @param outputDirName
	 * @return
	 */
	public VariableMap readRCCRobotFileResponse(String robotName, String outputDirName, String formUrl) {

		LOG.debug("Reading response from output directory for robot = " + robotName);

		VariableMap variables = Variables.createVariables();

		File outputDir = robotDirectoryScanner.getRobotFinalDirectory(robotName, outputDirName);
		File[] outputFileArray = outputDir.listFiles();

		if ((outputFileArray != null ? outputFileArray.length : 0) > 0) {
			File output = outputFileArray[0];

			try {
				// Need to check response type is base64 or not
				if (true) {
					String mimeType = URLConnection.guessContentTypeFromName(output.getName());
					LOG.debug("mimetype = " + mimeType);
					byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(output));
					String encodedData = new String(encoded, StandardCharsets.US_ASCII);

					if (formsflowProperties.isFormioEnabled()) {
						patchResponse(formUrl, output.getName(), encodedData, output.length());
					}
				}
				FileValue typedFileValue = Variables.fileValue(output.getName()).file(output)
						.mimeType("application/pdf").encoding("UTF-8").create();
				variables.put(ExternalClientConstants.FILE_RESPONSE_VAR, typedFileValue);
			} catch (Exception ex) {
				LOG.error("Error : Reading response file");
				ex.printStackTrace();
				throw new RobotClientRuntimeException("Response file is not found",
						HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
		}
		return variables;
	}

	private ResponseEntity<String> patchResponse(String formUrl, String fileName, String encodedData, long length)
			throws JsonProcessingException {
		Base64FileResponse[] base64FileResponses = new Base64FileResponse[] {
				new Base64FileResponse(fileName, encodedData, length) };

		List<FormElement> elements = new ArrayList<>();

		elements.add(new FormElement(ExternalClientConstants.FILE_RESPONSE_VAR, base64FileResponses));

		WebClient.RequestHeadersSpec requestHeadersSpec = webClient.patch()
				.uri(Objects.requireNonNull(getDecoratedServerUrl(formUrl)))
				.bodyValue(new ObjectMapper().writeValueAsString(elements));

		formioRequestProvider.intercept(requestHeadersSpec);

		Mono<ResponseEntity<String>> entityMono = requestHeadersSpec.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).retrieve()
				.onStatus(HttpStatus::is4xxClientError,
						response -> Mono.error(new RobotClientRuntimeException(response.toString(), 404)))
				.toEntity(String.class);

		return entityMono.block();
	}

	/**
	 * This function will create temporary output file generated by the robot
	 * 
	 * @param robotName
	 * @param outputDirName
	 * @return
	 */
	public boolean createOutputDirectory(String robotName, String outputDirName) {
		File outputDir = robotDirectoryScanner.getRobotLocationMap().get(robotName);
		return robotDirectoryScanner.createOutputDirectory(outputDir, outputDirName);
	}

	/**
	 * This function will clear temporary output file generated by the robot
	 * 
	 * @param robotName
	 * @param outputDirName
	 * @return
	 */
	public boolean clearAndRemoveOutputDirectory(String robotName, String outputDirName) {
		File outputDir = robotDirectoryScanner.getRobotWorkingDirectory(robotName, outputDirName);
		return robotDirectoryScanner.deleteDirectory(outputDir);
	}

	private String getDecoratedServerUrl(String url) {
		if (StringUtils.contains(url, "/form/")) {
			return formsflowProperties.getFormio().getUrl() + "/form/" + StringUtils.substringAfter(url, "/form/");
		}
		return null;
	}

	/**
	 * Read a file from outputUrl and return it as response.
	 * 
	 * @param audit
	 * @param formUrl
	 * @return
	 */
	public VariableMap readRobocorpCloudResponseFile(RobotHandlerAudit audit, String formUrl)
			throws JSONException, MalformedURLException, IOException {
		VariableMap variables = Variables.createVariables();
		String fileName = "data.pdf";
		try {
			ResponseEntity<String> finalResponse = webClient.method(HttpMethod.GET).uri(audit.getResponseUri())
					.header("Authorization", getApiKey()).accept(MediaType.APPLICATION_JSON)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(null).retrieve()
					.toEntity(String.class).block();

			LOG.error(finalResponse.getBody());
			JSONObject jsonObject = new JSONObject(finalResponse.getBody());
			InputStream in = new URL(jsonObject.getString("url")).openStream();
			byte[] encoded = Base64.encodeBase64(in.readAllBytes());
			String encodedData = new String(encoded, StandardCharsets.US_ASCII);

			patchResponse(formUrl, fileName, encodedData, encodedData.length());

			FileValue typedFileValue = Variables.fileValue(fileName).file(encoded).mimeType("application/pdf")
					.encoding("UTF-8").create();
			variables.put(ExternalClientConstants.FILE_RESPONSE_VAR, typedFileValue);
		} catch (Exception ex) {
			LOG.error("Error : Reading response file");
			ex.printStackTrace();
			throw new RobotClientRuntimeException("Response file is not found",
					HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		return variables;
	}

	private String getApiKey() {
		return this.apiKey;
	}

}
