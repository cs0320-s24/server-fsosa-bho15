package edu.brown.cs.student.main.census;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.List;

public class CensusAPIUtilities {
  public static CensusData deserializeCensus(List<List<String>> jsonCensus) {
    //    try {
    // Initializes Moshi
    Moshi moshi = new Moshi.Builder().build();

    // Initializes an adapter to a Census class then uses it to parse the JSON.
    JsonAdapter<CensusData> adapter = moshi.adapter(CensusData.class);

    return new CensusData(0);
    //    }
    // Returns an empty census... Probably not the best handling of this error case...
    // Notice an alternative error throwing case to the one done in OrderHandler. This catches
    // the error instead of pushing it up.
    //    catch (IOException e) {
    //      e.printStackTrace();
    //      return new CensusData(0.0);
    //    }
  }
}
