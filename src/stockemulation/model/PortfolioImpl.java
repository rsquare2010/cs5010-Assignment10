package stockemulation.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import stockemulation.util.StockInfoSanity;

/**
 * An implementation of the Portfolio interface.  This stores a collection of stock purchases as a
 * list of StockData. Also, stores a map of unique stock to their quantity. This is updated as and
 * when the stock is purchased. For each portfolio has a title and a source from which it can fetch
 * the stock data from. This source is a singleton which can vary depending on if its from a file or
 * a database or from a application. The source type is set during construction. For testing APIMock
 * is used. For each additional sources just a new class can be introduced to the api package.
 */
class PortfolioImpl implements Portfolio {

  protected final String title;                         /// Name of the portfolio.
  protected Map<String, Double> uniqueTickerList;       /// Map of unique stock and their quantity.
  protected List<StockDataExtn> stockPurchaseList;      /// List of purchases.
  protected API dataSource;                             /// Datasource name.

  /**
   * Constructor that sets the data source for the portfolio and sets the name of the portfolio. It
   * also initializes the lists to store the stock composition and purchase list. Throws an error if
   * the title is null or empty.
   *
   * @param title      Get the title of this portfolio.
   * @param dataSource A string to represent the datasource.
   * @throws IllegalArgumentException is thrown when a portfolio cannot be created.
   */
  PortfolioImpl(String title, API dataSource) throws IllegalArgumentException {
    if (title == null || title.isEmpty()) {
      throw new IllegalArgumentException("Portfolio title cannot be null or empty string.");
    }
    this.title = title;
    this.dataSource = dataSource;
    this.uniqueTickerList = new HashMap<>();
    this.stockPurchaseList = new LinkedList<>();
  }

  @Override
  public void buyShares(String tickerName, double value, LocalDateTime specifiedDate)
          throws IllegalArgumentException {
    StockInfoSanity.isTickerValid(tickerName);
    StockInfoSanity.isDateTimeValid(specifiedDate);
    StockInfoSanity.isPriceValid(value);

    double unitStockPrice = dataSource.getPriceAtTime(
            specifiedDate.toLocalDate(), tickerName);
    double quantity = value / unitStockPrice;

    updateComposition(tickerName, quantity);

    // made commission 0 to make it work like the old way, this is overridden in the inherited class
    stockPurchaseList.add(new StockDataExtnImpl(tickerName, specifiedDate, unitStockPrice,
            quantity, 0));
  }

  @Override
  public String getPorfolioTitle() {
    return title;
  }

  @Override
  public Map<String, Double> getCompositionSimple() {
    return Collections.unmodifiableMap(uniqueTickerList);
  }

  @Override
  public double getCostBasis(LocalDateTime specifiedDate) throws IllegalArgumentException {
    return stockPurchaseList.stream()
            .filter(s -> s.getPurchaseDate().isBefore(specifiedDate))
            .mapToDouble(s -> (s.getQuantity() * s.getCostPrice()))
            .sum();
  }

  @Override
  public double getTotalValue(LocalDateTime specifiedDate) throws IllegalArgumentException {
    return stockPurchaseList.stream()
            .filter(s -> s.getPurchaseDate().isBefore(specifiedDate))
            .mapToDouble(s -> (s.getQuantity() * dataSource
                    .getPriceAtTime(specifiedDate.toLocalDate(), s.getName())))
            .sum();
  }

  @Override
  public String toString() {
    return "Portfolio:" + title
            + "Datasource:" + dataSource;
  }


  protected void updateComposition(String tickerName, double quantity) {
    if (uniqueTickerList.containsKey(tickerName)) {
      uniqueTickerList.put(tickerName, uniqueTickerList.get(tickerName) + quantity);
    }
    else {
      uniqueTickerList.put(tickerName, quantity);
    }
  }

}
