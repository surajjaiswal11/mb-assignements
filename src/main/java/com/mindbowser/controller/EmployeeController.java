package com.mindbowser.controller;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindbowser.constant.MessageConstant;
import com.mindbowser.constant.RolesConstant;
import com.mindbowser.exception.CustomException;
import com.mindbowser.model.EmployeeModel;
import com.mindbowser.model.ResponseModel;
import com.mindbowser.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employee")
@Api(value = "/employee")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal Server Error"),
		@ApiResponse(code = 404, message = "Service not found"), @ApiResponse(code = 200, message = "Success") })
public class EmployeeController {

	@Autowired
	public EmployeeService employeeService;

	@Autowired
	private Environment environment;

	/**
	 * Save Employee Information.
	 * 
	 * @author Mindbowser | suraj.jaiswal@mindbowser.com
	 * @throws CustomException
	 */
	@PostMapping("/save")
	@PreAuthorize(RolesConstant.ADMIN)
	@ApiOperation(value = "Save Employee Information.", notes = "This API used to save Employee.")
	public ResponseEntity<ResponseModel> saveEmployee(@Valid @RequestBody EmployeeModel employeeModel)
			throws CustomException {
		log.info("------------ Save Employee [web service] --------------");
		ResponseModel response = ResponseModel.getInstance();
		employeeService.saveEmployee(employeeModel);
		response.setData(null);
		response.setMessage(environment.getProperty(MessageConstant.SUCCESSFULL_SAVE_EMPLOYEE));
		response.setStatusCode(HttpStatus.SC_OK);
		return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);

	}

	/**
	 * update or delete Employee Information.
	 * 
	 * @author Mindbowser | suraj.jaiswal@mindbowser.com
	 * @throws CustomException
	 */
	@PostMapping("/update-delete")
	@PreAuthorize(RolesConstant.ADMIN)
	@ApiOperation(value = "update or delete Employee Information.", notes = "This API used to Update or delete Employee.")
	public ResponseEntity<ResponseModel> updateOrDeleteEmployee(@Valid @RequestBody EmployeeModel employeeModel)
			throws CustomException {
		log.info("------------ Update Or Delete Employee [web service] --------------");
		ResponseModel response = ResponseModel.getInstance();
		employeeService.updateOrDeleteEmployee(employeeModel);
		response.setData(null);

		if (employeeModel.isDeleted()) {
			response.setData(null);
			response.setMessage(environment.getProperty(MessageConstant.MSG_USER_DELETE_SUCCESS));
		} else {
			response.setMessage(environment.getProperty(MessageConstant.MSG_USER_UPDATE_SUCCESS));
		}
		response.setStatusCode(HttpStatus.SC_OK);
		return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);

	}

	/**
	 * GET All Employee
	 * 
	 * @author Mindbowser | suraj.jaiswal@mindbowser.com
	 * @throws CustomException
	 */
	@GetMapping("/get")
	@PreAuthorize(RolesConstant.ADMIN)
	@ApiOperation(value = "Get All Employee.", notes = "This API used to Get All employee.")
	public ResponseEntity<ResponseModel> getAllEmployee(@RequestParam(required = true) Integer currentPage,
			@RequestParam(required = true) Integer totalPerPage) throws CustomException {
		log.info("------------ GET All Employee [web service] --------------");
		ResponseModel response = ResponseModel.getInstance();
		response.setData(employeeService.getEmployeesList(currentPage, totalPerPage));
		response.setMessage(environment.getProperty(MessageConstant.SUCCESSFULLY_GET_EMPLOYEE));
		response.setStatusCode(HttpStatus.SC_OK);
		return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);

	}

}
