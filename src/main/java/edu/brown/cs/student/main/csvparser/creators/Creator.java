package edu.brown.cs.student.main.csvparser.creators;

import java.util.List;

public class Creator implements CreatorFromRow<List<String>> {

  /**
   * create method to convert the list of strings into an object of type t. for our purposes, it can
   * remain a list of strings so we simply return the input.
   *
   * @param row list of strings representing a csv row
   * @return the same list of strings
   */
  @Override
  public List<String> create(List<String> row) {
    return row;
  }
}
