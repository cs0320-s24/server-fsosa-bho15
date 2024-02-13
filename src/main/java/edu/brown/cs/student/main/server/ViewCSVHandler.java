package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.csvparser.creators.Creator;
import edu.brown.cs.student.main.csvparser.functionalclasses.CSVParser;
import edu.brown.cs.student.main.exceptions.DataSourceException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ViewCSVHandler implements Route {

  private CSVParser<List<String>> csvParser;
  private List<List<String>> csv;

  public ViewCSVHandler() {
    this.csvParser = null;
    this.csv = null;
  }

  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    if (this.csvParser == null) {
      responseMap.put("result", "error_data_not_loaded");
      return responseMap;
    } else if (this.csv == null) {
      // TODO: handle any error that happens on parse
      this.csv = this.csvParser.parse();
    }
    responseMap.put("result", "success");
    responseMap.put("csv", this.csv);
    return responseMap;
  }

  public void load(String filepath) throws DataSourceException {
    try (FileReader fileReader = new FileReader("data/" + filepath);
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      this.csvParser = new CSVParser<>(bufferedReader, new Creator());
    } catch (IOException e) {
      throw new DataSourceException(e.getMessage(), e);
    }
  }
}
