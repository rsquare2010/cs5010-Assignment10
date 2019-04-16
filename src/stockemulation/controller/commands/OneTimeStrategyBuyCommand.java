package stockemulation.controller.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

import stockemulation.model.ModelExtn;
import stockemulation.view.EmulatorView;

public class OneTimeStrategyBuyCommand extends CommonCommands {

  public OneTimeStrategyBuyCommand(Scanner scanner) throws IllegalArgumentException {
    super(scanner);
  }

  @Override
  public void execute(ModelExtn model, EmulatorView view) {
    int portfolioNumber = super.choosePortfolio(model, view);
    String strategyName;
    if (portfolioNumber == -1) {
      return;
    } else {
      strategyName = chooseStrategy(model, view, portfolioNumber);
    }
    LocalDate date = super.getDate(model, view);
    LocalTime time = LocalTime.of(15, 00);
    LocalDateTime dateTime = LocalDateTime.of(date, time);
    boolean isSuccess = true;
    try {
      model.dollarCosrAveraging(portfolioNumber, strategyName, dateTime, dateTime, 0);
    } catch (IllegalArgumentException e) {
      isSuccess = false;
      view.showMessage("Operation failed: " + e.getMessage());
    }
    if (isSuccess) {
      view.showMessage("Buy was successful");
    }
  }
}
