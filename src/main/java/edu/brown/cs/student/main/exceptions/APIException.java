package edu.brown.cs.student.main.exceptions;

public abstract class APIException extends Exception {
  private final Throwable cause;
  public APIException(String message) {
    super(message);
    this.cause = null;
  }

  public APIException(String message, Throwable cause) {
    super(message, cause);
    this.cause = cause;
  }

  public Throwable getCause() {
    return this.cause;
  }

  public String getErrorCode() {
    return null;
  }
}
