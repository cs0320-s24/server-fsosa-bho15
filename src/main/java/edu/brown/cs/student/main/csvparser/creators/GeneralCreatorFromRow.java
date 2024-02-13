package edu.brown.cs.student.main.csvparser.creators;

import java.util.ArrayList;
import java.util.List;

/** This implementation of CreatorFromRow keeps rows as it is */
public class GeneralCreatorFromRow implements CreatorFromRow<List<String>> {

  public GeneralCreatorFromRow() {}

  /**
   * Trivially sends back the row.
   *
   * @param row from the csv
   * @return row
   */
  @Override
  public List<String> create(List<String> row) {
    return row;
  }

  /**
   * Checks to see if the csv contains a given attribute value in a given column
   *
   * @param elements a list of objects
   * @param attribute the specified column
   * @param value the specified value
   * @return a list of matches
   */
  @Override
  public List<List<String>> containsAttribute(
      List<List<String>> elements, int attribute, String value) {
    List<List<String>> matches = new ArrayList<>();
    for (List<String> element : elements) {
      if (element.get(attribute).equalsIgnoreCase(value)) {
        matches.add(element);
      }
    }
    return matches;
  }

  /**
   * Checks if the csv contains a value anywhere
   *
   * @param elements list of objects
   * @param value specified value
   * @return a list of matches
   */
  @Override
  public List<List<String>> contains(List<List<String>> elements, String value) {
    List<List<String>> matches = new ArrayList<>();
    for (List<String> element : elements) {
      for (int j = 0; j < element.size(); j++) {
        if (element.get(j).equalsIgnoreCase(value)) {
          matches.add(element);
          break;
        }
      }
    }
    return matches;
  }
}
