package stockemulation.controller.commands;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Scanner;

import stockemulation.model.ModelExtn;
import stockemulation.view.EmulatorView;

/**
 * An extension of the {@link CommonCommands} class. An instance of this class supports all
 * operations related to loading a strategy from a file. It takes in input from the user about the
 * absolute file path to a file that needs to be loaded It performs the load operation with the help
 * of the model and lets the user know if it was successful or not using the view.
 */
public class ReadStrategyFromFile extends CommonCommands {

  /**
   * Create an instance of the {@link ReadStrategyFromFile} class. This instance allows you
   * to enter the absolute file path of a file that contains strategy information for it to be
   * loaded into the application.
   *
   * @param scanner an instance of Scanner class used to get user input.
   */
  public ReadStrategyFromFile(Scanner scanner) throws IllegalArgumentException {
    super(scanner);
  }

  /**
   * This method Performs the Load file operation.  It takes in input from the user about the
   * absolute file path to a file that needs to be loaded It performs the load operation with the
   * help of the model and lets the user know if it was successful or not using the view.
   *
   * @param model This method takes in an ModelExtn model to help perform the actions and generate a
   *              result.
   * @param view  The method also takes in an EmulatorView to convey if the file was loaded
   *              successfully or not.
   */
  @Override
  public void execute(ModelExtn model, EmulatorView view) {
    String filePath = super.getFilePath(model, view);
    boolean isSuccessful = true;
    try {
      model.readStrategyFromFile(filePath);
    } catch (IllegalArgumentException | ParseException | IOException e) {
      isSuccessful = false;
      view.showMessage("Operation failed: " + e.getMessage());
    }
    if (isSuccessful) {
      view.showMessage("Successfully loaded from file");
    }
  }
}
