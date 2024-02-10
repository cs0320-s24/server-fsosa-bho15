package edu.brown.cs.student.main.FunctionalClasses;

import java.util.List;

/**
 * search class used for going through the csv rows and checking if the target string is present
 *
 * @param <T> the type of row to search through, generic type
 */
public class Searcher<T> {
  private CSVParser<List<String>> csv;
  private String output;

  /**
   * searcher constructor which sets the parser that is passed in to the global parser variable
   *
   * @param csvparser parser passed in from wherever the parser is created
   */
  public Searcher(CSVParser<List<String>> csvparser) {
    csv = csvparser;
    output = "";
  }

  /**
   * search method that goes through the parsed csv row by row looking for the target string
   *
   * @param target string to be searched for
   * @param hasHeader boolean indicating whether the csv has headers or not
   * @param column column number or name to be searched in, empty string if was not provided
   * @param nameOrNum whether the column provided is a name or number, empty string if no column
   */
  public void search(String target, boolean hasHeader, String column, String nameOrNum) {
    boolean printed = false;
    List<List<String>> list = csv.parse();
    int colNumber = Integer.MIN_VALUE;
    if (!column.equals("") && !column.equals("all")) {
      int len = list.get(0).size();
      if (nameOrNum.equals("name")) {
        for (int i = 0; i < len; i++) {
          if (list.get(0).get(i).equals(column)) colNumber = i;
        }
      } else {
        try {
          colNumber = Integer.parseInt(column);
        } catch (NumberFormatException e) {
          System.err.println("Please enter a valid column number or column name");
          output = "Please enter a valid column number or column name";
          System.exit(0);
        }
      }
      if (colNumber == Integer.MIN_VALUE) {
        System.err.println("Error indicating column to search.");
        output = "Error indicating column to search.";
        System.exit(0);
      }
    }
    int i = 0;
    if (hasHeader) i++;
    while (i < list.size()) {
      if (colNumber == Integer.MIN_VALUE) {
        for (int j = 0; j < list.get(i).size(); j++) {
          if (list.get(i).get(j).contains(target)) {
            System.out.println(list.get(i));
            output += list.get(i);
            printed = true;
            break;
          }
        }
      } else {
        try {
          list.get(i).get(colNumber);
        } catch (ArrayIndexOutOfBoundsException e) {
          System.err.println("Please enter a valid column.");
          output = "Please enter a valid column.";
          System.exit(0);
        }
        if (list.get(i).get(colNumber).contains(target)) {
          output += list.get(i);
          System.out.println(list.get(i));
          printed = true;
        }
      }
      i++;
    }
    if (!printed) {
      System.out.println("No results found!");
      output = "No results found!";
    }
  }

  /**
   * getter method for the output. used in testing.
   *
   * @return output variable which stores the text printed to the command line.
   */
  public String getOutput() {
    return output;
  }
}
