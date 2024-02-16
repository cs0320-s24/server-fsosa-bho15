package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.brown.cs.student.main.datasource.ACSDataSource;
import edu.brown.cs.student.main.datasource.CachingACSDataSource;
import edu.brown.cs.student.main.exceptions.BadRequestException;
import edu.brown.cs.student.main.exceptions.DataSourceException;
import edu.brown.cs.student.main.server.CensusHandler;
import edu.brown.cs.student.main.server.LoadCSVHandler;
import edu.brown.cs.student.main.server.SearchCSVHandler;
import edu.brown.cs.student.main.server.ViewCSVHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class IntegrationTest {

  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  @BeforeEach
  public void setup() {
    LoadCSVHandler loadHandler = new LoadCSVHandler();
    CachingACSDataSource dataSource = new CachingACSDataSource(true, 10, 1);

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
  public void testConnectToAPI() throws IOException, DataSourceException {
    URL url =
        new URL(
            "http://localhost:"
                + Spark.port()
                + "/broadband?county=Scott%20County&state=Minnesota");
    HttpURLConnection clientConnection = ACSDataSource.connect(url);
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection.disconnect();
  }

  @Test
  public void testCorrectAPIRequest() throws IOException, DataSourceException, BadRequestException {
    List<List<String>> results = ACSDataSource.accessAPI("Minnesota", "Scott County");
    assertEquals(91.9, Double.parseDouble(results.get(1).get(1)));
  }

  @Test
  public void testAPINoParameters() {
    assertThrows(
        BadRequestException.class,
        () -> {
          ACSDataSource.accessAPI(null, null);
        });
  }

  @Test
  public void testAPIMissingState() {
    assertThrows(
        BadRequestException.class,
        () -> {
          ACSDataSource.accessAPI(null, "Scott County");
        });
  }

  @Test
  public void testAPIMissingCounty() {
    assertThrows(
        BadRequestException.class,
        () -> {
          ACSDataSource.accessAPI("Minnesota", null);
        });
  }

  @Test
  public void testAPIInvalidState() {
    assertThrows(
        BadRequestException.class,
        () -> {
          ACSDataSource.accessAPI("America", "Scott County");
        });
  }

  @Test
  public void testAPIInvalidCounty() {
    assertThrows(
        BadRequestException.class,
        () -> {
          ACSDataSource.accessAPI("Minnesota", "America County");
        });
  }

}
