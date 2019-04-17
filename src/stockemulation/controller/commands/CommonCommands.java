package stockemulation.controller.commands;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import stockemulation.controller.EmulatorCommand;
import stockemulation.model.Model;
import stockemulation.model.ModelExtn;
import stockemulation.util.StockInfoSanity;
import stockemulation.view.EmulatorView;

/**
 * This is an abstract class which contains common methods used by various implementations of the
 * {@link EmulatorCommand} class. This class contains methods that take in input from the
 * user,validated them and converts them into an appropriate format to be used by the classes
 * extending it.
 */
abstract class CommonCommands implements EmulatorCommand {

  Scanner scanner;

  CommonCommands(Scanner scanner) throws IllegalArgumentException {
    if (scanner == null) {
      throw new IllegalArgumentException("Scanner cannot be null");
    }
    this.scanner = scanner;
  }

  int choosePortfolio(Model model, EmulatorView view) {
    if (model.getPortfolioCount() == 0) {
      view.showMessage("create a portfolio before you perform this operation");
      return -1;
    }
    List portfolioList = model.getPortfolioList();
    StringBuilder portfolioListFormatted = new StringBuilder();
    for (int i = 0; i < portfolioList.size(); i++) {
      portfolioListFormatted.append(i + 1).append(".").append(portfolioList.get(i)).append("\n");
    }
    view.showMessage("Enter the number of the portfolio you want to open");
    view.showMessage(portfolioListFormatted.toString());
    int portfolioNumber = getInt(scanner, view);
    while (portfolioNumber < 1 || portfolioNumber > model.getPortfolioCount()) {
      view.showMessage("Enter a number between 1 and " + model.getPortfolioCount());
      portfolioNumber = getInt(scanner, view);
    }
    return portfolioNumber - 1;
  }

  LocalDateTime getDateTime(Model model, EmulatorView view) {
    LocalDate date = getDate(model, view);
    LocalTime time = getTime(model, view);
    return LocalDateTime.of(date, time);
  }

  LocalDate getDate(Model model, EmulatorView view) {

    view.showMessage("Enter the year you want to perform this operation of the format YYYY");
    int year = getInt(scanner, view);
    while (!validYear(year)) {
      view.showMessage("Enter a year between 1900 and present");
      year = getInt(scanner, view);
    }

    view.showMessage("Enter the month for the in the format MM");
    int month = getInt(scanner, view);
    while (!validMonth(month, year)) {
      view.showMessage("Enter a month between 1 and 12 that is not in the future");
      month = getInt(scanner, view);
    }

    view.showMessage("Enter the day you want to perform this operation of the format DD");
    int day = getInt(scanner, view);
    while (!validDay(day, month, year)) {
      view.showMessage("Enter a valid date for the month and year. Date should not be in the "
              + "future");
      day = getInt(scanner, view);
    }
    return LocalDate.of(year, month, day);
  }

  private LocalTime getTime(Model model, EmulatorView view) {
    view.showMessage("Enter the hour you want to buy the stock between 09 and 16 in the "
            + "24 hour format HH");
    int hour = getHours(view);

    view.showMessage("Enter the minute you want to buy the stock, of the format MM with value "
            + "between 0 and 60");
    int minutes = getMinutes(view, hour);

    return LocalTime.of(hour, minutes);
  }

  private int getHours(EmulatorView view) {
    boolean isValidHour;
    int hour;
    do {
      isValidHour = true;
      hour = getInt(scanner, view);
      try {
        LocalTime.of(hour, 0);
      } catch (DateTimeException e) {
        isValidHour = false;
        view.showMessage("Enter a valid hour value");
      }
    }
    while (!isValidHour);
    return hour;
  }

  private int getMinutes(EmulatorView view, int hour) {
    int minutes;
    boolean isValidMinute;
    do {
      isValidMinute = true;
      minutes = getInt(scanner, view);
      try {
        LocalTime.of(hour, minutes);
      } catch (DateTimeException e) {
        isValidMinute = false;
        view.showMessage("Enter a minute value between 0 and 60 and not in the future");
      }
    }
    while (!isValidMinute);
    return minutes;
  }

  String getTicker(Model model, EmulatorView view) {
    view.showMessage("Enter the Ticker of the stock you want to buy");
    String ticker;
    boolean validTicker;
    do {
      validTicker = true;
      ticker = scanner.next();
      try {
        StockInfoSanity.isTickerValid(ticker);
      } catch (IllegalArgumentException e) {
        validTicker = false;
        view.showMessage(e.getMessage());
      }
    }
    while (!validTicker);
    return ticker;
  }

  String getFilePath(Model model, EmulatorView view) {
    view.showMessage("Enter the filepath");
    String filePath = scanner.next();
    while (filePath.isEmpty()) {
      view.showMessage("Enter a valid ticker of four characters in upper case");
      filePath = scanner.next();
    }
    return filePath;
  }

  private boolean validYear(int year) {
    return year > 1900 && year <= LocalDate.now().getYear();
  }

  private boolean validMonth(int month, int year) {
    if (year < LocalDate.now().getYear()) {
      return month > 0 && month < 13;
    } else {
      return month > 0 && month <= LocalDate.now().getMonthValue();
    }
  }

  private boolean validDay(int day, int month, int year) {
    LocalDate date;
    try {
      date = LocalDate.of(year, month, day);
    } catch (DateTimeException e) {
      return false;
    }
    return !date.isAfter(LocalDate.now());
  }

  protected String chooseStrategy(ModelExtn model, EmulatorView view) {
    view.showMessage("Choose ");
    List<String> strategyList = model.getStrategyList();
    if (strategyList.size() == 0) {
      view.showMessage("create a strategy before you perform this operation");
      return null;
    }
    StringBuilder strategyListFormatted = new StringBuilder();
    int strategyListSize = strategyList.size();
    for (int i = 0; i < strategyListSize; i++) {
      strategyListFormatted.append(i + 1).append(".").append(strategyList.get(i)).append("\n");
    }
    view.showMessage("Enter the number of the strategy you want to use");
    view.showMessage(strategyListFormatted.toString());
    int strategyNumber = getInt(scanner, view);
    while (strategyNumber < 1 || strategyNumber > strategyListSize) {
      view.showMessage("Enter a number between 1 and " + strategyListSize);
      strategyNumber = getInt(scanner, view);
    }
    return strategyList.get(strategyNumber - 1);
  }

  protected int getInt(Scanner scanner, EmulatorView view) {
    while (!scanner.hasNextInt()) {
      scanner.next();
      view.showMessage("Enter a valid number");
    }
    return scanner.nextInt();
  }

  protected Double getDouble(Scanner scanner, EmulatorView view) {
    while (!scanner.hasNextDouble()) {
      scanner.next();
      view.showMessage("Enter a number");
    }
    return scanner.nextDouble();
  }
}
