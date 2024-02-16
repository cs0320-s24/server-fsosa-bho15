package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.datasource.CachingACSDataSource;
import edu.brown.cs.student.main.server.CensusHandler;
import edu.brown.cs.student.main.server.LoadCSVHandler;
import edu.brown.cs.student.main.server.SearchCSVHandler;
import edu.brown.cs.student.main.server.ViewCSVHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class UnitTest {

  //  @BeforeAll
  //  public static void setup_before_everything() {
  //    Spark.port(0);
  //    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  //  }

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
  public void testLoadCSV() throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/load?filepath=ri.csv");
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    assertEquals(200, clientConnection.getResponseCode());
    clientConnection.disconnect();
  }
}
