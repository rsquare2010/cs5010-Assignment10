package stockemulation.controller.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

import stockemulation.model.ModelExtn;
import stockemulation.view.EmulatorView;

/**
 * An extension of the {@link CommonCommands} class. It supports operations to find the cost basis
 * of the stocks as well as their value on a given date in a particular portfolio. When checked
 * on a date before any stocks were bought the cost price and value of the portfolio will be 0.
 * Stocks bought after the provided date will not be considered while providing this information.
 */
public class DetailPortfolioCommand extends CommonCommands {

  /**
   * Create an instance of the DetailPortfolioCommand class. This instance allows you to enter
   * instructions from the commandline to get detailed cost basis, value and stock specific of about
   * an existing portfolio.
   *
   * @param scanner an instance of Scanner class used to get user input.
   */
  public DetailPortfolioCommand(Scanner scanner) {
    super(scanner);
    if (scanner == null) {
      throw new IllegalArgumentException("scanner is null");
    }
  }

  /**
   * This method Provides the cost basis and total value of a portfolio on a given date. When the
   * user wants to check the cost basis and total value of a portfolio. It asks the user to pick the
   * portfolio whose information will be displayed. It then asks the following information from the
   * user: Date in YYYY-MM-DD format(the value of the portfolio on this date will be displayed).
   * This operation cannot be performed on future dates. When a date is provided it take the opening
   * price of the stock on that to determine the value of a stock on that day. This operation cannot
   * be performed if a portfolio does not already exist.
   *
   * @param model This method takes in an ModelExtn model to help perform the actions and generate a
   *              result.
   * @param view  The method also takes in an EmulatorView to display the cost basis and value of a
   *              portfolio to the user as well as to provide prompts to get user input to
   *              perform actions.
   */
  @Override
  public void execute(ModelExtn model, EmulatorView view) {
    int portfolioNumber = super.choosePortfolio(model, view);
    if (portfolioNumber == -1) {
      return;
    }
    LocalDate date = super.getDate(model, view);
    LocalTime time = LocalTime.of(15,00);
    LocalDateTime dateTime = LocalDateTime.of(date, time);

    StringBuilder messageBuilder = new StringBuilder();

    try {
      messageBuilder.append("Cost basis is: $");
      messageBuilder.append( "\t");
      messageBuilder.append(model.getCostBasis(portfolioNumber, dateTime));
      messageBuilder.append( "\n");
      messageBuilder.append("Value is: $");
      messageBuilder.append( "\t");
      messageBuilder.append(model.getTotalValue(portfolioNumber, dateTime));
    } catch (IllegalArgumentException e) {
      messageBuilder.append( "Something went wrong");
    }
    view.showMessage(messageBuilder.toString());
  }
}
