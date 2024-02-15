package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.csvparser.creators.GeneralCreatorFromRow;
import edu.brown.cs.student.main.csvparser.functional.Parser;
import edu.brown.cs.student.main.exceptions.APIException;
import edu.brown.cs.student.main.exceptions.DataSourceException;
import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadCSVHandler implements Route {

  private Parser<List<String>> parser;
  private List<List<String>> csv;

  public LoadCSVHandler() {
    this.parser = null;
    this.csv = null;
  }

  @Override
  public Object handle(Request request, Response response) {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    String filepath = request.queryParams("filepath");
    Map<String, Object> responseMap = new HashMap<>();
    try {
      this.load(filepath);
    } catch (APIException e) {
      responseMap.put("result", e.getErrorCode());
      responseMap.put("message", e.getMessage());
      return adapter.toJson(responseMap);
    }
    responseMap.put("result", "success");
    responseMap.put("filepath", filepath);
    return adapter.toJson(responseMap);
  }

  private void load(String filepath) throws DataSourceException {
    try {
      FileReader fileReader = new FileReader("data/" + filepath);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      this.parser = new Parser<>(bufferedReader, true);
    } catch (IOException e) {
      throw new DataSourceException(e.getMessage(), e);
    }
  }

  public List<List<String>> getCSV() throws IOException, FactoryFailureException {
    if (this.csv == null) {
      this.csv = this.parser.parse(new GeneralCreatorFromRow());
    }
    return this.csv;
  }

  public Parser<List<String>> getCSVParser() {
    return this.parser;
  }
}
