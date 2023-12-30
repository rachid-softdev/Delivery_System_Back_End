package fr.univrouen.deliverysystem.application.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationError {

	@JsonProperty("class_name")
	private String className;

	@JsonProperty("field")
	private String field;

	@JsonProperty("violation_message")
	private String violationMessage;

	public ValidationError() {
		this.className = null;
		this.field = null;
		this.violationMessage = null;
	}

	public ValidationError(String className, String field, String violationMessage) {
		this.className = className;
		this.field = field;
		this.violationMessage = violationMessage;
	}

	public String getClassName() {
		return className;
	}

	public String getField() {
		return field;
	}

	public String getViolationMessage() {
		return violationMessage;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setViolationMessage(String violationMessage) {
		this.violationMessage = violationMessage;
	}

	@Override
	public String toString() {
		return "ValidationError{" +
				"className='" + className + '\'' +
				", field='" + field + '\'' +
				", violationMessage='" + violationMessage + '\'' +
				'}';
	}

	public static ValidationErrorBuilder builder() {
		return new ValidationErrorBuilder();
	}

	public static class ValidationErrorBuilder {

		private String className = "";
		private String field = "";
		private String violationMessage = "";

		public ValidationErrorBuilder className(String className) {
			this.className = className;
			return this;
		}

		public ValidationErrorBuilder field(String field) {
			this.field = field;
			return this;
		}

		public ValidationErrorBuilder violationMessage(String violationMessage) {
			this.violationMessage = violationMessage;
			return this;
		}

		public ValidationError build() {
			return new ValidationError(className, field, violationMessage);
		}
	}
}
