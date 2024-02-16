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

/** This class represents the ACS datasource. */
public class ACSDataSource {
  public static Map<String, String> stateCodes = new HashMap<>();

  public ACSDataSource() {}

  /**
   * This method returns the state code, given the state name.
   *
   * @param state String representing the state name.
   * @return String representing the state code.
   * @throws DataSourceException if there is an issue with the data source.
   * @throws IOException if there is an issue reading the data source.
   */
  private static String getStateCode(String state) throws DataSourceException, IOException {
    if (stateCodes.isEmpty()) {
      URL statesURL = new URL("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*");
      HttpURLConnection statesJson = ACSDataSource.connect(statesURL);

      try (Buffer reader = new Buffer().readFrom(statesJson.getInputStream())) {
        List<List<String>> states = CensusAPIUtilities.deserializeCensus(reader);
        for (List<String> strings : states) {
          stateCodes.put(strings.get(0).toLowerCase(), strings.get(1));
        }
      }
    }
    return stateCodes.get(state.toLowerCase());
  }

  /**
   * This method returns the county code, given the county name.
   *
   * @param county String that represents the name of the county.
   * @param stateCode String that represents the code of the state.
   * @return String that represents the county code.
   * @throws DataSourceException if there is an issue with the datasource.
   * @throws IOException if there is an issue reading the datasource.
   * @throws BadRequestException if the county doesn't exist.
   */
  private static String getCountyCode(String county, String stateCode)
      throws DataSourceException, IOException, BadRequestException {
    if (stateCode == null) throw new BadRequestException("Invalid state.");
    URL countyURL =
        new URL(
            "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode);
    HttpURLConnection countyJson = ACSDataSource.connect(countyURL);
    try (Buffer reader = new Buffer().readFrom(countyJson.getInputStream())) {
      List<List<String>> counties = CensusAPIUtilities.deserializeCensus(reader);
      for (List<String> strings : counties) {
        String countyName = strings.get(0).split(",")[0];
        if (strings.get(1).equals(stateCode) && countyName.equals(county)) {
          return strings.get(2);
        }
      }
    }
    throw new BadRequestException("County was not found in the data.");
  }

  /**
   * Method that access the API to get certain information.
   *
   * @param state String that represents the state.
   * @param county String that represents the county.
   * @return List of matches.
   * @throws BadRequestException if the request is malformed.
   * @throws DataSourceException if the data source is not ready.
   * @throws IOException if there is an issue reading.
   */
  public static List<List<String>> accessAPI(String state, String county)
      throws BadRequestException, DataSourceException, IOException {
    if (state == null) {
      throw new BadRequestException("Please enter a valid state.");
    }
    if (county == null) {
      throw new BadRequestException("Please enter a valid county.");
    }
    String stateCode = getStateCode(state);
    String countyCode = getCountyCode(county, stateCode);
    List<List<String>> results;
    try {
      URL url =
          new URL(
              "https://api.census.gov/data/2021/acs/acs1/subject/variables?"
                  + "get=NAME,S2802_C03_022E&for=county:"
                  + countyCode
                  + "&in=state:"
                  + stateCode);
      HttpURLConnection censusJson = connect(url);
      try (Buffer reader = new Buffer().readFrom(censusJson.getInputStream())) {
        results = CensusAPIUtilities.deserializeCensus(reader);
      }
    } catch (Exception e) {
      throw new DataSourceException("Broadband percentage information unavailable.");
    }
    return results;
  }

  /**
   * Creates an HttpURLConnection given a request URL.
   *
   * @param requestURL URL for the connection.
   * @return HttpURLConnection that can be accessed later.
   * @throws DataSourceException if the connection could not be made.
   * @throws IOException if there was a reading or writing issue.
   */
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
