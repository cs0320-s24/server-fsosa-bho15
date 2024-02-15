package edu.brown.cs.student.main.datasource;

import java.util.Map;

public interface CensusDataSource {
  public Map<String, Object> getCensusData(String state, String county);
}
