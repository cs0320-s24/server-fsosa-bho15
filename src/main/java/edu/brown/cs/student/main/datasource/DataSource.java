package edu.brown.cs.student.main.datasource;

import edu.brown.cs.student.main.census.CensusAPIUtilities;
import edu.brown.cs.student.main.exceptions.DataSourceException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okio.Buffer;

public class DataSource {
  public static Map<String, String> stateCodes;
  public DataSource() {
    stateCodes = new HashMap<>();
  }
  public static Map<String, Object> accessAPI(String state, String county){
    Map<String, Object> responseMap = new HashMap<>();
    String stateCode;
    stateCode = stateCodes.get(state.toLowerCase());
    String countyCode = "031"; //temp
    if (stateCode == null) {
      Map<String, Object> errorResponseMap = new HashMap<>();
      responseMap.put("result", "Invalid state");
      System.err.println("Invalid state");
      return errorResponseMap;
    }
    try {
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
    } catch (Exception e) {
      e.printStackTrace();
      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
    }
    return responseMap;
  }
  public static HttpURLConnection connect(URL requestURL) throws DataSourceException, IOException {
    URLConnection urlConnection = requestURL.openConnection();
    if (!(urlConnection instanceof HttpURLConnection clientConnection))
      throw new DataSourceException("unexpected: result of connection wasn't HTTP");
    clientConnection.connect(); // GET
    if (clientConnection.getResponseCode() != 200)
      throw new DataSourceException(
          "unexpected: API connection not success status " + clientConnection.getResponseMessage());
    return clientConnection;
  }
}
