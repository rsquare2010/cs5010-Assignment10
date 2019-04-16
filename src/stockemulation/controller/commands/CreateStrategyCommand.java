package stockemulation.controller.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import stockemulation.model.ModelExtn;
import stockemulation.util.StockInfoSanity;
import stockemulation.view.EmulatorView;

public class CreateStrategyCommand extends CommonCommands {


  public CreateStrategyCommand(Scanner scanner) throws IllegalArgumentException {
    super(scanner);
  }

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
      model.addStrategyToPortfolio(portfolioNumber, name, weights, price, commissionPrice);
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
