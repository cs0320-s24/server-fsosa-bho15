package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.loadcsv.LoadCSV;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadCSVHandler implements Route {

  @Override
  public Object handle(Request request, Response response) {
    String filepath = request.queryParams("filepath");
    Map<String, Object> responseMap = new HashMap<>();
    File tmpDir = new File("data/" + filepath);
    boolean exists = tmpDir.exists();
    if (!exists) {
      // TODO: add more descriptive errors
      responseMap.put("result", "Exception");
    } else {
      //  TODO: do we even need this object?
      LoadCSV loadcsv = new LoadCSV(filepath);
      responseMap.put("result", "success");
      responseMap.put("filepath", filepath);
    }
    return responseMap;
  }
}
