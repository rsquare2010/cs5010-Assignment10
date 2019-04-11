package stockemulation.model;

import java.time.LocalDate;
import java.util.HashMap;

/**
 * Interface of a local cache, We use an implementation of this class to cache API responses from
 * the AlphaVantage API.
 */
interface Cache {

  /**
   * Getter method to obtain the date on which this cache was last updated.
   * @return a {@link LocalDate} object which represents the date on which this cache was updated.
   */
  LocalDate getDate();

  /**
   * Getter method to obtain the cached stock related information.
   * @return a HashMap with key and value as strings which store historic dates and the
   *         opening prices of a specific stock on that day.
   */
  HashMap<String, String> getStockData();
}
