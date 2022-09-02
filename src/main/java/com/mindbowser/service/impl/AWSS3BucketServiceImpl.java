package com.mindbowser.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.mindbowser.constant.ExceptionConstant;
import com.mindbowser.exception.CustomException;
import com.mindbowser.service.AWSS3BucketService;

@Service
public class AWSS3BucketServiceImpl implements AWSS3BucketService {

	@Autowired
	private AmazonS3 amazonS3Client;

	@Value("${application.bucket.name}")
	private String bucketName;

	@Autowired
	private Environment environment;

	/**
	 * Upload file into AWS S3
	 *
	 * @param file
	 * 
	 * @throws CustomException
	 */

	@Override
	public void uploadFile(MultipartFile file) throws CustomException {

		if (!file.isEmpty()) {
			try {
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentLength(file.getSize());
				amazonS3Client.putObject(bucketName, file.getOriginalFilename(), file.getInputStream(), metadata);
			} catch (IOException ioe) {
				throw new CustomException(ioe.getMessage());
			} catch (AmazonServiceException serviceException) {
				throw new CustomException(serviceException.getMessage());
			} catch (AmazonClientException clientException) {
				throw new CustomException(clientException.getMessage());
			}
		} else {
			throw new CustomException(environment.getProperty(ExceptionConstant.FILE_NOT_FOUND));
		}
	}
}
