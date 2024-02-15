package edu.brown.cs.student.main.server;

<<<<<<< Updated upstream
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
=======
import edu.brown.cs.student.main.csvparser.creators.Creator;
import edu.brown.cs.student.main.csvparser.functionalclasses.CSVParser;
import edu.brown.cs.student.main.exceptions.DataSourceException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
>>>>>>> Stashed changes
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ViewCSVHandler implements Route {

<<<<<<< Updated upstream
  private final LoadCSVHandler loadHandler;

  public ViewCSVHandler(LoadCSVHandler loadHandler) {
    this.loadHandler = loadHandler;
=======
  private CSVParser<List<String>> csvParser;
  private List<List<String>> csv;
  public ViewCSVHandler() {
    this.csvParser = null;
    this.csv = null;
>>>>>>> Stashed changes
  }

  @Override
  public Object handle(Request request, Response response) {
<<<<<<< Updated upstream
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
=======
    Map<String, Object> responseMap = new HashMap<>();
    if (this.csvParser == null) {
      responseMap.put("result", "error_data_not_loaded");
      return responseMap;
    } else if (this.csv == null) {
      //TODO: handle any error that happens on parse
      this.csv = this.csvParser.parse();
    }
    responseMap.put("result", "success");
    return this.csv;
  }

  public void load(String filepath) throws DataSourceException {
    try (FileReader fileReader = new FileReader("data/" + filepath);
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      this.csvParser = new CSVParser<>(bufferedReader, new Creator());
    } catch (IOException e) {
      throw new DataSourceException(e.getMessage(), e);
    }
>>>>>>> Stashed changes
  }
}
