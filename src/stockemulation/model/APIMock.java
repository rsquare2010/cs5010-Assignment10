package stockemulation.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;


/**
 * This is a mock api class that is used to fet the stock info on a selected date and is primarily
 * used for testing the model in isolation from the actual api. This implements the API interface.
 * This is also a singleton and the same instance is used across the program lifetime. This uses a
 * lazy constructor an instance is create only on the first usage.
 */
class APIMock implements API {

  private List<String> dummpStocks;

  private static APIMock instance;

  /**
   * Private constructor that aids in making this class a singleton.
   */
  private APIMock() {
    dummpStocks = new LinkedList<>();
    dummpStocks.add("AAPL");
    dummpStocks.add("GOOG");
    dummpStocks.add("AS");
  }

  /**
   * Returns a reference of this mock api singleton. This calls the constructor lazily.
   */
  static APIMock getInstance() {
    if (instance == null) {
      instance = new APIMock();
    }
    return instance;
  }

  @Override
  public double getPriceAtTime(LocalDate date, String stockSymbol) throws IllegalArgumentException {
    if (!dummpStocks.contains(stockSymbol) || date.isBefore(LocalDate.parse("2000-12-03"))) {
      throw new IllegalArgumentException("Stock doesn't exist");
    }

    if (date == LocalDate.parse("2014-12-12")) {
      throw new IllegalArgumentException("Holiday");
    }

    if (date.isBefore(LocalDate.parse("2016-01-01"))) {
      return 30;
    } else {
      return 60;
    }
  }
}
