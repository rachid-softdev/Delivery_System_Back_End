package fr.univrouen.deliverysystem.delivery;

import org.springframework.http.HttpStatus;

public class DeliveryNotFoundException extends DeliveryException {

    public DeliveryNotFoundException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }

}
