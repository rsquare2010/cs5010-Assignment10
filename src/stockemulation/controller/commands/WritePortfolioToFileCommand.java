package stockemulation.controller.commands;

import java.io.IOException;
import java.util.Scanner;

import stockemulation.model.ModelExtn;
import stockemulation.view.EmulatorView;

/**
 * An extension of the {@link CommonCommands} class. An instance of this class supports all
 * operations related to writing a portfolio to a file. It takes in input from the user about the
 * absolute file path to which the file that needs to be written. It performs the write operation
 * with the help of the model and lets the user know if it was successful or not using the view.
 */
public class WritePortfolioToFileCommand extends CommonCommands {

  /**
   * Create an instance of the {@link WritePortfolioToFileCommand} class. This instance allows you
   * to enter the absolute file path and file name to which the selected portfolio will be
   * written for persistence.
   *
   * @param scanner an instance of Scanner class used to get user input.
   */
  public WritePortfolioToFileCommand(Scanner scanner) {
    super(scanner);
  }

  /**
   * This method Performs the write to file operation. It takes in input from the user about the
   * absolute file path to which the file that needs to be written. It performs the write operation
   * with the help of the model and lets the user know if it was successful or not using the view.
   *
   * @param model This method takes in an ModelExtn model to help perform the actions and generate a
   *              result.
   * @param view  The method also takes in an EmulatorView to convey if the portfolio was
   *              successfully written to a file or not.
   */
  @Override
  public void execute(ModelExtn model, EmulatorView view) {

    int portfolioNumber = super.choosePortfolio(model, view);
    if (portfolioNumber == -1) {
      return;
    }
    boolean isSuccess = true;

    String filePath = super.getFilePath(model, view);

    try {
      model.writePortfolioToFile( filePath,portfolioNumber);
    } catch (IOException e) {
      view.showMessage(e.getMessage());
      isSuccess = false;
    }
    if (isSuccess) {
      view.showMessage("File written successfully");
    }
  }
}
