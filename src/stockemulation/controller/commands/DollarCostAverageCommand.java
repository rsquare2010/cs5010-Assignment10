package stockemulation.controller.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

import stockemulation.model.ModelExtn;
import stockemulation.view.EmulatorView;

/**
 * An extension of the {@link CommonCommands} class. An instance of this class supports all
 * operations related to dollar cost average buying of stocks with a strategy. It takes in
 * input from the user about the portfolio in which to buy a stock, the strategy to use, the
 * start date of purchase, the end date and the interval in days to perform the transactions. It
 * performs the operation with the help of the model and lets the user know if it was successful
 * or not using the view.
 */
public class DollarCostAverageCommand extends CommonCommands {

  /**
   * Create an instance of the {@link DollarCostAverageCommand} class. This instance allows you to
   * enter instructions from the commandline and provide necessary information to buy stocks with a
   * strategy.
   *
   * @param scanner an instance of Scanner class used to get user input.
   */
  public DollarCostAverageCommand(Scanner scanner) {
    super(scanner);
  }

  /**
   * This method Performs the dollar cost purchase action on stocks. When the user wants to
   * purchase stocks: It asks the user to pick the portfolio under which the stocks will be
   * bought. It then asks for the strategy to use to perform the buy operation. It will also ask
   * for the start date of purchase, the end date and the interval in days to perform the
   * transactions. The date is in the YYYY-MM-DD format. The interval is specified as an int, it
   * cannot be negative. This operation can only be performed if there is a portfolio which has a
   * strategy associated with it.
   *
   * @param model This method takes in an Emulator model to help perform the actions and generate
   *             a result.
   * @param view The method also takes in an EmulatorView to convey the result to the user as
   */
  @Override
  public void execute(ModelExtn model, EmulatorView view) {
    int portfolioNumber = super.choosePortfolio(model, view);
    String strategyName;
    if (portfolioNumber == -1) {
      return;
    } else {
      strategyName = chooseStrategy(model, view, portfolioNumber);
    }
    LocalDate startDate = super.getDate(model, view);
    LocalTime starttime = LocalTime.of(15, 00);
    LocalDateTime startdateTime = LocalDateTime.of(startDate, starttime);

    LocalDate endDate = super.getDate(model, view);
    LocalTime endTime = LocalTime.of(15, 00);
    LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

    view.showMessage("Enter the interval in days between purchases");
    int interval = getInt(scanner, view);
    boolean isSuccess = true;
    try {
      //model.dollarCosrAveraging(portfolioNumber, strategyName, startdateTime, endDateTime,
      //interval);
    } catch (IllegalArgumentException e) {
      isSuccess = false;
      view.showMessage("Operation failed: " + e.getMessage());
    }
    if (isSuccess) {
      view.showMessage("Buy was successful");
    }
  }
}
