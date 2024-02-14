package edu.brown.cs.student.main.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import okio.Buffer;

public class CensusAPIUtilities {
  public static Census deserializeCensus(Buffer jsonCensus) {
    try {
      // Initializes Moshi
      Moshi moshi = new Moshi.Builder().build();

      // Initializes an adapter to a Census class then uses it to parse the JSON.
      JsonAdapter<Census> adapter = moshi.adapter(Census.class).nonNull();
      return adapter.fromJson(jsonCensus);
    } catch (IOException e) {
      e.printStackTrace();
      return new Census();
    }
  }
}
