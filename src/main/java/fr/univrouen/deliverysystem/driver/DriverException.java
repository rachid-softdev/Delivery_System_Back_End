package fr.univrouen.deliverysystem.driver;

import org.springframework.http.HttpStatus;

import fr.univrouen.deliverysystem.application.exception.ApplicationException;

public class DriverException extends ApplicationException {

    public DriverException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }

}
