package fr.univrouen.deliverysystem.application.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import fr.univrouen.deliverysystem.application.exception.ApiErrorResponse.ApiErrorResponseBuilder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/**
 * Inspired by :
 * https://www.baeldung.com/global-error-handler-in-a-spring-rest-api
 * Pour les exceptions qui ne sont pas dans la classe
 * ResponseEntityExceptionHandler,
 * il faut ajouter l'annotation @ExceptionHandler pour les gérer
 * Pour les exceptions déja intégrés, il faut que redéfinir les méthodes de
 * ResponseEntityExceptionHandler
 * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/ResponseEntityExceptionHandler.html
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger log = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<?> handleApplicationException(final ApplicationException exception,
			final HttpServletRequest request) {
		final String guid = UUID.randomUUID().toString();
		log.error(String.format("Error GUID=%s; error message: %s", guid, exception.getMessage()), exception);
		final ApiErrorResponse response = new ApiErrorResponse(guid, exception.getErrorCode(),
				exception.getMessage(), exception.getHttpStatus().value(), exception.getHttpStatus().name(),
				request.getRequestURI(), request.getMethod(), LocalDateTime.now(), new ArrayList<ValidationError>());
		return new ResponseEntity<>(response, exception.getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleUnknownException(final Exception exception,
			final HttpServletRequest request) {
		final String guid = UUID.randomUUID().toString();
		log.error(String.format("Error GUID=%s; error message: %s", guid, exception.getMessage()), exception);
		final String errorMessage = exception.getMessage(); // "Internal server error";
		final ApiErrorResponse response = new ApiErrorResponse(guid,
				ApplicationExceptionHandler.ErrorCodes.INTERNAL_ERROR.getCode(), errorMessage,
				HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
				request.getRequestURI(), request.getMethod(), LocalDateTime.now(), new ArrayList<ValidationError>());
		return new ResponseEntity<ApiErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Capture de l'exception ConstraintViolationException
	 * Format des exceptions de contraintes de validation
	 * 
	 * @return Les erreurs des contraintes de validation
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		final String guid = UUID.randomUUID().toString();
		final Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
		List<ValidationError> validationErrors = violations.stream()
				.map(violation -> new ValidationError(
						violation.getRootBeanClass().getName(),
						violation.getPropertyPath().toString(),
						violation.getMessage()))
				.collect(Collectors.toList());
		final HttpServletRequest servletRequest = this.getHttpServletRequest(request);
		final ApiErrorResponse apiError = new ApiErrorResponseBuilder()
				.guid(guid)
				.errorCode(ApplicationExceptionHandler.ErrorCodes.BAD_REQUEST.getCode())
				.message(ex.getMessage())
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.statusName(HttpStatus.BAD_REQUEST.name())
				.path(request.getDescription(false))
				.method(servletRequest != null ? servletRequest.getMethod() : null)
				.timestamp(LocalDateTime.now())
				.errors(validationErrors)
				.build();
		return handleExceptionInternal(
				ex, apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * This exception is thrown when an argument annotated with @Valid failed
	 * validation.
	 * Source :
	 * https://stackoverflow.com/questions/38282298/ambiguous-exceptionhandler-method-mapped-for-class-org-springframework-web-bin
	 * in Spring 3.1.0. It means that you need to @Overridemethod
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		final String guid = UUID.randomUUID().toString();
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<ValidationError> validationErrors = fieldErrors.stream()
				.map(fieldError -> new ValidationError(
						fieldError.getObjectName(),
						fieldError.getField(),
						fieldError.getDefaultMessage()))
				.collect(Collectors.toList());
		final HttpServletRequest servletRequest = this.getHttpServletRequest(request);
		final ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
				guid,
				ApplicationExceptionHandler.ErrorCodes.BAD_REQUEST.getCode(),
				ex.getMessage(),
				status.value(),
				HttpStatus.BAD_REQUEST.name(),
				request.getDescription(false),
				servletRequest != null ? servletRequest.getMethod() : null,
				LocalDateTime.now(),
				validationErrors);
		return handleExceptionInternal(
				ex, apiErrorResponse, headers, HttpStatus.BAD_REQUEST, request);
	}

	private HttpServletRequest getHttpServletRequest(WebRequest request) {
		if (request instanceof ServletWebRequest) {
			return ((ServletWebRequest) request).getRequest();
		}
		return null;
	}

	private enum ErrorCodes {

		INTERNAL_ERROR("INTERNAL_ERROR", "An internal server error occurred."),
		INVALID_REQUEST("INVALID_REQUEST", "The request is invalid."),
		RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "The requested resource was not found."),
		UNAUTHORIZED("UNAUTHORIZED", "Unauthorized access."),
		BAD_REQUEST("BAD_REQUEST", "Bad request."),
		FORBIDDEN("FORBIDDEN", "Access to the resource is forbidden.");

		private final String code;

		private final String description;

		ErrorCodes(String code, String description) {
			this.code = code;
			this.description = description;
		}

		public String getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}

	}

}
