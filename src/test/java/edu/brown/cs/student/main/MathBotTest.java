package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MathBotTest {

  @Test
  public void testAddition() {
    MathBot matherator9000 = new MathBot();
    double output = matherator9000.add(10.5, 3);
    assertEquals(13.5, output, 0.01);
  }

  @Test
  public void testLargerNumbers() {
    MathBot matherator9001 = new MathBot();
    double output = matherator9001.add(100000, 200303);
    assertEquals(300303, output, 0.01);
  }

  @Test
  public void testSubtraction() {
    MathBot matherator9002 = new MathBot();
    double output = matherator9002.subtract(18, 17);
    assertEquals(1, output, 0.01);
  }

  @Test
  public void testZero() {
    MathBot matherator9003 = new MathBot();
    double output = matherator9003.subtract(1, 1);
    assertEquals(0, output, 0.01);
  }

  @Test
  public void testNegative() {
    MathBot matherator9004 = new MathBot();
    double output = matherator9004.subtract(1, 2);
    assertEquals(-1, output, 0.01);
  }

  @Test
  public void testDecimal() {
    MathBot matherator9005 = new MathBot();
    double output = matherator9005.add(1.5, 2);
    assertEquals(3.5, output, 0.01);
  }

  @Test
  public void testBigNumber() {
    MathBot matherator9006 = new MathBot();
    double output = matherator9006.add(999999, 1);
    assertEquals(1000000, output, 0.01);
  }

  @Test
  public void testEuclideanOne() {
    MathBot matherator9007 = new MathBot();
    List<String> row1 = new ArrayList<>() {
      {
        add("1");
        add("Lonely Star");
        add("5");
        add("-2.24");
        add("10.04");
      }
    };
    double output = matherator9007.eucDistanceBetween(row1, row1);
    assertEquals(0, output, 0.01);
  }

  @Test
  public void testEuclideanTwo() {
    MathBot matherator9008 = new MathBot();
    List<String> row1 = new ArrayList<>() {
      {
        add("0");
        add("Sol");
        add("0");
        add("0");
        add("0");
      }
    };

    List<String> row2 = new ArrayList<>() {
      {
        add("1");
        add("");
        add("282.43485");
        add("0.00449");
        add("5.36884");
      }
    };
    double output = matherator9008.eucDistanceBetween(row1, row2);
    assertEquals(282.485, output, 0.01);
  }

  @Test
  public void testEuclideanZeroDist() {
    MathBot matherator9009 = new MathBot();
    List<String> row1 = new ArrayList<>() {
      {
        add("0");
        add("Sol");
        add("0");
        add("0");
        add("0");
      }
    };

    List<String> row2 = new ArrayList<>() {
      {
        add("0");
        add("Sol");
        add("0");
        add("0");
        add("0");
      }
    };
    double output = matherator9009.eucDistanceBetween(row1, row2);
    assertEquals(0, output, 0.01);
  }

  @Test
  public void testEuclideanTwoNonZero() {
    MathBot matherator9010 = new MathBot();
    List<String> row1 = new ArrayList<>() {
      {
        add("2");
        add("");
        add("43.04329");
        add("0.00285");
        add("-15.24144");
      }
    };

    List<String> row2 = new ArrayList<>() {
      {
        add("3");
        add("");
        add("277.11358");
        add("0.02422");
        add("223.27753");
      }
    };
    double output = matherator9010.eucDistanceBetween(row1, row2);
    assertEquals(334.18587, output, 0.01);
  }
}
