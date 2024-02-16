package edu.brown.cs.student.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.csvparser.creators.GeneralCreatorFromRow;
import edu.brown.cs.student.main.csvparser.functional.Parser;
import edu.brown.cs.student.main.csvparser.functional.Search;
import edu.brown.cs.student.main.datasource.CachingACSDataSource;
import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.server.CensusHandler;
import edu.brown.cs.student.main.server.LoadCSVHandler;
import edu.brown.cs.student.main.server.SearchCSVHandler;
import edu.brown.cs.student.main.server.ViewCSVHandler;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

/** unit test class for testing individual methods and load/view/search endpoints */
public class UnitTest {

  @BeforeEach
  public void setup() {
    LoadCSVHandler loadHandler = new LoadCSVHandler();
    CachingACSDataSource dataSource = new CachingACSDataSource(true, 10, 1, false);

    Spark.get("load", loadHandler);
    Spark.get("view", new ViewCSVHandler(loadHandler));
    Spark.get("search", new SearchCSVHandler(loadHandler));
    Spark.get("broadband", new CensusHandler(dataSource));
    Spark.init();
    Spark.awaitInitialization();
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints after each test
    Spark.unmap("load");
    Spark.unmap("view");
    Spark.unmap("search");
    Spark.unmap("broadband");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  @Test
  public void testLoadCSV() throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/load?filepath=ri.csv");
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection.disconnect();
  }

  @Test
  public void testChangeLoadedCSV() throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/load?filepath=ri.csv");
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    URL differentRequestURL =
        new URL("http://localhost:" + Spark.port() + "/load?filepath=ten-star.csv");
    clientConnection = (HttpURLConnection) differentRequestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection.disconnect();
  }

  @Test
  public void testViewCSV() throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/view");
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection.disconnect();
  }

  @Test
  public void testSearchCSV() throws IOException, FactoryFailureException {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    Map<String, Object> responseMap = new HashMap<>();
    Parser<List<String>> parser = new Parser<>(new FileReader("data/ri.csv"), true);
    Search search = new Search(parser, new GeneralCreatorFromRow());
    List<List<String>> results =
        search.search("Cranston", parser.parse(new GeneralCreatorFromRow()));
    responseMap.put("result", "success");
    responseMap.put("data", results);
    assertEquals(
        "{\"result\":\"success\",\"data\":[[\"Cranston\",\"77,145.00\",\"95,763.00\",\"38,269.00\"]]}",
        adapter.toJson(responseMap));
  }
}
