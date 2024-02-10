package edu.brown.cs.student.main.Creators;

import java.util.List;

public class CreatorArray implements CreatorFromRow<String[]> {

  /**
   * create method to convert the list of strings into an object of type array.
   *
   * @param row list of strings representing a csv row
   * @return array of strings
   */
  @Override
  public String[] create(List<String> row) {
    String[] ret = new String[row.size()];
    for (int i = 0; i < row.size(); i++) {
      ret[i] = row.get(i);
    }
    return ret;
  }
}
