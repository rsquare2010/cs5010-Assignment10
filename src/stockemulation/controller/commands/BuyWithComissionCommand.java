package stockemulation.controller.commands;

import java.time.LocalDateTime;
import java.util.Scanner;

import stockemulation.model.ModelExtn;
import stockemulation.util.StockInfoSanity;
import stockemulation.view.EmulatorView;

/**
 * An extension of the {@link CommonCommands} class. An instance of this class supports all
 * operations related to buying a stock. It takes in input from the user about the portfolio in
 * which to buy a stock, the date of purchase, the amount in dollars to spend on buying stocks,
 * the ticker of the stock to be bought and the commission for this transaction in US Dollars. It
 * performs the operation with the help of the model and lets the user know if it was successful
 * or not using the view.
 */

public class BuyWithComissionCommand extends BuyCommand {

  /**
   * Create an instance of the {@link BuyWithComissionCommand} class. This instance allows you to
   * enter instructions from the commandline and provide necessary information to buy stocks with
   * commission fees.
   *
   * @param scanner an instance of Scanner class used to get user input.
   */
  public BuyWithComissionCommand(Scanner scanner) {
    super(scanner);
  }

  /**
   * This method Performs the Buy stocks action with support for commissions. When the user wants
   * to purchase stocks: It asks the user to pick the portfolio under which the stocks will be
   * bought. It then asks the following information from the user Date in YYYY-MM-DD format. Time
   * of stock purchase in HH-MM format. Input time is in 24hour format and so takes in numbers.
   * Stocks cannot be bought outside of stock market trading time: 9am - 4pm. Stocks can also not
   * be bought on weekends and the days the stock market is shut. User will also have to specify
   * the Ticker of the stock they want to buy (Stock tickers are in upper case and less than 5
   * characters). User will also have to specify the commission fees for this transaction in US
   * Dollars And finally the amount of money in USD they want to buy stocks for. This operation
   * cannot be performed if a portfolio does not already exist.
   *
   * @param model This method takes in an ModelExtn model to help perform the actions and generate a
   *              result.
   * @param view  The method also takes in an EmulatorView to convey if the stocks were bought
   *              success as well as to provide prompts to get user input to perform actions.
   */
  @Override
  public void execute(ModelExtn model, EmulatorView view) {
    int portfolioNumber = super.choosePortfolio(model, view);

    if (portfolioNumber == -1) {
      return;
    }

    LocalDateTime dateTime = super.getDateTime(model, view);

    String ticker = super.getTicker(model, view);

    view.showMessage("Enter the price you want to buy stocks for:");
    double price = getPrice(view);

    view.showMessage("Enter the commission fees for this purchase:");
    double commissionPrice = getCommission(view);

    try {
      model.buyStock(portfolioNumber, dateTime, ticker, price, commissionPrice);
    } catch (IllegalArgumentException e) {
      view.showMessage(e.getMessage());
      return;
    }
    view.showMessage("Stocks Successfully bought");
  }

  private double getCommission(EmulatorView view) {
    Double commission;
    boolean isValidCommission;
    do {
      isValidCommission = true;
      commission = getDouble(scanner, view);
      try {
        StockInfoSanity.isCommissionValid(commission);
      } catch (IllegalArgumentException e) {
        isValidCommission = false;
        view.showMessage(e.getMessage());
      }
    }
    while (!isValidCommission);
    return commission;
  }
}
