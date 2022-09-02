package com.mindbowser.model;

import java.util.Date;
import java.util.List;

public class ErrorResponse {
	private Date timestamp;
	private String message;
	private List<String> details;
	private int statusCode;
	

	public ErrorResponse(Date timestamp, String message, List<String> details, int statusCode) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
		this.statusCode = statusCode;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}


	public int getStatusCode() {
		return statusCode;
	}

	public List<String> getDetails() {
		return details;
	}
	

}