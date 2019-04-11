To run the jar file.

Navigate to this directory in the command line and run the following command.
java -jar stockemulator.jar (This runs the jar on default options)

This application takes in two string arguments like:
java -jar stockemulator.jar <arg1> <arg2>

<arg1> Represents different data sources:
alphavantage - this uses data from the alphavantage API
mock - Uses mock data.

<arg2> Represents the UI type of this application:
cli - runs the application with a command line interface.
gui - runs the application with a graphical user interface


Sample commands:
-To run with alphavantage as data source run the following command.
java -jar stockemulator.jar alphavantage

-To run with mock data(AAPL and GOOGL are supported ticker) run the following command.
java -jar stockemulator.jar mock

-To run the GUI application
java -jar stockemulator.jar alphavantage GUI

-To run the CLI application
java -jar stockemulator.jar alphavantage CLI

If no arguments are specified, the application will use alphavantage as datasource and gui as the ui
 type.

This will only work on machines with JDK 1.8 or later.

We have provided a file with filename "second.json" in this folder, this is a previously saved portfolio to help with testing.