package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.exceptions.DataSourceException;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadCSVHandler implements Route {

  private final ViewCSVHandler viewHandler;

  public LoadCSVHandler(ViewCSVHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  @Override
  public Object handle(Request request, Response response) {
    String filepath = request.queryParams("filepath");
    Map<String, Object> responseMap = new HashMap<>();
    try {
      this.viewHandler.load(filepath);
    } catch (DataSourceException e) {
      responseMap.put("result", "error_datasource");
      return responseMap;
    }
    responseMap.put("result", "success");
    responseMap.put("filepath", filepath);
    return responseMap;
  }
}
