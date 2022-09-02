package com.mindbowser.service;

import org.springframework.web.multipart.MultipartFile;

import com.mindbowser.exception.CustomException;

public interface AWSS3BucketService {

	void uploadFile(MultipartFile file) throws CustomException;

}
