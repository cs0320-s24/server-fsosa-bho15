package edu.brown.cs.student.main.exceptions;

public class BadRequestException extends Exception {
  // The root cause of this datasource problem
  private final Throwable cause;

  public BadRequestException(String message) {
    super(message); // Exception message
    this.cause = null;
  }

  public BadRequestException(String message, Throwable cause) {
    super(message); // Exception message
    this.cause = cause;
  }

  /**
   * Returns the Throwable provided (if any) as the root cause of this exception. We don't make a
   * defensive copy here because we don't anticipate mutation of the Throwable to be any issue, and
   * because this is mostly implemented for debugging support.
   *
   * @return the root cause Throwable
   */
  public Throwable getCause() {
    return this.cause;
  }
}
