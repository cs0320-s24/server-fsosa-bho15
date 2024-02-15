package edu.brown.cs.student.main.server;

<<<<<<< Updated upstream
import edu.brown.cs.student.main.census.CensusAPIUtilities;
import edu.brown.cs.student.main.datasource.DataSource;
import edu.brown.cs.student.main.exceptions.DataSourceException;
=======
import edu.brown.cs.student.main.census.Census;
import edu.brown.cs.student.main.census.CensusAPIUtilities;
>>>>>>> Stashed changes
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.Map;
import okio.Buffer;
import spark.Request;
import spark.Response;
import spark.Route;

public class CensusHandler implements Route {

  public CensusHandler() {}

  @Override
  public Object handle(Request request, Response response) throws Exception {
<<<<<<< Updated upstream
    if (!DataSource.getStateCodes().containsKey("california")) {
      this.mapStateCodes(DataSource.getStateCodes());
=======
    String state = request.queryParams("state");
    String stateCode = "06"; // temp
    // int stateCode = requestStateCode(state);
    String county = request.queryParams("county");
    String countyCode = "031"; // temp
    // int countyCode = requestCountyCode(county);
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      String censusJson = this.sendRequest(stateCode, countyCode);
      // Deserializes JSON into a Census
      Census census = CensusAPIUtilities.deserializeCensus(censusJson);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("census", censusJson);
      return responseMap;
    } catch (Exception e) {
      e.printStackTrace();
      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
>>>>>>> Stashed changes
    }
    return DataSource.accessAPI(request.queryParams("state"), request.queryParams("county"));
  }

  private void mapStateCodes(Map<String, String> map) throws IOException, DataSourceException {
    URL statesURL = new URL("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*");
    HttpURLConnection statesJson = DataSource.connect(statesURL);
    List<List<String>> states =
        CensusAPIUtilities.deserializeCensus(new Buffer().readFrom(statesJson.getInputStream()));
    for (List<String> strings : states) {
      map.put(strings.get(0).toLowerCase(), strings.get(1));
    }
  }
}
