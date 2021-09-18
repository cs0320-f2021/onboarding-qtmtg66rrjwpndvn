package edu.brown.cs.student.main;

import com.google.common.collect.ImmutableMap;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  // use port 4567 by default when running server
  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // set up parsing of command line flags
    OptionParser parser = new OptionParser();

    // "./run --gui" will start a web server
    parser.accepts("gui");

    // use "--port <n>" to specify what port on which the server runs
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);

    OptionSet options = parser.parse(args);
    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      List<List<String>> records = new ArrayList<>();
      String input;
      while ((input = br.readLine()) != null) {
        try {
          input = input.trim();
          String[] arguments = input.split(" ");
          MathBot math = new MathBot();
          switch (arguments[0]) {
            case "add":
              double resAdd =
                  math.add(Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2]));
              System.out.println(resAdd);
              break;
            case "subtract":
              double resSub =
                  math.subtract(Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2]));
              System.out.println(resSub);
              break;
            case "stars":
              // parse CSV
              String delim = ",";
              String line;
              BufferedReader fileReader = new BufferedReader(new FileReader(arguments[1]));
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
              System.out.println("Read " + records.size() + " stars from " + arguments[1]);
              break;
            case "naive_neighbors":
              // find root_star
              List<String> rootStar = new ArrayList<>();
              // if input format is K, starName
              if (arguments.length == 3) {
                for (List<String> row : records) {
                  // if the proper name of this star (row) is equal to the input, we've found the star
                  if (row.get(1).equals(arguments[2].replace("\"", ""))) {
                    rootStar = row;
                  }
                }
                // if input format is K, coords
              } else if (arguments.length == 5) {
                rootStar = new ArrayList<>() {
                  {
                    add("");
                    add("");
                    add(arguments[2]);
                    add(arguments[3]);
                    add(arguments[4]);
                  }
                };
              } else {
                // how handle spaces in name of input star? "Lonely Star" makes arguments array
                // have an extra 2 length instead of 1...
                break;
              }
              // find euclidean distance between input star and all other stars in the dataset

              // find distance between all other stars in the dataset and this star
              for (List<String> row : records) {
                // keep track of star by index
                double distToRoot = math.eucDistanceBetween(rootStar, row);
                // add distance to this row (star) at index 5 ==> overwritten name at index 1 since can't add
                row.set(1, Double.toString(distToRoot));
//                row.add(Double.toString(distToRoot));
              }
              // sort stars (records) in ascending order using the eucDistance col => using index 1 now instead of 5
              records.sort(Comparator.comparingDouble(row -> Double.parseDouble(row.get(1))));
              ArrayList<String> nearestStars = new ArrayList<>();
              // extract first K stars (those with the shortest distances)
              double kNearest = Double.parseDouble(arguments[1]);
              if (arguments.length == 3) {
                for (int i = 0; i < kNearest + 1 && i < records.size(); i++) {
                  // skip rootStar if in dataset
                  if (rootStar.get(0).equals(records.get(i).get(0))) {
                    continue;
                  }
                  nearestStars.add(records.get(i).get(0));
                }
              } else {
                for (int i = 0; i < kNearest; i++) {
                  nearestStars.add(records.get(i).get(0));
                }
              }
              for (String star : nearestStars) {
                System.out.println(star);
              }
              break;
            default:
              throw new Exception("Incorrect Command");
          }
        } catch (Exception e) {
          // e.printStackTrace();
          System.out.println("ERROR: We couldn't process your input");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("ERROR: Invalid input for REPL");
    }

  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration(Configuration.VERSION_2_3_0);

    // this is the directory where FreeMarker templates are placed
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    // set port to run the server on
    Spark.port(port);

    // specify location of static resources (HTML, CSS, JS, images, etc.)
    Spark.externalStaticFileLocation("src/main/resources/static");

    // when there's a server error, use ExceptionPrinter to display error on GUI
    Spark.exception(Exception.class, new ExceptionPrinter());

    // initialize FreeMarker template engine (converts .ftl templates to HTML)
    FreeMarkerEngine freeMarker = createEngine();

    // setup Spark Routes
    Spark.get("/", new MainHandler(), freeMarker);
  }

  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler<Exception> {
    @Override
    public void handle(Exception e, Request req, Response res) {
      // status 500 generally means there was an internal server error
      res.status(500);

      // write stack trace to GUI
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  /**
   * A handler to serve the site's main page.
   *
   * @return ModelAndView to render.
   * (main.ftl).
   */
  private static class MainHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      // this is a map of variables that are used in the FreeMarker template
      Map<String, Object> variables = ImmutableMap.of("title",
          "Go go GUI");

      return new ModelAndView(variables, "main.ftl");
    }
  }
}
