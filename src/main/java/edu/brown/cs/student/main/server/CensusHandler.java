package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.census.CensusAPIUtilities;
import edu.brown.cs.student.main.datasource.DataSource;
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

  public CensusHandler() {
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    if (!DataSource.stateCodes.containsKey("california")) this.mapStateCodes(DataSource.stateCodes);
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    return DataSource.accessAPI(state, county);
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
