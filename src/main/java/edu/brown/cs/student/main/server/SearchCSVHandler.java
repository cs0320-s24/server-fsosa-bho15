package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.csvparser.creators.GeneralCreatorFromRow;
import edu.brown.cs.student.main.csvparser.functional.Parser;
import edu.brown.cs.student.main.csvparser.functional.Search;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
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
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);

    String columnName = request.queryParams("columnName");
    String columnIndex = request.queryParams("columnIndex");
    String attribute = request.queryParams("attribute");

    Map<String, Object> responseMap = new HashMap<>();

    Parser<List<String>> parser = this.loadHandler.getCSVParser();
    if (parser == null) {
      responseMap.put("result", "error_data_not_loaded");
      responseMap.put("message", "Load was not called before search");
      return adapter.toJson(responseMap);
    }
    this.search = new Search<>(parser, new GeneralCreatorFromRow());
    List<List<String>> results;

    if (attribute == null) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("message", "Attribute to search by was not given.");
      return adapter.toJson(responseMap);
    } else if (columnName != null && columnIndex != null) {
      responseMap.put("result", "error_bad_request");
      responseMap.put(
          "message", "Both column name and number were given. Please only include one.");
      return adapter.toJson(responseMap);
    } else if (columnName != null) {
      try {
        results = this.search.search(columnName, attribute);
        responseMap.put("result", "success");
        responseMap.put("data", results);
        responseMap.put("column_searched", columnName);
        return adapter.toJson(responseMap);
      } catch (Exception e) {
        responseMap.put("result", "error_bad_request");
        responseMap.put("message", e.getMessage());
        return adapter.toJson(responseMap);
      }
    } else if (columnIndex != null) {
      try {
        results = this.search.search(Integer.parseInt(columnIndex), attribute);
        responseMap.put("result", "success");
        responseMap.put("data", results);
        responseMap.put("column_searched", columnIndex);
        return adapter.toJson(responseMap);
      } catch (Exception e) {
        responseMap.put("result", "error_bad_request");
        responseMap.put("message", e.getMessage());
        return adapter.toJson(responseMap);
      }
    }
    try {
      results = this.search.search(attribute);
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("message", e.getMessage());
      return adapter.toJson(responseMap);
    }
    responseMap.put("result", "success");
    responseMap.put("data", results);
    return adapter.toJson(responseMap);
  }
}
