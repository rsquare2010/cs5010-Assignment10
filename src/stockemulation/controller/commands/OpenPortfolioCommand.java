package stockemulation.controller.commands;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import stockemulation.model.ModelExtn;
import stockemulation.view.EmulatorView;

/**
 * An extension of the {@link CommonCommands} class. It supports operations to get a brief overview
 * of the contents of a particular portfolio. It displays the list of existing portfolio and asks
 * the user to pick the portfolio whose summary will be displayed. The summary of a portfolio is the
 * unique stocks under it and the quantity of each stock.
 */
public class OpenPortfolioCommand extends CommonCommands {

  /**
   * Create an instance of the DetailPortfolioCommand class. This instance allows you to enter
   * instructions from the commandline to get an overview of the contents of an existing portfolio.
   *
   * @param scanner an instance of Scanner class used to get user input.
   */
  public OpenPortfolioCommand(Scanner scanner) {
    super(scanner);
    if (scanner != null) {
      this.scanner = scanner;
    }
  }

  /**
   * This method Provides the summary of the contents of a portfolio on a given date. When the user
   * wants to check the summary of a portfolio, It asks the user to pick the portfolio whose
   * information will be displayed. This operation cannot be performed if a portfolio does not
   * already exist.
   *
   * @param model This method takes in an Emulator model to help perform the actions and generate a
   *              result.
   * @param view  The method also takes in an EmulatorView to display the summary of a portfolio to
   *              the user as well as to provide prompts to get user input to perform actions.
   */
  @Override
  public void execute(ModelExtn model, EmulatorView view) {
    int portfolioNumber = super.choosePortfolio(model, view);
    if (portfolioNumber == -1) {
      return;
    }
    String message;
    try {
      Map<String, Double> map = model.getPortfolioDetails(portfolioNumber);
      Set entries = map.entrySet();
      Iterator entriesIterator = entries.iterator();
      StringBuilder portfolioSummaryBuilder = new StringBuilder();
      int i = 0;
      while (entriesIterator.hasNext()) {

        Map.Entry mapping = (Map.Entry) entriesIterator.next();
        portfolioSummaryBuilder.append(i + 1);
        portfolioSummaryBuilder.append("\t");
        portfolioSummaryBuilder.append(mapping.getKey().toString());
        portfolioSummaryBuilder.append("\t");
        portfolioSummaryBuilder.append(mapping.getValue().toString());
        portfolioSummaryBuilder.append("\n");

        i++;
      }
      message = portfolioSummaryBuilder.toString();
    } catch (IllegalArgumentException e) {
      message = "Something went wrong";
    }
    view.showMessage(message);
  }
}
