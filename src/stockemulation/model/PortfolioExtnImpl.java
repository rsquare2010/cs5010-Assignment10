package stockemulation.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import stockemulation.util.StockInfoSanity;


/**
 * This is the implementation of {@link PortfolioExtn} and most of the implementation is extended
 * from {@link PortfolioImpl}. This implements the buyshares by taking in the commission price for
 * each transaction and also overrides the getCostBasis to reflect this addition. This
 * implementation provides a way to write the details of the portfolio including all transaction
 * information stored in its {@link StockDataExtn} list and strategies stored in the {@link
 * StrategyData} map  into a json file.
 */
class PortfolioExtnImpl extends PortfolioImpl implements PortfolioExtn {

  /**
   * Constructor that sets the data source reference, title of portfolio by calling the parent
   * constructor and passing along the arguments it takes. Propagates and error thrown by the base
   * class. Also initializes the map of strategies.
   *
   * @param title      the tile of the portfolio.
   * @param dataSource reference of the API that is used to fetch data from.
   * @throws IllegalArgumentException title or data source is invalid.
   */
  PortfolioExtnImpl(String title, API dataSource) throws IllegalArgumentException {
    super(title, dataSource);
  }

  @Override
  public void buyShares(
          String tickerName,
          double value,
          LocalDateTime specifiedDate,
          double commission
  ) throws IllegalArgumentException {

    StockInfoSanity.isTickerValid(tickerName);
    StockInfoSanity.isDateTimeValid(specifiedDate);
    StockInfoSanity.isPriceValid(value);
    StockInfoSanity.isCommissionValid(commission);

    double unitStockPrice = dataSource.getPriceAtTime(
            specifiedDate.toLocalDate(), tickerName);
    double quantity = value / unitStockPrice;
    updateComposition(tickerName, quantity);

    stockPurchaseList.add(new StockDataExtnImpl(
            tickerName, specifiedDate, unitStockPrice, quantity, commission));
  }

  @Override
  public void addShares(String tickerName, double costPerUnit, double quantity, LocalDateTime specifiedDate, double commission) throws IllegalArgumentException {
    StockInfoSanity.isTickerValid(tickerName);
    StockInfoSanity.isDateTimeValid(specifiedDate);
    StockInfoSanity.isCommissionValid(commission);
    updateComposition(tickerName, quantity);
    stockPurchaseList.add(new StockDataExtnImpl(
            tickerName, specifiedDate, costPerUnit, quantity, commission));
  }

  @Override
  public void writeToFile(String filepath) throws IOException {
    JSONObject obj = new JSONObject();

    obj.put("title", this.title);

    obj.put("transactions", addTransactionsToFileWrite());

    try (FileWriter file = new FileWriter(filepath)) {
      file.write(obj.toJSONString());
      file.flush();
    } catch (IOException e) {
      throw new IOException(e);
    }
  }

  @Override
  public double getCostBasis(LocalDateTime specifiedDate)
          throws IllegalStateException {
    return stockPurchaseList.stream()
            .filter(s -> s.getPurchaseDate().isBefore(specifiedDate))
            .mapToDouble(s -> (s.getQuantity() * s.getCostPrice() + s.getCommission()))
            .sum();
  }

  @Override
  public void investWeighted(
          LocalDateTime investmentDate,
          double totalInvestmentAmount,
          Map<String, Double> stockWeights,
          double commission
  )throws IllegalArgumentException {
    StockInfoSanity.isPriceValid(totalInvestmentAmount);
    StockInfoSanity.isCommissionValid(commission);
    LocalDateTime requiredDate = getMarketOpenDate(investmentDate);

    for (Map.Entry<String, Double> entry : stockWeights.entrySet()) {
      this.buyShares(
              entry.getKey(),
              entry.getValue() * totalInvestmentAmount / 100,
              requiredDate,
              commission
      );
    }
  }

  @Override
  public void investEqual(
          LocalDateTime investmentDate,
          double totalInvestmentAmount,
          double commission
  ) throws IllegalArgumentException {
    StockInfoSanity.isPriceValid(totalInvestmentAmount);
    StockInfoSanity.isCommissionValid(commission);
    LocalDateTime requiredDate = getMarketOpenDate(investmentDate);

    double weightedAmountPerStock = totalInvestmentAmount / uniqueTickerList.size();
    for (String tickerName : this.uniqueTickerList.keySet()) {
      this.buyShares(
              tickerName,
              weightedAmountPerStock,
              requiredDate,
              commission);
    }
  }

  private LocalDateTime getMarketOpenDate(LocalDateTime investmentDate) {
    while (investmentDate.isBefore(LocalDateTime.now())) {
      try {
        StockInfoSanity.isDateTimeValid(investmentDate);
        break;
      } catch (IllegalArgumentException e) {
        investmentDate = investmentDate.plusDays(1);
        continue;
      }
    }
    return investmentDate;
  }

  private JSONArray addTransactionsToFileWrite() {
    JSONArray transactions = new JSONArray();
    for (StockDataExtn stock : stockPurchaseList) {
      JSONObject stockObj = new JSONObject();
      stockObj.put("ticker", stock.getName());
      stockObj.put("purchaseDate", stock.getPurchaseDate().toString());
      stockObj.put("costPerUnit", stock.getCostPrice());
      stockObj.put("quantity", stock.getQuantity());
      stockObj.put("commission", stock.getCommission());
      transactions.add(stockObj);
    }
    return transactions;
  }


}
