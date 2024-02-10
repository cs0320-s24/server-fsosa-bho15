package edu.brown.cs.student.test;

import static org.junit.jupiter.api.Assertions.*;

import edu.brown.cs.student.main.csvparser.Parser;
import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.objectCreators.GeneralCreatorFromRow;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

public class ParserTest {

  @Test
  public void WithHeaderTest() throws IOException, FactoryFailureException {
    // setup
    String filePath = "data/census/dol_ri_earnings_disparity.csv";
    Reader fileReader = new FileReader(filePath);
    Parser parser = new Parser(fileReader, true);
    parser.parse(new GeneralCreatorFromRow());

    List<String> expectedHeader =
        List.of(
            "State",
            "Data Type",
            "Average Weekly Earnings",
            "Number of Workers",
            "Earnings Disparity",
            "Employed Percent");
    assertEquals(parser.getHeader(), expectedHeader);
  }

  @Test
  public void WithoutHeaderTest() throws IOException, FactoryFailureException {
    // setup
    String filePath = "data/census/dol_ri_earnings_disparity_no_header.csv";
    Reader fileReader = new FileReader(filePath);
    Parser parser = new Parser(fileReader, false);
    parser.parse(new GeneralCreatorFromRow());

    assertNull(parser.getHeader());
  }

  @Test
  public void StringReaderTest() throws IOException, FactoryFailureException {
    // setup
    Reader stringReader =
        new StringReader(
            "State,Data Type,Average Weekly Earnings,Number of Workers,Earnings Disparity,Employed Percent\n"
                + "RI,White,\" $1,058.47 \",395773.6521, $1.00 ,75%\n"
                + "RI,Black, $770.26 ,30424.80376, $0.73 ,6%\n"
                + "RI,Native American/American Indian, $471.07 ,2315.505646, $0.45 ,0%\n"
                + "RI,Asian-Pacific Islander,\" $1,080.09 \",18956.71657, $1.02 ,4%\n"
                + "RI,Hispanic/Latino, $673.14 ,74596.18851, $0.64 ,14%\n"
                + "RI,Multiracial, $971.89 ,8883.049171, $0.92 ,2%");
    Parser parser = new Parser(stringReader, true);
    List<List<String>> points = parser.parse(new GeneralCreatorFromRow());

    assertEquals(6, points.size());
  }

  @Test
  public void FileReaderTest() throws IOException, FactoryFailureException {
    // setup
    String filePath = "data/census/dol_ri_earnings_disparity.csv";
    Reader fileReader = new FileReader(filePath);
    Parser parser = new Parser(fileReader, true);
    List<List<String>> points = parser.parse(new GeneralCreatorFromRow());

    assertEquals(6, points.size());
  }

  @Test
  public void InconsistentRowsTest() throws IOException, FactoryFailureException {
    // setup
    String filePath = "data/census/dol_ri_earnings_disparity_incomplete.csv";
    Reader fileReader = new FileReader(filePath);
    // we lie! we say that there is no header even if there is. This allows us to check that
    // we read in misaligned data just fine if the user does not specify a header
    Parser parser = new Parser(fileReader, false);
    List<List<String>> points = parser.parse(new GeneralCreatorFromRow());

    assertEquals(3, points.size());
    assertEquals(6, points.get(0).size());
    assertEquals(6, points.get(1).size());
    assertEquals(5, points.get(2).size());
  }

  @Test
  public void RowMismatchTest() throws IOException {
    // setup
    String filePath = "data/malformed/malformed_signs.csv";
    Reader fileReader = new FileReader(filePath);
    Parser parser = new Parser(fileReader, true);
    assertThrows(
        FactoryFailureException.class,
        () -> {
          parser.parse(new GeneralCreatorFromRow());
        });
  }

  @Test
  public void RegExTest() {
    final Pattern regexSplitCSVRow =
        Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");
    String testRow = "Caesar, \"Julius, he wrote. He said, \"hello!\"\", \"veni, vidi, vici\"";
    List<String> split = Arrays.asList(regexSplitCSVRow.split(testRow));
    System.out.println(split);
    System.out.println(split.size());
  }

  // Not writing a bad directory test because that is handled in main
}
