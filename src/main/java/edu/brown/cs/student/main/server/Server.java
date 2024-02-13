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

    // Setting up the handler for the GET csv and census endpoints
    ViewCSVHandler viewHandler = new ViewCSVHandler();

    Spark.get("load", new LoadCSVHandler(viewHandler));
    Spark.get("view", viewHandler);
    Spark.get("broadband", new CensusHandler());
    Spark.init();
    Spark.awaitInitialization();

    // Notice this link alone leads to a 404... Why is that?
    System.out.println("Server started at http://localhost:" + port);
  }
}
