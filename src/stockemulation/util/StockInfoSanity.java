package stockemulation.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 * This is a common interface shared between model and controller to check the inputs before even
 * calling the methods. The methods that this interface supports are all static. This inputs are
 * checked according to the business logic. Currently it checks if the ticker name, date and time,
 * price of stock, and commission are valid.
 */
public interface StockInfoSanity {

  /**
   * This function checks if the entered string for ticker name of the stock is valid or not. If it
   * is invalid it throws the appropriate error. The ticker name should not be empty or null, it
   * should be 1-5 chars long and all in capital case.
   *
   * @param tickerName the input string name which has to be verified.
   * @throws IllegalArgumentException if the tickerName string is invalid as specified above.
   */
  static void isTickerValid(String tickerName) throws IllegalArgumentException {
    if (tickerName == null || tickerName.isEmpty()) {
      throw new IllegalArgumentException("Ticker name cannot be empty");
    }
    if (!tickerName.matches("^[A-Z]{1,5}+$")) {
      throw new IllegalArgumentException("Invalid format for ticker name");
    }
  }

  /**
   * This function verifies if the date and time data got as LocalDateTime object is of the right
   * values. The object should not be null. The date should be weekday and not holiday for markets.
   * The time should be within 9 AM and 4 PM when the markets are actually open. If not this throws
   * an error stating the reason.
   *
   * @param specifiedDateTime the object reference of LocalDateTime type which has to be verified,
   * @throws IllegalArgumentException if the specifiedDate is invalid as specified above.
   */

  static void isDateTimeValid(LocalDateTime specifiedDateTime) throws IllegalArgumentException {
    if (specifiedDateTime == null) {
      throw new IllegalArgumentException("DateTime of purchase cannot be null");
    }

    DayOfWeek dayOfWeek = specifiedDateTime.getDayOfWeek();
    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
      throw new IllegalArgumentException("Cannot trade on weekends");
    }
    isTimeValid(specifiedDateTime.toLocalTime());

    if (specifiedDateTime.isAfter(LocalDateTime.now())) {
      throw new IllegalArgumentException("Cannot trade on a future date or time");
    }
  }

  /**
   * This function verifies if the value of shares of a stock to be bought is valid. This should not
   * be negative for it to be valid. If it is invalid its throws an error.
   *
   * @param price the value of shares of a stock to be bought.
   * @throws IllegalArgumentException if the price is invalid.
   */
  static void isPriceValid(double price) throws IllegalArgumentException {
    if (price <= 0) {
      throw new IllegalArgumentException("Price of stock to be bought cannot be negative");
    }
  }

  /**
   * This function verifies if the value for the commission to be paid for a transaction is valid.
   * This should not be negative for it to be valid. If it is invalid its throws an error.
   *
   * @param commission the value for the commission to be paid for a transaction.
   * @throws IllegalArgumentException if the commission price is invalid.
   */
  static void isCommissionValid(double commission) throws IllegalArgumentException {
    if (commission < 0) {
      throw new IllegalArgumentException("Commission for a transaction cannot be negative");
    }
  }

  /**
   * This method checks if the time value of a transaction is valid. The application only
   * supports trading hours between 9 am and 4 pm so any transactions with a timestamp outside of
   * this will be considered invalid.
   *
   * @param time the time at which a transaction happens.
   * @throws IllegalArgumentException if the time provided is during non supported trading hours.
   */
  static void isTimeValid(LocalTime time) throws IllegalArgumentException {
    int hour = time.getHour();
    if (!(hour >= 9 && hour <= 15)) {
      throw new IllegalArgumentException("Trading is possible only between 9 am and 4 pm");
    }
  }

  static void isWeightValid(double weight) throws IllegalArgumentException {
    if(weight <= 0.0) {
      throw new IllegalArgumentException("Weight of a ticker has to be more than 0");
    } else if (weight > 100.0) {
      throw new IllegalArgumentException("Weight of a ticker should be less than 100");
    }
  }
}
