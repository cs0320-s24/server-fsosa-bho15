package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.csvparser.creators.GeneralCreatorFromRow;
import edu.brown.cs.student.main.csvparser.functionalclasses.Parser;
import edu.brown.cs.student.main.csvparser.functionalclasses.Search;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchCSVHandler implements Route {

  private final LoadCSVHandler loadHandler;
  private Search<List<String>> search;

  public SearchCSVHandler(LoadCSVHandler loadHandler) {
    this.loadHandler = loadHandler;
    this.search = null;
  }

  @Override
  public Object handle(Request request, Response response) {
    String columnName = request.queryParams("columnName");
    String columnIndex = request.queryParams("columnIndex");
    String attribute = request.queryParams("attribute");

    Map<String, Object> responseMap = new HashMap<>();

    Parser<List<String>> parser = this.loadHandler.getCSVParser();
    if (parser == null) {
      responseMap.put("result", "error_data_not_loaded");
      responseMap.put("message", "Load was not called before search");
    }
    this.search = new Search<>(parser, new GeneralCreatorFromRow());
    List<List<String>> results = null;

    if (attribute == null) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("message", "Attribute to search by was not given.");
      return responseMap;
    } else if (columnName != null && columnIndex != null) {
      responseMap.put("result", "error_bad_request");
      responseMap.put(
          "message", "Both column name and number were given. Please only include one.");
      return responseMap;
    } else if (columnName != null) {
      try {
        results = this.search.search(columnName, attribute);
      } catch (Exception e) {
        responseMap.put("result", "error_bad_request");
        responseMap.put("message", e.getMessage());
      }
    } else if (columnIndex != null) {
      try {
        results = this.search.search(columnIndex, attribute);
      } catch (Exception e) {
        responseMap.put("result", "error_bad_request");
        responseMap.put("message", e.getMessage());
      }
    }
    try {
      results = this.search.search(attribute);
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("message", e.getMessage());
    }
    responseMap.put("result", "success");
    responseMap.put("matches", results);
    return responseMap;
  }
}
