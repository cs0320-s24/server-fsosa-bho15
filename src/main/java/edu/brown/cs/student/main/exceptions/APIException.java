package edu.brown.cs.student.main.exceptions;

/** Abstract class that includes a way to get the error code for the response map. */
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

  /**
   * Returns the cause of the error
   *
   * @return a Throwable object that represents the cause.
   */
  public Throwable getCause() {
    return this.cause;
  }

  /**
   * Gets the error code for the responseMap.
   *
   * @return error code.
   */
  public String getErrorCode() {
    return null;
  }
}
