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

public class CachingCensusDataSource implements CensusDataSource {

  private final LoadingCache<List<String>, List<List<String>>> cache;

  public CachingCensusDataSource() {
    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(1, TimeUnit.MINUTES)
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

  @Override
  public Map<String, Object> getCensusData(String state, String county) {
    Map<String, Object> responseMap = new HashMap<>();
    List<String> arguments = Arrays.asList(state, county);
    List<List<String>> results;
    try {
      results = this.cache.getUnchecked(arguments);
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
    responseMap.put("percentage", results);
    return responseMap;
  }
}
