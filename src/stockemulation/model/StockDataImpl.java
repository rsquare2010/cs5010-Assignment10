package stockemulation.model;

import java.time.LocalDateTime;

import stockemulation.util.StockInfoSanity;

/**
 * Implementation class of the interface StockData. This stock data is for one particular purchase.
 * It also has the functionality to return the contents of the purchase as a string.
 */
class StockDataImpl implements StockData {

  private final String tickerName;              /// Ticker name of the stock.
  private final LocalDateTime purchaseDate;     /// Local Date of purchase of the stock.
  private final double costPrice;               /// Price/Value of stock on the purchase date.
  private final double quantity;                /// Quantity of stock bought in the purchase.

  /**
   * Constructor for the this implementation. The ticker name argument has to be a string 4
   * characters long and all in capital cases. The purchase date should be a standard java
   * LocalDateTime reference. Assumption: stock is assumed to be bought between market open and
   * market close on that day. Takes in cost price of per unit quantity of stock for the ticker.
   * Assumption: cost price is assumed constant for a day from market open till market close. Also,
   * takes in the quantity of stocks bought in that purchase. Ticker format: "XXXXX", length 1-5 and
   * all in capitals. Assumption: data on stock is available starting only from 2000's.
   *
   * @param tickerName   the ticker name of the stock to be bought.
   * @param purchaseDate the local date time of purchase for the stock.
   * @param costPrice    the price/value for the stock on the purchase date.
   * @param quantity     the amount of stock that was bought in that particular purchase.
   * @throws IllegalArgumentException if _tickerName is not of the right format given above.
   * @throws IllegalArgumentException if _purchaseDame or _tickerName is null.
   * @throws IllegalArgumentException if _costPrice or _quantity is negative.
   */
  StockDataImpl(
          String tickerName,
          LocalDateTime purchaseDate,
          double costPrice,
          double quantity) throws IllegalArgumentException {
    StockInfoSanity.isTickerValid(tickerName);
    StockInfoSanity.isDateTimeValid(purchaseDate);
    if (costPrice < 0 || quantity < 0) {
      throw new IllegalArgumentException("Cost price or quantity cannot be negative");
    }

    this.tickerName = tickerName;
    this.purchaseDate = purchaseDate;
    this.costPrice = costPrice;
    this.quantity = quantity;
  }

  @Override
  public String getName() {
    return tickerName;
  }

  @Override
  public LocalDateTime getPurchaseDate() {
    return purchaseDate;
  }

  @Override
  public double getCostPrice() {
    return costPrice;
  }

  @Override
  public double getQuantity() {
    return quantity;
  }

  @Override
  public String toString() {
    return "Ticker name:" + tickerName
            + " Purchase date:" + purchaseDate
            + " Cost per Stock:" + costPrice
            + " Quantity:" + quantity;
  }

}
