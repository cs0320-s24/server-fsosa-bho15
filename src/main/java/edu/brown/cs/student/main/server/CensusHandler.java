package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.census.CensusAPIUtilities;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class CensusHandler implements Route {

  private boolean called;
  private Map<String, String> stateCodes;
  private Map<String, String> countyCodes;

  public CensusHandler() {
    this.called = false;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String state = request.queryParams("state");
    //String stateCode = requestStateCode(state);
    String stateCode = "06"; // temp
    // String stateCode = requestStateCode(state);
    String county = request.queryParams("county");
    String countyCode = "031"; // temp
    // int countyCode = requestCountyCode(county);
    this.called = true;
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      String censusJson = this.sendRequest(stateCode, countyCode);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("census", CensusAPIUtilities.deserializeCensus(censusJson));
      return responseMap;
    } catch (Exception e) {
      e.printStackTrace();
      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
    }
    return responseMap;
  }

  private String sendRequest(String stateCode, String countyCode)
      throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildCensusApiRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2021/acs/acs1/subject/variables?"
                        + "get=NAME,S2802_C03_022E&for=county:"
                        + countyCode
                        + "&in=state:"
                        + stateCode))
            .GET()
            .build();

    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> sentCensusApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildCensusApiRequest, HttpResponse.BodyHandlers.ofString());

    // What's the difference between these two lines? Why do we return the body? What is useful from
    // the raw response (hint: how can we use the status of response)?
    System.out.println(sentCensusApiResponse);
    System.out.println(sentCensusApiResponse.body());

    //    return sentCensusApiResponse.body();
    return null;
  }

  private String requestStateCode(String stateName)
      throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildCensusApiRequest =
        HttpRequest.newBuilder()
            .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"))
            .GET()
            .build();

    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> sentCensusApiResponse =
        HttpClient.newBuilder().build().send(buildCensusApiRequest, BodyHandlers.ofString());
    System.out.println(sentCensusApiResponse);
    String jsonString = sentCensusApiResponse.body();
    return sentCensusApiResponse.body();
  }
    private String requestCountyCode(String countyName)
        throws URISyntaxException, IOException, InterruptedException {
      HttpRequest buildBoredApiRequest =
          HttpRequest.newBuilder()
              .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*"))
              .GET()
              .build();

      // Send that API request then store the response in this variable. Note the generic type.
      HttpResponse<String> sentBoredApiResponse =
          HttpClient.newBuilder()
              .build()
              .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());
      String code = sentBoredApiResponse.body();
      return code;
    }
}
