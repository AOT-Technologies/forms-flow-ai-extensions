package org.camunda.rpa.client.controller;

import org.camunda.rpa.client.core.RobotDataManager;
import org.camunda.rpa.client.data.entity.RobotHandlerConfig;
import org.camunda.rpa.client.exception.RobotClientDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class make use of robot handler configuration table to do some
 * operations
 * 
 * @author Shibin Thomas
 */
@RestController
@RequestMapping("/api/robot/")
public class CamundaRPAClientController {

	@Autowired
	private RobotDataManager robotDataManager;

	@GetMapping(value = "healthcheck")
	public String healthCheck() {
		return "Welcome to Camunda RPA Client";
	}

	/**
	 * Get list of robot configuration data
	 * 
	 * @return
	 */
	@GetMapping(value = "config/list", produces = "application/json")
	public ResponseEntity<List<RobotHandlerConfig>> listConfigData() {

		List<RobotHandlerConfig> response = null;
		try {
			response = robotDataManager.listRobotConfigData();
			return ResponseEntity.ok(response);
		} catch (RobotClientDataException ex) {
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Update robot configuration data
	 * 
	 * @return
	 */
	@PutMapping(value = "config/update", produces = "application/json", consumes = "application/json")
	public ResponseEntity<RobotHandlerConfig> updateConfigData(@RequestBody RobotHandlerConfig config) {
		RobotHandlerConfig response = null;
		try {
			response = robotDataManager.updateRobotHandlerConfigData(config);
			return ResponseEntity.ok(response);
		} catch (RobotClientDataException ex) {
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Save robot configuration data
	 * 
	 * @return
	 */
	@PostMapping(value = "config/save", produces = "application/json", consumes = "application/json")
	public ResponseEntity<RobotHandlerConfig> saveConfigData(@RequestBody RobotHandlerConfig config) {
		RobotHandlerConfig response = null;
		try {
			response = robotDataManager.saveRobotHandlerConfigData(config);
			return ResponseEntity.ok(response);
		} catch (RobotClientDataException ex) {
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
