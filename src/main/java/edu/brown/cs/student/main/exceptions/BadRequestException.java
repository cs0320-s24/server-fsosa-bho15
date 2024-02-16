package edu.brown.cs.student.main.exceptions;

/** Exception that represents a malformed API request. */
public class BadRequestException extends APIException {

  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Returns the specified error code for this exception.
   *
   * @return String representing the error code.
   */
  @Override
  public String getErrorCode() {
    return "error_bad_request";
  }
}
