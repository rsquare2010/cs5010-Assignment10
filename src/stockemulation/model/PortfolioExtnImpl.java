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
 * implementation provides a way to write the details of the potfolio incliding all transation
 * information stored in its {@link StockDataExtn} list into a json file.
 */
class PortfolioExtnImpl extends PortfolioImpl implements PortfolioExtn {

  Map<String, StrategyData> investmentStrategies;

  /**
   * Constructor that sets the data source reference, title of portfolio by calling the parent
   * constructor and passing along the arguments it takes. Propagates and error thrown by the base
   * class.
   *
   * @param title      the tile of the portfolio.
   * @param dataSource reference of the API that is used to fetch data from.
   * @throws IllegalArgumentException title or data source is invalid.
   */
  PortfolioExtnImpl(String title, API dataSource) throws IllegalArgumentException {
    super(title, dataSource);
    this.investmentStrategies = new HashMap<>();
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

    obj.put("strategies", addStrategiesToFileWrite());

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
  public void investWeighted(LocalDateTime investmentDate, double totalInvestmentAmount, Map<String, Double> stockWeights, double commission) {
    StockInfoSanity.isDateTimeValid(investmentDate);
    for (Map.Entry<String, Double> entry : stockWeights.entrySet()) {
      this.buyShares(
              entry.getKey(),
              entry.getValue() * totalInvestmentAmount / 100,
              investmentDate,
              commission
      );
    }
  }

  @Override
  public void investEqual(LocalDateTime investmentDate, double totalInvestmentAmount, double commission) {
    double weightedAmountPerStock = totalInvestmentAmount / uniqueTickerList.size();
    for (String tickerName : this.uniqueTickerList.keySet()) {
      this.buyShares(
              tickerName,
              weightedAmountPerStock,
              investmentDate,
              commission);
    }
  }

  @Override
  public void createAndUpdateStrategy(String strategyName, Map<String, Double> tickerWeightMap, double investmentAmount, double commission) {
    this.investmentStrategies.put(
            strategyName,
            new StrategyDataImpl(strategyName, tickerWeightMap, investmentAmount, commission)
    );
  }

  @Override
  public StrategyData getStrategyByName(String strategyName) {
    if (!this.investmentStrategies.containsKey(strategyName)) {
      throw new IllegalStateException("The strategy does not exist in this portfolio");
    }
    return investmentStrategies.get(strategyName);
  }

  @Override
  public List<String> getStrategiesList() {
    return new ArrayList<>(investmentStrategies.keySet());
  }

  @Override
  public void investWithStrategy(String strategyName, LocalDateTime speifiedDate) {
    StrategyData strategy = this.getStrategyByName(strategyName);
    investWeighted(
            speifiedDate, strategy.getInvestmentAmount(), strategy.getTickerAndWeights(), strategy.getCommission());
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

  private JSONArray addStrategiesToFileWrite() {
    JSONArray strategies = new JSONArray();
    for (StrategyData strategyData : investmentStrategies.values()) {
      JSONObject strategyObj = new JSONObject();
      strategyObj.put("strategyName", strategyData.getStrategyName());
      strategyObj.put("tickerWeightsMap", strategyData.getTickerAndWeights());
      strategyObj.put("investmentAmount", strategyData.getInvestmentAmount());
      strategyObj.put("commission", strategyData.getCommission());
      strategies.add(strategyObj);
    }
    return strategies;
  }


}
