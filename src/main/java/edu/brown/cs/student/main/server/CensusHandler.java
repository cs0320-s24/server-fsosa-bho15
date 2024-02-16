package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.datasource.ACSProxyInterface;
import spark.Request;
import spark.Response;
import spark.Route;

/** Handler class that represents the broadband endpoint. */
public class CensusHandler implements Route {

  private final ACSProxyInterface datasource;

  public CensusHandler(ACSProxyInterface datasource) {
    this.datasource = datasource;
  }

  /**
   * Use the proxy class to get the census data, given our request.
   *
   * @param request given through the browser.
   * @param response unused here.
   * @return the map that details the responses.
   */
  @Override
  public Object handle(Request request, Response response) {
    return this.datasource.getCensusData(
        request.queryParams("state"), request.queryParams("county"));
  }
}
