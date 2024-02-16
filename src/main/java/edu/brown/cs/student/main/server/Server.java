package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.datasource.CachingACSDataSource;
import spark.Spark;

/**
 * The entry point to our program, which initializes the handlers.
 */
public class Server {
  public static void main(String[] args) {
    int port = 3241;
    Spark.port(port);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    LoadCSVHandler loadHandler = new LoadCSVHandler();
    CachingACSDataSource dataSource = new CachingACSDataSource(true, 10, 1);

    Spark.get("load", loadHandler);
    Spark.get("view", new ViewCSVHandler(loadHandler));
    Spark.get("search", new SearchCSVHandler(loadHandler));
    Spark.get("broadband", new CensusHandler(dataSource));

    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }
}
