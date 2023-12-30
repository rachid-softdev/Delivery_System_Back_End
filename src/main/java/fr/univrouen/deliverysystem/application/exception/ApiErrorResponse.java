package fr.univrouen.deliverysystem.application.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiErrorResponse {

    @JsonProperty("guid")
    private String guid;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("statusCode")
    private Integer statusCode;

    @JsonProperty("statusName")
    private String statusName;

    @JsonProperty("path")
    private String path;

    @JsonProperty("method")
    private String method;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
	
    @JsonProperty("errors")
    private List<ValidationError> errors;

    public ApiErrorResponse(
            String guid,
            String errorCode,
            String message,
            Integer statusCode,
            String statusName,
            String path,
            String method,
            LocalDateTime timestamp,
            List<ValidationError> errors
    ) {
        this.guid = guid;
        this.errorCode = errorCode;
        this.message = message;
        this.statusCode = statusCode;
        this.statusName = statusName;
        this.path = path;
        this.method = method;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    public static ApiErrorResponseBuilder builder() {
        return new ApiErrorResponseBuilder();
    }

    public static class ApiErrorResponseBuilder {
        private String guid;
        private String errorCode;
        private String message;
        private Integer statusCode;
        private String statusName;
        private String path;
        private String method;
        private LocalDateTime timestamp;
        private List<ValidationError> errors;

        public ApiErrorResponseBuilder guid(String guid) {
            this.guid = guid;
            return this;
        }

        public ApiErrorResponseBuilder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ApiErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ApiErrorResponseBuilder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ApiErrorResponseBuilder statusName(String statusName) {
            this.statusName = statusName;
            return this;
        }

        public ApiErrorResponseBuilder path(String path) {
            this.path = path;
            return this;
        }

        public ApiErrorResponseBuilder method(String method) {
            this.method = method;
            return this;
        }

        public ApiErrorResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ApiErrorResponseBuilder errors(List<ValidationError> errors) {
            this.errors = errors;
            return this;
        }

        public ApiErrorResponse build() {
            return new ApiErrorResponse(guid, errorCode, message, statusCode, statusName, path, method, timestamp, errors);
        }
    }
}
