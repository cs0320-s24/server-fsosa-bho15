package edu.brown.cs.student.main.csvparser.functional;

import edu.brown.cs.student.main.csvparser.creators.CreatorFromRow;
import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The Parser class is in charge of converting the .csv file into a List of an object. The format
 * that this object takes is dictated by the implementation used of the CreatorFromRow interface.
 */
public class Parser<T> {

  private static final Pattern regexSplitCSVRow =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");
  private final BufferedReader bufferedReader;
  private final List<String> header;

  /**
   * Takes in a Reader object to parse.
   *
   * @param reader Reader object
   * @param isHeader boolean to inform about header status
   * @throws IOException if there are issues reading the file
   */
  public Parser(Reader reader, boolean isHeader) throws IOException {
    this.bufferedReader = new BufferedReader(reader);
    if (isHeader) {
      this.header = this.readHeader();
    } else {
      this.header = null;
    }
  }

  /** Reads in the header if required. */
  public List<String> readHeader() throws IOException {
    String row = this.bufferedReader.readLine();
    return Arrays.asList(regexSplitCSVRow.split(row));
  }

  /** Getter for the header. */
  public List<String> getHeader() {
    return this.header;
  }

  /**
   * Converts the .csv into a list of objects, first splitting the list through the regex and then
   * passing it to the create() method of CreatorFromRow.
   *
   * @param creatorFromRow interface that dictates how we parse each row into an object
   * @return a list of objects
   * @throws IOException for reading issues within the file
   * @throws FactoryFailureException for issues related to transformation of rows into object
   */
  public List<T> parse(CreatorFromRow<T> creatorFromRow)
      throws IOException, FactoryFailureException {
    List<T> points = new ArrayList<>();
    String row = bufferedReader.readLine();
    while (row != null) {
      List<String> attributes = Arrays.asList(regexSplitCSVRow.split(row));
      if (this.header != null && attributes.size() != this.header.size()) {
        throw new FactoryFailureException("Row size did not match header size.", attributes);
      }
      T point = creatorFromRow.create(attributes);
      points.add(point);
      row = bufferedReader.readLine();
    }
    return points;
  }
}
