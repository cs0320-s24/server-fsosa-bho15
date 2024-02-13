package edu.brown.cs.student.main.exceptions;

/** This is an error provided when an attribute provided does not exist in the csv. */
public class MissingAttributeException extends Exception {

  final String attribute;

  /**
   * Exception for attribute missing
   *
   * @param message descriptive error message
   * @param attribute attribute missing
   */
  public MissingAttributeException(String message, String attribute) {
    super(message);
    this.attribute = attribute;
  }
}
