package edu.brown.cs.student.main.loadcsv;

public class LoadCSV {
  private final String filepath;

  public LoadCSV(String filepath) {
    this.filepath = filepath;
  }

  @Override
  public String toString() {
    return this.filepath;
  }
}
