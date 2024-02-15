package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.datasource.CachingCensusDataSource;
import spark.Spark;

public class Server {
  public static void main(String[] args) {
    int port = 3241;
    Spark.port(port);

    // TODO: think about how to change this for security
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Setting up the handler for the GET csv and census endpoints
    LoadCSVHandler loadHandler = new LoadCSVHandler();
    CachingCensusDataSource dataSource = new CachingCensusDataSource(true, 10, 1);

    Spark.get("load", loadHandler);
    Spark.get("view", new ViewCSVHandler(loadHandler));
    Spark.get("search", new SearchCSVHandler(loadHandler));
    Spark.get("broadband", new CensusHandler(dataSource));
    Spark.init();
    Spark.awaitInitialization();

    // Notice this link alone leads to a 404... Why is that?
    System.out.println("Server started at http://localhost:" + port);
  }
}
