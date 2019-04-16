package stockemulation.controller.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

import stockemulation.model.ModelExtn;
import stockemulation.view.EmulatorView;

public class DollarCostAverageCommand extends CommonCommands {

  public DollarCostAverageCommand(Scanner scanner) throws IllegalArgumentException {
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
      model.dollarCosrAveraging(portfolioNumber, strategyName, startdateTime, endDateTime,
              interval);
    } catch (IllegalArgumentException e) {
      isSuccess = false;
      view.showMessage("Operation failed: " + e.getMessage());
    }
    if (isSuccess) {
      view.showMessage("Buy was successful");
    }
  }
}
