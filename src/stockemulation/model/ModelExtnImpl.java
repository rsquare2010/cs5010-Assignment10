package stockemulation.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This is an implementation of {@link ModelExtn} and has some of the implementations directly taken
 * from {@link ModelImpl} by extending it. This provide two constructors, one to create the object
 * with default data source which is Alpha Vantage api and other constructor to choose the API.
 */
public class ModelExtnImpl extends ModelImpl implements ModelExtn {

  Map<String, StrategyData> investmentStrategies;

  /**
   * Default constructor that calls the default constructor of the base class and sets the api to
   * the efault Alpha vantage api.
   */
  public ModelExtnImpl() {
    super();
    this.investmentStrategies = new HashMap<>();
  }

  /**
   * Constructor that provides a way to select the data source by passing the Enum value. This gets
   * a reference to the api singleton of that type. It essentially make a call to the base class and
   * does this.
   */
  public ModelExtnImpl(APITypes datasourcetype) {
    super(datasourcetype);
    this.investmentStrategies = new HashMap<>();
  }

  @Override
  public void buyStock(int portfolioNumber, LocalDateTime date, String ticker, Double price,
                       double commission) throws IllegalArgumentException {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }

    portfolios.get(portfolioNumber).buyShares(ticker, price, date, commission);
  }

  @Override
  public void writePortfolioToFile(String filepath, int portfolioNumber)
          throws IllegalArgumentException, IOException {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }
    if (filepath == null || filepath.equals("")) {
      throw new IllegalArgumentException("File path for writing portfolio cannot be empty");
    }
    portfolios.get(portfolioNumber).writeToFile(filepath);

  }

  @Override
  public void readPortfolioFromFile(String filepath) throws IllegalArgumentException, IOException,
          ParseException {
    JSONParser parser = new JSONParser();
    Object obj = parser.parse(new FileReader(filepath));
    JSONObject jsonObject = (JSONObject) obj;

    String title = (String) jsonObject.get("title");
    createPortfolio(title);
    try {
      readTransactionFromPortfolioFile(jsonObject);
    }catch (Exception e) {
      portfolios.remove(portfolios.size()-1);
    }
  }

  @Override
  public void addStrategyData(String strategyName, Map<String, Double> tickerWeightMap, double investmentAmount, double commission) throws IllegalArgumentException {
    this.investmentStrategies.put(
            strategyName,
            new StrategyDataImpl(strategyName, tickerWeightMap, investmentAmount, commission)
    );
  }

  @Override
  public void writeStrategyToFile(String filepath, String strategyName) throws IllegalArgumentException, IOException {
    StrategyData strategyData = investmentStrategies.get(strategyName);
    JSONObject strategyObj = new JSONObject();
    strategyObj.put("strategyName", strategyData.getStrategyName());
    strategyObj.put("tickerWeightsMap", strategyData.getTickerAndWeights());
    strategyObj.put("investmentAmount", strategyData.getInvestmentAmount());
    strategyObj.put("commission", strategyData.getCommission());

    try (FileWriter file = new FileWriter(filepath)) {
      file.write(strategyObj.toJSONString());
      file.flush();
    } catch (IOException e) {
      throw new IOException(e);
    }
  }

  @Override
  public void readStrategyFromFile(String filepath) throws IllegalArgumentException, IOException, ParseException {
    JSONParser parser = new JSONParser();
    Object obj = parser.parse(new FileReader(filepath));
    JSONObject jsonObject = (JSONObject) obj;
    addStrategyData(
            (String) jsonObject.get("strategyName"),
            (Map<String, Double>) jsonObject.get("tickerWeightsMap"),
            (double) jsonObject.get("investmentAmount"),
            (double) jsonObject.get("commission")
    );
  }

  @Override
  public void investWithStrategy(int portfolioNumber, String strategyName, LocalDateTime investmentDate)
          throws IllegalArgumentException {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }
    StrategyData strategy = investmentStrategies.get(strategyName);
    portfolios.get(portfolioNumber).investWeighted(
            investmentDate,
            strategy.getInvestmentAmount(),
            strategy.getTickerAndWeights(),
            strategy.getCommission()
    );
  }


  @Override
  public List<String> getStrategyList() {
    return new ArrayList<>(investmentStrategies.keySet());
  }

  @Override
  public void dollarCostAveraging(
          int portfolioNumber,
          String strategyName,
          LocalDateTime startDate,
          LocalDateTime endDate,
          int freaquencyInDays
  ) throws IllegalArgumentException {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }
    if (strategyName == null || startDate == null) {
      throw new IllegalStateException("strategy name or start date cannot be null");
    }
    if (freaquencyInDays <= 0) {
      throw new IllegalArgumentException("investment frequency cannot be negative or 0.");
    }

    try {
      PortfolioExtn portfolio = portfolios.get(portfolioNumber);
      StrategyData strategy = investmentStrategies.get(strategyName);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e);
    }

    LocalDateTime currentDate = startDate;
    if (endDate == null) {
      endDate = LocalDateTime.now();
    }

    while (currentDate.isBefore(endDate)) {
      investWithStrategy(portfolioNumber, strategyName, currentDate);
      currentDate = currentDate.plusDays(freaquencyInDays);
    }
  }

  private void addStock(int portfolioNumber, LocalDateTime date, String ticker, double costPerUnit, double quantity, double commission) throws IllegalArgumentException {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }
    portfolios.get(portfolioNumber).addShares(ticker, costPerUnit, quantity, date, commission);
  }

  private void readTransactionFromPortfolioFile(JSONObject jsonObject) {
    JSONArray transactions = (JSONArray) jsonObject.get("transactions");
    Iterator<JSONObject> iterator = transactions.iterator();
    while (iterator.hasNext()) {
      JSONObject stockObj = iterator.next();
      addStock(
              portfolios.size() - 1,
              LocalDateTime.parse((String) stockObj.get("purchaseDate")),
              (String) stockObj.get("ticker"),
              (double) stockObj.get("costPerUnit"),
              (double) stockObj.get("quantity"),
              (double) stockObj.get("commission")
      );
    }
  }

}