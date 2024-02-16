package edu.brown.cs.student.main.csvparser.creators;

import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import java.util.List;

/**
 * This interface defines a method that allows your CSV parser to convert each row into an object of
 * some arbitrary passed type.
 */
public interface CreatorFromRow<T> {
  T create(List<String> row) throws FactoryFailureException;

  List<T> containsAttribute(List<T> element, int attribute, String value);

  List<T> contains(List<T> element, String value);
}
