package edu.brown.cs.student.main.datasource;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.exceptions.BadRequestException;
import edu.brown.cs.student.main.exceptions.DataSourceException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

/**
 * This class serves as a proxy class that makes API requests to the ACS website if necessary,
 * and if not necessary takes the result from the cache.
 */
public class CachingCensusDataSource implements CensusDataSource {

  private final LoadingCache<List<String>, List<List<String>>> cache;

  /**
   * The constructor takes in options for how to set up the cache, and uses those options to create it.
   *
   * @param useCache whether or not the developer wants to cache at all.
   * @param maxSize the maximum number of entries in the cache at a given time.
   * @param minutesBeforeRemoval the number of minutes an entry in the cache will stay.
   */
  public CachingCensusDataSource(boolean useCache, int maxSize, int minutesBeforeRemoval) {
    if (!useCache) {
      maxSize = 0;
      minutesBeforeRemoval = 0;
    }
    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(maxSize)
            .expireAfterWrite(minutesBeforeRemoval, TimeUnit.MINUTES)
            .recordStats()
            .build(
                new CacheLoader<>() {
                  @NotNull
                  @Override
                  public List<List<String>> load(@NotNull List<String> strings)
                      throws DataSourceException, BadRequestException, IOException {
                    return DataSource.accessAPI(strings.get(0), strings.get(1));
                  }
                });
  }

  /**
   * Fills in a response map, either by accessing the cache or by making a new API request.
   * @param state a String representing the state name.
   * @param county a String representing the county name.
   * @return a map that describes our results.
   */
  @Override
  public Map<String, Object> getCensusData(String state, String county) {
    Map<String, Object> responseMap = new HashMap<>();
    List<String> arguments = Arrays.asList(state, county);
    List<List<String>> results;
    String percentage;
    try {
      results = this.cache.getUnchecked(arguments);
      percentage = results.get(1).get(1);
      double percent = Double.parseDouble(percentage);
      if (percent < 0.0 || percent > 100.0) {
        throw new DataSourceException("Percentage is incorrectly reported on census API.");
      }
    } catch (Exception e) {
      // TODO: make the error specific
      responseMap.put("result", "error");
      responseMap.put("message", e.getMessage());
      return responseMap;
    }
    responseMap.put("result", "success");
    responseMap.put("time", new Date());
    responseMap.put("state", state);
    responseMap.put("county", county);
    responseMap.put("percentage", percentage);
    return responseMap;
  }
}
