package edu.brown.cs.student.main.csvparser.functionalclasses;

import edu.brown.cs.student.main.csvparser.creators.CreatorFromRow;
import edu.brown.cs.student.main.csvparser.creators.FactoryFailureException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class for parsing the CSV
 *
 * @param <T> generic type variable for the rows to be parsed into
 */
public class CSVParser<T> {
  private BufferedReader br;
  private CreatorFromRow<T> t;

  /**
   * constructor of Parser class, creates a buffered reader from the reader that is passed in
   *
   * @param r generic reader
   * @param type generic type that implements creatorfromrow interface
   */
  public CSVParser(Reader r, CreatorFromRow<T> type) {
    br = new BufferedReader(r);
    t = type;
  }

  /**
   * parse method which uses a regex to determine how to split the rows on commas. reads in from the
   * reader that is passed in using a buffered reader which allows for use of the readLine() method
   *
   * @return a list of the type passed into the constructor
   */
  public List<T> parse() {
    // TODO: remove catch blocks to throw error instead?
    Pattern regexSplitCSVRow = Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");
    String line = " ";
    List<T> ret = new ArrayList<>();
    while (line != null) {
      try {
        line = br.readLine();
      } catch (IOException e) {
        System.err.println("IO Error.");
        System.exit(0);
      }
      if (line != null) {
        String[] result = regexSplitCSVRow.split(line);
        try {
          ret.add(t.create(Arrays.asList(result)));
        } catch (FactoryFailureException e) {
          System.err.println("Factory Failure Error.");
          System.exit(0);
        }
      }
    }
    return ret;
  }
}
