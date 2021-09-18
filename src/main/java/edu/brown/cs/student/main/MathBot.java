package edu.brown.cs.student.main;

import java.util.List;

public class MathBot {

  /**
   * Default constructor.
   */
  public MathBot() {

  }

  /**
   * Adds two numbers together.
   *
   * @param num1 the first number.
   * @param num2 the second number.
   * @return the sum of num1 and num2.
   */
  public double add(double num1, double num2) {
    return num1 + num2;
  }

  /**
   * Subtracts two numbers.
   *
   * @param num1 the first number.
   * @param num2 the second number.
   * @return the difference of num1 and num2.
   */
  public double subtract(double num1, double num2) {
    return num1 - num2;
  }

  /**
   * Find euclidean distance between two 3D points.
   * @param row1 the first star's 3D coords
   * @param row2 the second star's 3D coords
   * @return the euclidean distance between the two input points
   */
  public double eucDistanceBetween(List<String> row1, List<String> row2) {
    // convert coords from string to double
    double x1 = Double.parseDouble(row1.get(2));
    double y1 = Double.parseDouble(row1.get(3));
    double z1 = Double.parseDouble(row1.get(4));

    double x2 = Double.parseDouble(row2.get(2));
    double y2 = Double.parseDouble(row2.get(3));
    double z2 = Double.parseDouble(row2.get(4));

    // find euclidean distance
    return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
  }
}
