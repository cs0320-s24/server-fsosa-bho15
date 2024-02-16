package edu.brown.cs.student.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.brown.cs.student.main.datasource.CachingACSDataSource;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * testing class for cache functionality and expected behavior
 */
public class CacheTest {

  @Test
  public void testCacheMiss() {
    CachingACSDataSource dataSource = new CachingACSDataSource(true, 10, 1, true);

    String state = "Rhode Island";
    String county = "Providence";
    Map<String, Object> results = dataSource.getCensusData(state, county);

    assertEquals(county.length() + "", results.get("percentage"));
    assertEquals(0, dataSource.getCache().stats().hitCount());
    assertEquals(1, dataSource.getCache().stats().missCount());
    assertEquals(1, dataSource.getCache().stats().loadSuccessCount());
  }

  @Test
  public void testCacheHit() {
    CachingACSDataSource dataSource = new CachingACSDataSource(true, 10, 1, true);

    dataSource.getCensusData("Rhode Island", "Providence");
    dataSource.getCensusData("Rhode Island", "Providence");

    assertEquals(1, dataSource.getCache().stats().hitCount());
    assertEquals(1, dataSource.getCache().stats().missCount());
    assertEquals(1, dataSource.getCache().stats().loadSuccessCount());
  }

  @Test
  public void testCacheUnload() {
    CachingACSDataSource dataSource = new CachingACSDataSource(true, 1, 1, true);

    dataSource.getCensusData("Rhode Island", "Providence");
    dataSource.getCensusData("Massachusetts", "Middlesex");
    dataSource.getCensusData("Rhode Island", "Providence");

    assertEquals(0, dataSource.getCache().stats().hitCount());
    assertEquals(3, dataSource.getCache().stats().missCount());
    assertEquals(3, dataSource.getCache().stats().loadSuccessCount());
  }

  @Test
  public void testNoCache() {
    CachingACSDataSource dataSource = new CachingACSDataSource(false, 0, 0, true);

    dataSource.getCensusData("Rhode Island", "Providence");
    dataSource.getCensusData("Rhode Island", "Providence");

    assertEquals(0, dataSource.getCache().stats().hitCount());
    assertEquals(2, dataSource.getCache().stats().missCount());
    assertEquals(2, dataSource.getCache().stats().loadSuccessCount());
  }
}
