package com.Wrapper;

import org.springframework.http.HttpStatus;

public class ResponseWrapper {

	static String message=null;
	static HttpStatus statuscode;
	static String Warning=null;
	
	public static String getMessage() {
		return message;
	}
	public static void setMessage(String message) {
		ResponseWrapper.message = message;
	}
	public static HttpStatus getStatuscode() {
		return statuscode;
	}
	public static void setStatuscode(HttpStatus statuscode) {
		ResponseWrapper.statuscode = statuscode;
	}
	public static String getWarning() {
		return Warning;
	}
	public static void setWarning(String warning) {
		Warning = warning;
	}

	
	
}
