# README for Stars Project 0

# Design
The `Stars` class contains functionality to parse CSV files and compute kNearest neighbors.

## Methods
`Stars.parseCSV` takes a filename and returns a nested List of Strings - a 2D list representing rows and columns of the CSV.  
`Stars.kNearest` takes command line arguments, a nested List of Strings representing the parsed CSV, and a MathBot instance containing the `eucDistanceBetween` method which computes the straight line distance between two 3D coordinates (two stars).

## Testing
Primary tests for the `add` and `subtract` REPL commands are contained in `test/system` while tests for the `naive_neighbors` command are in `test/system/stars`.

Test cases include invalid (malformed) commands, multiple stars, missing quotes, input formats (name vs. coordinates) and no neighbors.

# Building

To build use:
`mvn package`

To run use:
`./run`

This will give you a barebones REPL, where you can enter text and you will be output at most 5 suggestions sorted alphabetically.

To start the server use:
`./run --gui [--port=<port>]`