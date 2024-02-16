package edu.brown.cs.student.main.datasource;

import java.util.Collections;
import java.util.List;

/** Mock data source returns a predictable percentage. */
public class MockACSDataSource {

  /**
   * Gets a test percentage without a GET request.
   *
   * @param state to be searched.
   * @param county to be searched.
   * @return an output where the percentage is a double that is equivalent to county.length()
   */
  public static List<List<String>> accessAPI(String state, String county) {
    return List.of(Collections.singletonList(county), List.of(state, county.length() + ""));
  }
}
