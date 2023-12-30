package fr.univrouen.deliverysystem.driver;

import org.springframework.http.HttpStatus;

public class DriverNotFoundException extends DriverException {

	public DriverNotFoundException(String errorCode, String message, HttpStatus httpStatus) {
		super(errorCode, message, httpStatus);
	}

}
