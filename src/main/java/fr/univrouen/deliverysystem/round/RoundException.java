package fr.univrouen.deliverysystem.round;

import org.springframework.http.HttpStatus;

import fr.univrouen.deliverysystem.application.exception.ApplicationException;

public class RoundException extends ApplicationException {

    public RoundException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }

}
