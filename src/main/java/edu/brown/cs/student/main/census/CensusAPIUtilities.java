package edu.brown.cs.student.main.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;

public class CensusAPIUtilities {
  public static Census deserializeCensus(String jsonCensus) {
    try {
      // Initializes Moshi
      Moshi moshi = new Moshi.Builder().build();

      // Initializes an adapter to a Census class then uses it to parse the JSON.
      JsonAdapter<Census> adapter = moshi.adapter(Census.class);

      Census census = adapter.fromJson(jsonCensus);

      return census;
    }
    // Returns an empty census... Probably not the best handling of this error case...
    // Notice an alternative error throwing case to the one done in OrderHandler. This catches
    // the error instead of pushing it up.
    catch (IOException e) {
      e.printStackTrace();
      return new Census();
    }
  }
}
