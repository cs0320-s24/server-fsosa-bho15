package edu.brown.cs.student.test;

import static org.junit.jupiter.api.Assertions.*;

import edu.brown.cs.student.main.CSVParser.Parser;
import edu.brown.cs.student.main.CSVParser.Search;
import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.exceptions.MissingAttributeException;
import edu.brown.cs.student.main.objectCreators.EarningsByRaceFromRow;
import edu.brown.cs.student.main.objectCreators.GeneralCreatorFromRow;
import java.io.*;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SearchTest {

  @Test
  public void ValuePresentTest() throws IOException, FactoryFailureException {
    // setup
    String filePath = "data/census/dol_ri_earnings_disparity.csv";
    Reader fileReader = new FileReader(filePath);
    Parser parser = new Parser(fileReader, true);
    Search search = new Search(parser, new GeneralCreatorFromRow());
    List<List<String>> matches = search.search("Black");

    assertEquals(1, matches.size());
  }

  @Test
  public void ValueMissingTest() throws IOException, FactoryFailureException {
    // setup
    String filePath = "data/census/dol_ri_earnings_disparity.csv";
    Reader fileReader = new FileReader(filePath);
    Parser parser = new Parser(fileReader, true);
    Search search = new Search(parser, new GeneralCreatorFromRow());
    List<List<String>> matches = search.search("African American");

    assertEquals(0, matches.size());
  }

  @Test
  public void ValuePresentByColumnTest()
      throws IOException, FactoryFailureException, MissingAttributeException {
    // setup
    String filePath = "data/census/dol_ri_earnings_disparity.csv";
    Reader fileReader = new FileReader(filePath);
    Parser parser = new Parser(fileReader, true);
    Search search = new Search(parser, new GeneralCreatorFromRow());
    List<List<String>> matches = search.search("Data Type", "Black");

    assertEquals(1, matches.size());
  }

  @Test
  public void ValuePresentDifferentColumnTest()
      throws IOException, FactoryFailureException, MissingAttributeException {
    // setup
    String filePath = "data/census/dol_ri_earnings_disparity.csv";
    Reader fileReader = new FileReader(filePath);
    Parser parser = new Parser(fileReader, true);
    Search search = new Search(parser, new GeneralCreatorFromRow());
    List<List<String>> matches = search.search("State", "Black");

    assertEquals(0, matches.size());
  }

  @Test
  public void SearchByColumnIndexPresentTest()
      throws IOException, FactoryFailureException, MissingAttributeException {
    // setup
    String filePath = "data/census/dol_ri_earnings_disparity.csv";
    Reader fileReader = new FileReader(filePath);
    Parser parser = new Parser(fileReader, true);
    Search search = new Search(parser, new GeneralCreatorFromRow());
    List<List<String>> matches = search.search(1, "Black");

    assertEquals(1, matches.size());
  }

  @Test
  public void SearchByColumnIndexAbsentTest()
      throws IOException, FactoryFailureException, MissingAttributeException {
    // setup
    String filePath = "data/census/dol_ri_earnings_disparity.csv";
    Reader fileReader = new FileReader(filePath);
    Parser parser = new Parser(fileReader, true);
    Search search = new Search(parser, new GeneralCreatorFromRow());
    List<List<String>> matches = search.search(1, "Car");

    assertEquals(0, matches.size());
  }

  @Test
  public void UseAlternativeCreatorFromRowTest() throws IOException, FactoryFailureException {
    // setup
    String filePath = "data/census/dol_ri_earnings_disparity.csv";
    Reader fileReader = new FileReader(filePath);
    Parser parser = new Parser(fileReader, true);
    Search search = new Search(parser, new EarningsByRaceFromRow());
    List<List<String>> matches = search.search(1, "Black");

    assertEquals(1, matches.size());
  }
}
