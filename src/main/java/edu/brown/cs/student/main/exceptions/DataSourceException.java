package edu.brown.cs.student.main.exceptions;

/**
 * Exception that represents an issue with the datasource.
 */
public class DataSourceException extends APIException {

  public DataSourceException(String message) {
    super(message);
  }

  public DataSourceException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Returns error code for this exception.
   * @return String that represents the error code.
   */
  @Override
  public String getErrorCode() {
    return "error_datasource";
  }
}
