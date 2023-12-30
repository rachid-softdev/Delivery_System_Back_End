package fr.univrouen.deliverysystem.delivery;

import org.springframework.http.HttpStatus;

import fr.univrouen.deliverysystem.application.exception.ApplicationException;

public class DeliveryException extends ApplicationException {

    public DeliveryException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }

}
