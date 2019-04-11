package stockemulation.model;

import java.time.LocalDateTime;

import stockemulation.util.StockInfoSanity;

/**
 * This is implementation of {@link StockDataExtn} and get most of its implementation by extending
 * from {@link StockDataImpl}. This implementation provides a way to get the commission details
 * stored in the class. It also provides a way to print the class details as a string by overriding
 * toString().
 */
class StockDataExtnImpl extends StockDataImpl implements StockDataExtn {

  private double commission;      // price in $ of commission that has to paid for the transaction.


  /**
   * Constructor for this extension implementation of stock data. This first constructs the parent
   * implementation {@link StockDataImpl} by passing the arguments need for it. Propagates any error
   * thrown by it. Then checks if the commission argument entered is valid or not. And then assigns
   * it to the member variable. The validity for the arguments are either specified in the parent
   * class or in {@link StockInfoSanity}.
   *
   * @param tickerName   the ticker name of the stock to be bought.
   * @param purchaseDate the local date time of purchase for the stock.
   * @param costPrice    the price/value for the stock on the purchase date.
   * @param quantity     the amount of stock that was bought in that particular purchase.
   * @param commission   the price to be paid for the transaction.
   * @throws IllegalArgumentException if tickerName is not of the right format given above.
   * @throws IllegalArgumentException if purchaseDame or _tickerName is null.
   * @throws IllegalArgumentException if costPrice or _quantity is negative.
   * @throws IllegalArgumentException if commission is negative.
   */
  StockDataExtnImpl(
          String tickerName,
          LocalDateTime purchaseDate,
          double costPrice,
          double quantity,
          double commission
  ) throws IllegalArgumentException {
    super(tickerName, purchaseDate, costPrice, quantity);
    StockInfoSanity.isCommissionValid(commission);
    this.commission = commission;
  }

  @Override
  public double getCommission() {
    return commission;
  }

  @Override
  public String toString() {
    return "Ticker name:" + getName()
            + " Purchase date:" + getPurchaseDate()
            + " Cost per Stock:" + getCostPrice()
            + " Quantity:" + getQuantity()
            + "Commission:" + getCommission();
  }
}
