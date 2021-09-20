package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Stars {

  /**
   * Default constructor.
   */
  public Stars() {
  }

  /**
   * Parse input CSV into a List of Lists of Strings representing rows and columns
   * of StarID, ProperName, X, Y, Z.
   *
   * @param fileName - command lines arguments
   * @return - List of Lists of Strings
   */
  public List<List<String>> parseCSV(String fileName) throws IOException {
    List<List<String>> records = new ArrayList<>();
    String delim = ",";
    String line;
    BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
    // get one line from csv, split data by comma, store
    // read each line
    while ((line = fileReader.readLine()) != null) {
      // ignore the schema (first row)
      if (line.contains("StarID")) {
        continue;
      }
      String[] row = line.split(delim);
      records.add(Arrays.asList(row));
    }
    System.out.println("Read " + records.size() + " stars from " + fileName);
    return records;
  }

  public static ArrayList<String> kNearest(String[] arguments, List<List<String>> records,
                                           MathBot math) {
    // find root_star
    List<String> rootStar = new ArrayList<>();
    // if input format is K, coords
    if (arguments.length == 5) {
      rootStar = new ArrayList<>() {
        {
          add("");
          add("");
          add(arguments[2]);
          add(arguments[3]);
          add(arguments[4]);
        }
      };
      // if input format is K, starName
    } else {
      // handle spaces in name of input star e.g. "Lonely Star"
      // by concatenating
      StringBuilder starName = new StringBuilder("");
      // if name is multiple words long, separate by spaces
      for (int i = 2; i < arguments.length; i++) {
        starName.append(arguments[i]);
        if (arguments.length > 3 && i != arguments.length - 1) {
          starName.append(" ");
        }
      }
      for (List<String> row : records) {
        // if the proper name of this star (row) is equal to the input, we've found the star
        if (row.get(1).equals(starName.toString().replace("\"", ""))) {
          rootStar = row;
        }
      }
    }
    // find euclidean distance between input star and all other stars in the dataset
    for (List<String> row : records) {
      double distToRoot = math.eucDistanceBetween(rootStar, row);
      // add distance to this row (star) at index 1, overwriting proper name
      row.set(1, Double.toString(distToRoot));
      // row.add(Double.toString(distToRoot));
    }
    // sort stars (records) in ascending order using the eucDistance col
    records.sort(Comparator.comparingDouble(row -> Double.parseDouble(row.get(1))));
    ArrayList<String> nearestStars = new ArrayList<>();
    // extract first K stars (those with the shortest distances)
    double kNearest = Double.parseDouble(arguments[1]);
    // if format is K, coords
    if (arguments.length == 5) {
      for (int i = 0; i < kNearest; i++) {
        nearestStars.add(records.get(i).get(0));
      }
    } else {
      // if format is K, properName
      for (int i = 0; i < kNearest + 1 && i < records.size(); i++) {
        // skip rootStar if in dataset
        if (rootStar.get(0).equals(records.get(i).get(0))) {
          continue;
        }
        nearestStars.add(records.get(i).get(0));
      }
    }
    return nearestStars;
  }
}
