package edu.brown.cs.student;

public class IntegrationTest {

  //  @BeforeAll
  //  public static void setup_before_everything() {
  //    Spark.port(0);
  //    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  //  }
  //
  //  /**
  //   * Shared state for all tests. We need to be able to mutate it (adding recipes etc.) but never
  //   * need to replace the reference itself. We clear this state out after every test runs.
  //   */
  //  final Map<String, Object> responseMap = new HashMap<>();
  //
  //  @BeforeEach
  //  public void setup() {
  //    // Re-initialize state, etc. for _every_ test method run
  //    this.responseMap.clear();
  //
  //    // In fact, restart the entire Spark server for every test!
  //    LoadCSVHandler loadHandler = new LoadCSVHandler();
  //    CachingCensusDataSource dataSource = new CachingCensusDataSource(true, 10, 1);
  //
  //    Spark.get("load", loadHandler);
  //    Spark.get("view", new ViewCSVHandler(loadHandler));
  //    Spark.get("search", new SearchCSVHandler(loadHandler));
  //    Spark.get("broadband", new CensusHandler(dataSource));
  //    Spark.init();
  //    Spark.awaitInitialization();
  //  }
  //
  //  @AfterEach
  //  public void teardown() {
  //    // Gracefully stop Spark listening on both endpoints after each test
  //    Spark.unmap("load");
  //    Spark.unmap("view");
  //    Spark.unmap("search");
  //    Spark.unmap("broadband");
  //    Spark.awaitStop(); // don't proceed until the server is stopped
  //  }
  //
  //  /**
  //   * Helper to start a connection to a specific API endpoint/params
  //   *
  //   * @param apiCall the call string, including endpoint (NOTE: this would be better if it had
  // more
  //   *     structure!)
  //   * @return the connection for the given URL, just after connecting
  //   * @throws IOException if the connection fails for some reason
  //   */
  //  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
  //    // Configure the connection (but don't actually send the request yet)
  //    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
  //    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
  //
  //    // The default method is "GET", which is what we're using here.
  //    // If we were using "POST", we'd need to say so.
  //    clientConnection.setRequestMethod("GET");
  //
  //    clientConnection.connect();
  //    return clientConnection;
  //  }
  //
  //  @Test
  //  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the
  // type
  //  // checker
  //  public void testAPICorrectCensusRequest() throws IOException {
  //    HttpURLConnection clientConnection =
  //        tryRequest("broadband?county=Scott County&state=Minnesota");
  //    assertEquals(
  //        "http://localhost:3241/broadband?county=Scott%20County&state=Minnesota",
  //        "http://localhost:" + Spark.port() +
  // "/broadband?county=Scott%20County&state=Minnesota");
  //    assertEquals(200, clientConnection.getResponseCode());
  //    List<List<String>> results =
  //        CensusAPIUtilities.deserializeCensus(
  //            new Buffer().readFrom(clientConnection.getInputStream()));
  //    System.out.println(results);
  //    clientConnection.disconnect();
  //  }
}