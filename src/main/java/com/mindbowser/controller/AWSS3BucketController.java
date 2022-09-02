package com.mindbowser.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mindbowser.constant.MessageConstant;
import com.mindbowser.constant.RolesConstant;
import com.mindbowser.exception.CustomException;
import com.mindbowser.model.ResponseModel;
import com.mindbowser.service.AWSS3BucketService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/aws/s3/bucket")
@Api(value = "/aws/s3/bucket")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal Server Error"),
		@ApiResponse(code = 404, message = "Service not found"), @ApiResponse(code = 200, message = "Success") })

public class AWSS3BucketController {

	@Autowired
	private AWSS3BucketService service;

	@Autowired
	private Environment environment;

	/**
	 * Upload file AWS S3 bucket.
	 * 
	 * @author Mindbowser | suraj.jaiswal@mindbowser.com
	 * @throws CustomException
	 */
	@PostMapping("/file/upload")
	@PreAuthorize(RolesConstant.ADMIN)
	@ApiOperation(value = "Upload file AWS S3 bucket.", notes = "This API used to  upload file AWS S3 bucket.")
	public ResponseEntity<ResponseModel> uploadFile(@RequestParam("file") MultipartFile file) throws CustomException {
		log.info("------------ Upload file [web service] --------------");
		ResponseModel response = ResponseModel.getInstance();
		service.uploadFile(file);
		response.setMessage(environment.getProperty(MessageConstant.SUCCESSFULLY_FILE_UPLOADED));
		response.setStatusCode(HttpStatus.SC_OK);

		return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);
	}

}