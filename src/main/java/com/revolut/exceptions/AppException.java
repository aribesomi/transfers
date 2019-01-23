package com.revolut.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


public class AppException extends WebApplicationException {
	private static final long serialVersionUID = 1L;

	public AppException(String message, Status httpStatus) {
        super(Response.status(httpStatus).entity(message).type(MediaType.TEXT_PLAIN).build());
    }
	
	public AppException(String message, Status httpStatus, Throwable e) {
		super(e, Response.status(httpStatus).entity(message).type(MediaType.TEXT_PLAIN).build());
    }
}