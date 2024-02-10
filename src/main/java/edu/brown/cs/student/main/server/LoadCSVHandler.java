package edu.brown.cs.student.main.server;

import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadCSVHandler implements Route {

  @Override
  public Object handle(Request request, Response response) {
    String filePath = request.queryParams("filePath");
    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {

    } catch (Exception e) {

    }
    return null;
  }

//  public
}
