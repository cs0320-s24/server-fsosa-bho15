package edu.brown.cs.student.main.datasource;

public interface CensusDataSource {
  public String getCensusData(String state, String county);
}
