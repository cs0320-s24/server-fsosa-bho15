package edu.brown.cs.student.main.datasource;

import java.util.Map;

/**
 * This interface models a proxy class for getting the Census data.
 */
public interface ACSProxyInterface {
  Map<String, Object> getCensusData(String state, String county);
}
