package edu.brown.cs.student.main.datasource;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class CachingCensusDataSource implements CensusDataSource {
  public CachingCensusDataSource() {
    LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .build(
            new CacheLoader<Key, Graph>() {
              public Graph load(Key key) throws AnyException {
                return createExpensiveGraph(key);
              }
            });

  }

  @Override
  public String getCensusData(String state, String county) {
    return null;
  }
}
