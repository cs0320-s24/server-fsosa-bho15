package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.census.CensusAPIUtilities;
import edu.brown.cs.student.main.exceptions.DataSourceException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import okio.Buffer;
import spark.Request;
import spark.Response;
import spark.Route;

public class CensusHandler implements Route {

  private final Map<String, String> stateCodes;

  public CensusHandler() {
    this.stateCodes = new HashMap<>();
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> responseMap = new HashMap<>();
    if (!this.stateCodes.containsKey("california")) this.mapStateCodes();
    String state = request.queryParams("state");
    String stateCode;
    stateCode = this.stateCodes.get(state.toLowerCase());
    if (stateCode == null) {
      //TODO: Add to response map
      responseMap.put("result", "Invalid state");
      System.err.println("Invalid state");
      return responseMap;
    }
    String county = request.queryParams("county");
    String countyCode = "031"; // temp
    try {
      // Sends a request to the API and receives JSON back
      URL url =
          new URL(
              "https://api.census.gov/data/2021/acs/acs1/subject/variables?"
                  + "get=NAME,S2802_C03_022E&for=county:"
                  + countyCode
                  + "&in=state:"
                  + stateCode);
      HttpURLConnection censusJson = connect(url);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("time", new Date());
      responseMap.put("state", state);
      responseMap.put("county", county);
      responseMap.put(
          "percentage",
          CensusAPIUtilities.deserializeCensus(new Buffer().readFrom(censusJson.getInputStream()))
              .get(1)
              .get(1));
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

  /**
   * Private helper method; throws IOException so different callers can handle differently if
   * needed.
   */
  private static HttpURLConnection connect(URL requestURL) throws DataSourceException, IOException {
    URLConnection urlConnection = requestURL.openConnection();
    if (!(urlConnection instanceof HttpURLConnection clientConnection))
      throw new DataSourceException("unexpected: result of connection wasn't HTTP");
    clientConnection.connect(); // GET
    if (clientConnection.getResponseCode() != 200)
      throw new DataSourceException(
          "unexpected: API connection not success status " + clientConnection.getResponseMessage());
    return clientConnection;
  }
  private void mapStateCodes() throws IOException, DataSourceException {
    URL statesURL = new URL("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*");
    HttpURLConnection statesJson = connect(statesURL);
    List<List<String>> states =
        CensusAPIUtilities.deserializeCensus(new Buffer().readFrom(statesJson.getInputStream()));
    for (List<String> strings : states) {
      this.stateCodes.put(strings.get(0).toLowerCase(), strings.get(1));
    }
  }
}
