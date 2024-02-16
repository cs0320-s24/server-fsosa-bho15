package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.datasource.ACSProxyInterface;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class CensusHandler implements Route {

  private final ACSProxyInterface datasource;

  public CensusHandler(ACSProxyInterface datasource) {
    this.datasource = datasource;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    return this.datasource.getCensusData(
        request.queryParams("state"), request.queryParams("county"));
  }

  public record CensusSuccessResponse(String response_type, Map<String, Object> responseMap) {

    public CensusSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }
  }

  public record CensusFailureResponse(String response_type) {

    public CensusFailureResponse() {
      this("error");
    }
  }
}
