package stockemulation.controller.commands;

import java.util.Scanner;

import stockemulation.controller.EmulatorCommand;
import stockemulation.model.ModelExtn;
import stockemulation.view.EmulatorView;

/**
 * An implementation of the {@link EmulatorCommand}. An instance of this class supports all
 * operations related to creating a portfolio. It takes in input from the user for the name of the
 * portfolio to be created. It creates a portfolio if there isn't one already with the same name
 * with the help of the model. This class lets the user know if creating a new portfolio  was
 * successful or not using the view.
 */
public class CreatePortfolioCommand implements EmulatorCommand {

  private Scanner scanner;

  /**
   * Create an instance of the CreatePortfolioCommand class. This instance allows you to enter
   * instructions from the commandline and provide necessary information to create a new portfolio.
   *
   * @param s an instance of Scanner class used to get user input.
   */
  public CreatePortfolioCommand(Scanner s) {
    if (s != null) {
      scanner = s;
    }
  }

  /**
   * This method creates a new portfolio. It asks the user to enter the name of the portfolio to be
   * created. It creates a new portfolio with the entered name unless there already exists another
   * portfolio with the same name. White spaces at the beginning and end of the name string are
   * ignored.
   *
   * @param model This method takes in an ModelExtn model to help perform the actions and generate a
   *              result.
   * @param view  The method also takes in an EmulatorView to notify the user on successful creation
   *              of a portfolio or the failure to create one as well as to provide prompts to get
   *              user input to perform actions.
   */
  @Override
  public void execute(ModelExtn model, EmulatorView view) {
    view.showMessage("Enter the name of the portfolio");
    boolean isCreatedSuccessfully = false;
    String name = scanner.next();
    if (name != null) {
      isCreatedSuccessfully = model.createPortfolio(name.trim());
    }
    if (isCreatedSuccessfully) {
      view.showMessage("Portfolio " + name.trim() + " created successfully");
    } else {
      view.showMessage("Operation failed");
    }
  }
}
