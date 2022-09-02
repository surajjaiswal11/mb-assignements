package com.mindbowser.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AwsS3ClientConfig {

	@Value("${cloud.aws.credentials.accessKey}")
	private String awsAccessKey;

	@Value("${cloud.aws.credentials.secretKey}")
	private String awsSecretKey;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Bean
	public AmazonS3 s3client() {

		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
		return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
	}
}