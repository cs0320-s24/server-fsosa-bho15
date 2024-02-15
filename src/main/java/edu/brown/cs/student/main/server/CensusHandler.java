package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.datasource.CachingCensusDataSource;
import edu.brown.cs.student.main.datasource.DataSource;
import spark.Request;
import spark.Response;
import spark.Route;

public class CensusHandler implements Route {

  private final CachingCensusDataSource datasource;

  public CensusHandler(CachingCensusDataSource datasource) {
    this.datasource = datasource;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    return this.datasource.getCensusData(request.queryParams("state"), request.queryParams("county"));
  }
}
