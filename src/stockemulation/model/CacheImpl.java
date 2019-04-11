package stockemulation.model;

import java.time.LocalDate;
import java.util.HashMap;

/**
 * Implementation of the cache interface. An instance of this class stores dates on which the
 * market was open and the opening prices of a particular stock on that day. An object of this
 * class also contains the last date this cache was updated.
 */
class CacheImpl implements Cache {

  private LocalDate date;
  private HashMap<String, String> stockData;

  /**
   * Used to create an instance of this implementation of the Cache interface. It stores dates on
   * which the market was open and the opening prices of a particular stock on that day. An
   * object of this class also contains the last date this cache was updated.
   * @param date the latest of information that is being cached.
   * @param map of dates and opening stock prices on that day.
   */
  CacheImpl(LocalDate date, HashMap<String, String> map) {
    this.date = date;
    stockData = map;
  }

  @Override
  public LocalDate getDate() {
    return date;
  }

  @Override
  public HashMap<String, String> getStockData() {
    return stockData;
  }
}
