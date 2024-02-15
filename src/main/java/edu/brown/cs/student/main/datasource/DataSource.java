package edu.brown.cs.student.main.datasource;

import edu.brown.cs.student.main.census.CensusAPIUtilities;
import edu.brown.cs.student.main.exceptions.BadRequestException;
import edu.brown.cs.student.main.exceptions.DataSourceException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okio.Buffer;

public class DataSource {
  public static Map<String, String> stateCodes = new HashMap<>();

  public DataSource() {}

  public static String getStateCode(String state) throws DataSourceException, IOException {
    if (stateCodes.isEmpty()) {
      URL statesURL = new URL("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*");
      HttpURLConnection statesJson = DataSource.connect(statesURL);
      List<List<String>> states =
          CensusAPIUtilities.deserializeCensus(new Buffer().readFrom(statesJson.getInputStream()));
      for (List<String> strings : states) {
        stateCodes.put(strings.get(0).toLowerCase(), strings.get(1));
      }
    }
    return stateCodes.get(state.toLowerCase());
  }

  public static List<List<String>> accessAPI(String state, String county)
      throws BadRequestException, DataSourceException, IOException {
    String stateCode = getStateCode(state);
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
      results =
          CensusAPIUtilities.deserializeCensus(new Buffer().readFrom(censusJson.getInputStream()));
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
