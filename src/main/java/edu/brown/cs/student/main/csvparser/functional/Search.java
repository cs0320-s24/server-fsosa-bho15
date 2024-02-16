package edu.brown.cs.student.main.csvparser.functional;

import edu.brown.cs.student.main.csvparser.creators.CreatorFromRow;
import edu.brown.cs.student.main.exceptions.MissingAttributeException;
import java.util.List;

/**
 * The Search class represents the search functionality. It contains the ability to search by value
 * only, value and attribute, or value and attribute index.
 */
public class Search<T> {

  private final Parser<T> parser;
  private final CreatorFromRow<T> creatorFromRow;
  private List<String> header;

  /**
   * Search constructor takes in a parser and a creatorFromRow
   *
   * @param parser able to convert rows into objects
   * @param creatorFromRow serves as a utility for matching
   */
  public Search(Parser<T> parser, CreatorFromRow<T> creatorFromRow) {
    this.parser = parser;
    this.creatorFromRow = creatorFromRow;
    this.header = null;
  }

  /**
   * Searches by attribute and value. Returns only the examples where the given attribute matches
   * the given value.
   *
   * @param attribute String that defines the attribute from the header being searched for
   * @param value value of the attribute being searched for
   * @return a list of matches
   * @throws MissingAttributeException if the attribute is not present in the file
   */
  public List<T> search(String attribute, String value, List<T> csv)
      throws MissingAttributeException {
    this.header = this.parser.getHeader();
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
    return this.creatorFromRow.containsAttribute(csv, attributeIndex, value);
  }

  /**
   * Searches by attribute index and value. Returns only the examples where the given attribute
   * matches the given value.
   *
   * @param attributeIndex int that defines the attribute from the header being searched for
   * @param value value of the attribute being searched for
   * @return a list of matches
   */
  public List<T> search(int attributeIndex, String value, List<T> csv)
      throws IndexOutOfBoundsException {
    this.header = this.parser.getHeader();
    if (attributeIndex < 0 || attributeIndex >= this.header.size()) {
      throw new IndexOutOfBoundsException(attributeIndex);
    }
    return this.creatorFromRow.containsAttribute(csv, attributeIndex, value);
  }

  /**
   * Searches all objects and attributes for a given value.
   *
   * @param value value being searched for
   * @return list of matches
   */
  public List<T> search(String value, List<T> csv) {
    this.header = this.parser.getHeader();
    return this.creatorFromRow.contains(csv, value);
  }
}
