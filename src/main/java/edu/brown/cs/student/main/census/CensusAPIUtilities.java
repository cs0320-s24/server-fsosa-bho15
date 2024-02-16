package edu.brown.cs.student.main.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import okio.Buffer;

/** Class containing functionality to deserialize a JSON into Java objects. */
public class CensusAPIUtilities {

  /**
   * Turns a JSON string into java objects using Moshi.
   *
   * @param jsonCensus Buffer to JSON output from ACS API.
   * @return List of items returned by the API.
   * @throws IOException if there is an issue reading the String.
   */
  public static List<List<String>> deserializeCensus(Buffer jsonCensus) throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    Type type =
        Types.newParameterizedType(
            List.class, Types.newParameterizedType(List.class, String.class));
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(type);
    return adapter.fromJson(jsonCensus);
  }
}
