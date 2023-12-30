package fr.univrouen.deliverysystem.round;

import org.springframework.http.HttpStatus;

public class RoundNotFoundException extends RoundException {

	public RoundNotFoundException(String errorCode, String message, HttpStatus httpStatus) {
		super(errorCode, message, httpStatus);
	}

}
