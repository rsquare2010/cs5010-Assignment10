package stockemulation.model;


import java.time.LocalDateTime;

/**
 * Interface class for the stock data that has information on each individual ticker purchase on a
 * particular date. This stock data is for one particular purchase. The information you can get
 * using this interface are, name of the ticker, purchase date for the stock, cost of each stock on
 * that purchase date, and the quantity of stocks of that particular ticker purchased.
 * <ul><li>Assumption: The stock is being traded in dollars ($).</li><li>Assumption: Buy or sell
 * price of the stock is the same on a particular date.(In real life even at a given time, the cost
 * to buy a stock and sell it at the same timestamp are different because of
 * fees).</li><li>Assumption: The stock value does not vary through the day. If the stock market was
 * open on a given day the stock's price is assumed to be the opening price of the stock on that
 * day.</li></ul>
 */
interface StockData {

  /**
   * Returns the ticker name of the stock that was bought as a string.
   *
   * @return the ticker name of the stock that was bought as a string.
   */
  String getName();

  /**
   * Returns the local date time of purchase of the stock as a standard java Date object.
   *
   * @return the local date time of purchase of the stock as a standard java Date object.
   */
  LocalDateTime getPurchaseDate();

  /**
   * Returns the cost price of the stock on the purchase date as a double value in dollars (Check
   * Assumption:).
   *
   * @return the cost price of the stock on the purchase date as a double value in dollars.
   */
  double getCostPrice();


  /**
   * Returns the quantity of stocks of the ticker that was bought in that purchase as double.
   *
   * @return the quantity of stocks of the ticker that was bought in that purchase as double.
   */
  double getQuantity();

}
