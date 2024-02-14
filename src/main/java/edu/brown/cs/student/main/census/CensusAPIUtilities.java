package edu.brown.cs.student.main.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import okio.Buffer;

public class CensusAPIUtilities {
  public static List<List<String>> deserializeCensus(Buffer jsonCensus) throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    Type type =
        Types.newParameterizedType(
            List.class, Types.newParameterizedType(List.class, String.class));
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(type);
    return adapter.fromJson(jsonCensus);
  }
}
