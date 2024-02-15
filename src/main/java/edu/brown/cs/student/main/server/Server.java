package edu.brown.cs.student.main.server;

import static spark.Spark.after;

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

<<<<<<< Updated upstream
    // Setting up the handler for the GET csv and census endpoints
    LoadCSVHandler loadHandler = new LoadCSVHandler();

    Spark.get("load", loadHandler);
    Spark.get("view", new ViewCSVHandler(loadHandler));
    Spark.get("search", new SearchCSVHandler(loadHandler));
    Spark.get("broadband", new CensusHandler());
=======
    // Setting up the handler for the GET /order and /activity endpoints
    Spark.get("load", new LoadCSVHandler());
    Spark.get("load", new ViewCSVHandler());
    Spark.get("census", new CensusHandler());
>>>>>>> Stashed changes
    Spark.init();
    Spark.awaitInitialization();

    // Notice this link alone leads to a 404... Why is that?
    System.out.println("Server started at http://localhost:" + port);
  }
}
