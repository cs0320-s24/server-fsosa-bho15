package edu.brown.cs.student.main;

import edu.brown.cs.student.main.csvparser.creators.Creator;
import edu.brown.cs.student.main.csvparser.functionalclasses.CSVParser;
import edu.brown.cs.student.main.csvparser.functionalclasses.Searcher;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    if (args.length < 3) {
      System.err.println(
          "Please enter the filename, target value, and 'true' if the csv has column headers. "
              + "Separate each argument with a space. You can also enter a column number or name as a 4th argument"
              + " to specify a column to search in. If you do this, specify whether it's a name or number"
              + "by typing 'name' or 'num' as the 5th argument.");
      System.exit(0);
    }
    if (args[0].charAt(0) == '.') {
      System.err.println("Don't begin the filepath with a period please.");
      System.exit(0);
    }
    boolean hasHeader = false;
    String column = "";
    String nameOrNum = "";
    if (args[1].equals("true")) hasHeader = true;
    else {
      if (!args[1].equals("false")) {
        System.err.println(
            "Please enter 'true' or 'false' to indicate whether the csv file contains headers");
        System.exit(0);
      }
    }
    if (args.length > 3) {
      column = args[3];
      if (args.length > 4) {
        if (!args[4].equals("name") && !args[4].equals("num") && hasHeader) {
          System.err.println(
              "Please enter 'name' or 'num' as the argument after the column to specify"
                  + "whether the column identifier is the column's name or number.");
          System.exit(0);
        } else {
          nameOrNum = args[4];
        }
      } else if (hasHeader) {
        System.err.println(
            "Please specify whether the column identifier is the name of the column or "
                + "column number");
        System.exit(0);
      } else {
        nameOrNum = "num";
      }
    }

    new Main(args).run(args[0], args[2], hasHeader, column, nameOrNum);
  }

  private Main(String[] args) {}

  /** Runs the parser */
  private void run(
      String filename, String target, boolean hasHeader, String column, String nameOrNum) {
    Reader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new FileReader("data/" + filename));
    } catch (FileNotFoundException e) {
      System.err.println(
          "Error: File not found. Make sure to include the directory it is in. For example, "
              + "stars/ten-star.csv");
      System.err.println(filename);
      System.exit(0);
    }
    Searcher<List<String>> searcher =
        new Searcher<>(new CSVParser<>(bufferedReader, new Creator()));
    searcher.search(target, hasHeader, column, nameOrNum);
  }
}
