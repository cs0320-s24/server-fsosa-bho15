package edu.brown.cs.student.main.datasource;

import java.util.Map;

public interface CensusDataSource {
  Map<String, Object> getCensusData(String state, String county);
}
