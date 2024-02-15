package edu.brown.cs.student.main.exceptions;

public class DataSourceException extends APIException {

  public DataSourceException(String message) {
    super(message);
  }

  public DataSourceException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public String getErrorCode() {
    return "error_datasource";
  }
}
