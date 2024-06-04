package com.project.api.clients.security.exceptions;

import org.springframework.http.HttpStatus;

public class CustomException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private HttpStatus status;

    public CustomException(HttpStatus status, String message, int code) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
