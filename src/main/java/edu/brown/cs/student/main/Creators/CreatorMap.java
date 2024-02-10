package edu.brown.cs.student.main.Creators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatorMap implements CreatorFromRow<Map<String, String>> {

  /**
   * create method to convert the list of strings into an object of type map.
   *
   * @param row list of strings representing a csv row
   * @return map of strings. maps first string in row to the rest.
   */
  @Override
  public Map<String, String> create(List<String> row) {
    Map<String, String> ret = new HashMap<>();
    if (row.size() == 0) return ret;
    ret.put(row.get(0), "");
    for (int i = 1; i < row.size(); i++) {
      ret.put(row.get(0), ret.get(row.get(0)) + row.get(i));
    }
    return ret;
  }
}
