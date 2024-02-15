package edu.brown.cs.student.main.datasource;

import edu.brown.cs.student.main.census.CensusAPIUtilities;
import edu.brown.cs.student.main.exceptions.BadRequestException;
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
  public static Map<String, String> stateCodes = new HashMap<>();

  public DataSource() {
    stateCodes = new HashMap<>();
  }

  public static Map<String, String> getStateCodes() {
    return stateCodes;
  }

  public static List<List<String>> accessAPI(String state, String county)
      throws BadRequestException, DataSourceException {
    String stateCode;
    stateCode = stateCodes.get(state.toLowerCase());
    String countyCode = "031"; // temp
    if (stateCode == null) {
      throw new BadRequestException("State does not exist.");
    }
    List<List<String>> results = null;
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
      results = CensusAPIUtilities.deserializeCensus(
          new Buffer().readFrom(censusJson.getInputStream()));
    } catch (Exception e) {
      throw new DataSourceException("There was an issue accessing the datasource.");
    }
    return results;
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
