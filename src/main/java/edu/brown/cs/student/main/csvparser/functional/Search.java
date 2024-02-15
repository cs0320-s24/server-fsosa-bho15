package edu.brown.cs.student.main.csvparser.functional;

import edu.brown.cs.student.main.csvparser.creators.CreatorFromRow;
import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.exceptions.MissingAttributeException;
import java.io.IOException;
import java.util.List;

/**
 * The Search class represents the search functionality. It contains the ability to search by value
 * only, value and attribute, or value and attribute index.
 */
public class Search<T> {

  private final Parser<T> parser;
  private final CreatorFromRow<T> creatorFromRow;
  private boolean isParsed;
  private List<String> header;
  private List<T> points;

  /**
   * Search constructor takes in a parser and a creatorFromRow
   *
   * @param parser able to convert rows into objects
   * @param creatorFromRow serves as a utility for matching
   */
  public Search(Parser<T> parser, CreatorFromRow<T> creatorFromRow) {
    this.parser = parser;
    this.creatorFromRow = creatorFromRow;
    this.isParsed = false;
    this.header = null;
    this.points = null;
  }

  /**
   * Searches by attribute and value. Returns only the examples where the given attribute matches
   * the given value.
   *
   * @param attribute String that defines the attribute from the header being searched for
   * @param value value of the attribute being searched for
   * @return a list of matches
   * @throws IOException for issues during parse
   * @throws FactoryFailureException for issues during conversion of row to object
   * @throws MissingAttributeException if the attribute is not present in the file
   */
  public List<T> search(String attribute, String value)
      throws IOException, FactoryFailureException, MissingAttributeException {
    // parse only if we have not parsed before
    if (!this.isParsed) {
      this.header = this.parser.getHeader();
      this.points = this.parser.parse(this.creatorFromRow);
      this.isParsed = true;
    }
    // get the index from the attribute
    int attributeIndex = -1;
    for (int i = 0; i < header.size(); i++) {
      if (header.get(i).equalsIgnoreCase(attribute)) {
        attributeIndex = i;
      }
    }
    if (attributeIndex == -1) {
      throw new MissingAttributeException("Attribute not found", attribute);
    }
    return this.creatorFromRow.containsAttribute(this.points, attributeIndex, value);
  }

  /**
   * Searches by attribute index and value. Returns only the examples where the given attribute
   * matches the given value.
   *
   * @param attributeIndex int that defines the attribute from the header being searched for
   * @param value value of the attribute being searched for
   * @return a list of matches
   * @throws IOException for issues during parse
   * @throws FactoryFailureException for issues during conversion of row to object
   */
  public List<T> search(int attributeIndex, String value)
      throws IOException, FactoryFailureException, IndexOutOfBoundsException {
    // parse only if we have not parsed before
    if (!this.isParsed) {
      this.header = this.parser.getHeader();
      this.points = this.parser.parse(this.creatorFromRow);
      this.isParsed = true;
    }
    // check that the index is within range
    if (attributeIndex < 0 || attributeIndex >= this.header.size()) {
      throw new IndexOutOfBoundsException(attributeIndex);
    }
    return this.creatorFromRow.containsAttribute(this.points, attributeIndex, value);
  }

  /**
   * Searches all objects and attributes for a given value.
   *
   * @param value value being searched for
   * @return list of matches
   * @throws IOException for issues during parse
   * @throws FactoryFailureException for issues during row conversion
   */
  public List<T> search(String value) throws IOException, FactoryFailureException {
    // parse only if we have not parsed before
    if (!this.isParsed) {
      this.header = this.parser.getHeader();
      this.points = this.parser.parse(this.creatorFromRow);
      this.isParsed = true;
    }
    return this.creatorFromRow.contains(this.points, value);
  }
}
