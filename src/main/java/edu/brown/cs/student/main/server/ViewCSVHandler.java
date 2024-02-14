package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ViewCSVHandler implements Route {

  private final LoadCSVHandler loadHandler;

  public ViewCSVHandler(LoadCSVHandler loadHandler) {
    this.loadHandler = loadHandler;
  }

  @Override
  public Object handle(Request request, Response response) {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);

    Map<String, Object> responseMap = new HashMap<>();
    if (this.loadHandler.getCSVParser() == null) {
      responseMap.put("result", "error_data_not_loaded");
      responseMap.put("message", "Data was not loaded properly before view request.");
      return adapter.toJson(responseMap);
    }
    List<List<String>> csv;
    try {
      csv = this.loadHandler.getCSV();
    } catch (Exception e) {
      responseMap.put("result", "error_datasource");
      responseMap.put("message", "Parser was not able to parse data loaded.");
      return adapter.toJson(responseMap);
    }
    responseMap.put("result", "success");
    responseMap.put("csv", csv);
    return adapter.toJson(responseMap);
  }
}