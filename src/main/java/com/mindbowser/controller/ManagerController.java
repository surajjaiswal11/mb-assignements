package com.mindbowser.controller;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindbowser.constant.MessageConstant;
import com.mindbowser.exception.CustomException;
import com.mindbowser.mail.MailerHelper;
import com.mindbowser.model.ManagerModel;
import com.mindbowser.model.ResponseModel;
import com.mindbowser.service.ManagerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Api(value = "/api/auth")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal Server Error"),
		@ApiResponse(code = 404, message = "Service not found"), @ApiResponse(code = 200, message = "Success") })

public class ManagerController {

	@Autowired
	private ManagerService managerService;

	@Autowired
	private Environment environment;

	@Autowired
	private MailerHelper mailHelper;

	/**
	 * Manager signup API.
	 * 
	 * @author Mindbowser | suraj.jaiswal@mindbowser.com
	 * @throws CustomException
	 */
	@PermitAll
	@PostMapping("/signup")
	@ApiOperation(value = "Sign up Manager.", notes = "This API used to sign up new user.")
	public ResponseEntity<ResponseModel> registerUser(@Valid @RequestBody ManagerModel model) throws CustomException {
		log.info("------------ In signUpDummy [web service] --------------");
		ResponseModel response = ResponseModel.getInstance();

		managerService.signUpManager(model);
		mailHelper.sendEmail(() -> mailHelper.sendConfirmationEmail(model));

		response.setMessage(environment.getProperty(MessageConstant.MSG_SINGUP_SUCCESS));
		response.setStatusCode(HttpStatus.SC_OK);

		return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);

	}

	/**
	 * Login API.
	 * 
	 * @author Mindbowser | suraj.jaiswal@mindbowser.com
	 * @throws CustomException
	 */
	@PermitAll
	@PostMapping("/signin")
	@ApiOperation(value = "Login user.", notes = "This API used to login.")
	public ResponseEntity<ResponseModel> login(@RequestBody ManagerModel model) throws CustomException {
		log.info("------------ In login [web service] --------------");
		ResponseModel response = ResponseModel.getInstance();
		response.setData(managerService.login(model));
		response.setMessage(environment.getProperty(MessageConstant.MSG_LOGIN_SUCCESS));
		response.setStatusCode(HttpStatus.SC_OK);
		return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);

	}

}
