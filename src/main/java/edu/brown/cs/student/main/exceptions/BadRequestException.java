package edu.brown.cs.student.main.exceptions;

public class BadRequestException extends APIException {

  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public String getErrorCode() {
    return "error_bad_request";
  }
}
