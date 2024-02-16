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

/** The LoadCSVHandler deals with the load endpoint. */
public class LoadCSVHandler implements Route {

  private Parser<List<String>> parser;
  private List<List<String>> csv;

  public LoadCSVHandler() {
    this.parser = null;
    this.csv = null;
  }

  /**
   * Takes in the filepath to access the request.
   *
   * @param request provided through the Browser.
   * @param response unused here.
   * @return responseMap with information about the load's success.
   */
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

  /**
   * Load method updates the parser that is contained within the LoadCSVHandler.
   *
   * @param filepath to the CSV.
   * @throws DataSourceException thrown if there is an error with the source.
   */
  private void load(String filepath) throws DataSourceException {
    try {
      FileReader fileReader = new FileReader("data/" + filepath);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      this.parser = new Parser<>(bufferedReader, true);
      this.csv = this.parser.parse(new GeneralCreatorFromRow());
    } catch (IOException | FactoryFailureException e) {
      throw new DataSourceException(e.getMessage(), e);
    }
  }

  /**
   * Grabs the CSV from the parser.
   *
   * @return the CSV stored within the class.
   * @throws IOException if there is an issue reading the file.
   * @throws FactoryFailureException if there is an issue parsing the file.
   */
  public List<List<String>> getCSV() throws IOException, FactoryFailureException {
    if (this.csv == null) {
      this.csv = this.parser.parse(new GeneralCreatorFromRow());
    }
    return this.csv;
  }

  /**
   * Gets the parser for use by the SearchCSVHandler.
   *
   * @return the parser.
   */
  public Parser<List<String>> getCSVParser() {
    return this.parser;
  }
}
