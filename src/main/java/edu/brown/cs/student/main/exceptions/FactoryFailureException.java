package edu.brown.cs.student.main.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an error provided to catch any error that may occur when you create an object from a row.
 * Feel free to expand or supplement or use it for other purposes.
 */
public class FactoryFailureException extends APIException {
  final List<String> row;

  public FactoryFailureException(String message, List<String> row) {
    super(message);
    this.row = new ArrayList<>(row);
  }

  public FactoryFailureException(String message, Throwable cause, List<String> row) {
    super(message, cause);
    this.row = new ArrayList<>(row);
  }

  /**
   * Returns the error code.
   *
   * @return String that represents the error code.
   */
  @Override
  public String getErrorCode() {
    return "error_factory_failure";
  }
}
