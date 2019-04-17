package stockemulation.controller.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import stockemulation.model.ModelExtn;
import stockemulation.util.StockInfoSanity;
import stockemulation.view.EmulatorView;

/**
 * An extension of the {@link CommonCommands} class. An instance of this class supports all
 * operations related to creating a strategy. It takes in input from the user for the name of the
 * strategy to be created. It creates a strategy if there isn't one already with the same name
 * with the help of the model. This class lets the user know if creating a new strategy  was
 * successful or not using the view.
**/
public class CreateStrategyCommand extends CommonCommands {


  /**
   * Create an instance of the CreateStrategyCommand class. This instance allows you to enter
   * instructions from the commandline and provide necessary information to create a new strategy.
   *
   * @param scanner an instance of Scanner class used to get user input.
   */
  public CreateStrategyCommand(Scanner scanner) {
    super(scanner);
  }

  /**
   * This method creates a new strategy. It asks the user to enter the name of the portfolio to be
   * created. It creates a new strategy with the entered name unless there already exists another
   * strategy with the same name. White spaces at the beginning and end of the name string are
   * ignored.  It asks the user to pick the portfolio under which the strategy will be created. It
   * then asks the following information from the user: Ticker names and weights associated with
   * those ticker names. Sum of the weights should be equal to 100, weights cannot be negative.
   * User will also have to specify the Ticker of the stock they want to buy (Stock tickers are
   * in upper case and less than 5 characters). The commission fees for every transaction  And
   * finally the amount of money in USD they want to buy stocks for. This operation cannot be
   * performed if a portfolio does not already exist.
   *
   * @param model This method takes in an ModelExtn model to help perform the actions and generate a
   *              result.
   * @param view  The method also takes in an EmulatorView to notify the user on successful creation
   *              of a strategy or the failure to create one as well as to provide prompts to get
   *              user input to perform actions.
   */
  @Override
  public void execute(ModelExtn model, EmulatorView view) {

    int portfolioNumber = super.choosePortfolio(model, view);

    if (portfolioNumber == -1) {
      return;
    }
    List<String> tickerList = new ArrayList<>();
    String ticker;
    String option;
    do {
      ticker = getTicker(model, view);
      tickerList.add(ticker);
      view.showMessage("Press Y to add another Ticker");
      option = scanner.next();
    }
    while (option.equalsIgnoreCase("y"));
    view.showMessage("Press Y to choose even weights, press any other key to select weights");
    option = scanner.next();
    Map<String, Double> weights = new HashMap<>();
    if (option.equalsIgnoreCase("y")) {
      for (int i = 0; i < tickerList.size(); i++) {
        weights.put(tickerList.get(i), 100.0 / (tickerList.size()));
      }
    } else {
      List<Double> weightsList = new ArrayList<>();
      boolean isWeightsValid = false;
      do {
        view.showMessage("Enter the weight for each ticker from the first one");

        Double sumWeight = 0.0;
        for (int i = 0; i < tickerList.size(); i++) {
          view.showMessage("Enter weight for ticker: " + tickerList.get(i));
          weightsList.add(getValidWeight(view));
        }
        for (int i = 0; i < weightsList.size(); i++) {
          sumWeight += weightsList.get(i);
        }
        if (100 - sumWeight > 0.01 || sumWeight - 100 > 0.01) {
          isWeightsValid = true;
        } else {
          view.showMessage("The sum of individual weights should be 100 please enter again");
        }
      }
      while (!isWeightsValid);
      for (int i = 0; i < tickerList.size(); i++) {
        weights.put(tickerList.get(i), weightsList.get(i));
      }
    }

    view.showMessage("Enter the price you want to buy stocks for:");
    double price = getPrice(view);

    view.showMessage("Enter the commission fees for this purchase:");
    double commissionPrice = getPrice(view);

    view.showMessage("Enter the name of the strategy");
    boolean isCreatedSuccessfully = false;
    String name = scanner.next();
    try {
      model.addStrategyData(name, weights, price, commissionPrice);
    } catch (IllegalArgumentException e) {
      isCreatedSuccessfully = false;
      view.showMessage(e.getMessage());
    }
    if (isCreatedSuccessfully) {
      view.showMessage("Strategy " + name.trim() + " created successfully");
    }
  }

  protected double getValidWeight(EmulatorView view) {
    Double weight;
    boolean isValidWeight;
    do {
      isValidWeight = true;
      weight = getDouble(scanner, view);
      try {
        StockInfoSanity.isWeightValid(weight);
      } catch (IllegalArgumentException e) {
        isValidWeight = false;
        view.showMessage(e.getMessage());
      }
    }
    while (!isValidWeight);
    return weight;
  }

  protected double getPrice(EmulatorView view) {
    Double price;
    boolean isValidPrice;
    do {
      isValidPrice = true;
      price = getDouble(scanner, view);
      try {
        StockInfoSanity.isPriceValid(price);
      } catch (IllegalArgumentException e) {
        isValidPrice = false;
        view.showMessage(e.getMessage());
      }
    }
    while (!isValidPrice);
    return price;
  }
}
