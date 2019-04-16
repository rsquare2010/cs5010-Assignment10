package stockemulation.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import stockemulation.util.StockInfoSanity;

/**
 * This is an implementation of {@link ModelExtn} and has some of the implementations directly taken
 * from {@link ModelImpl} by extending it. This provide two constructors, one to create the object
 * with default data source which is Alpha Vantage api and other constructor to choose the API.
 */
public class ModelExtnImpl extends ModelImpl implements ModelExtn {

  /**
   * Default constructor that calls the default constructor of the base class and sets the api to
   * the efault Alpha vantage api.
   */
  public ModelExtnImpl() {
    super();
  }

  /**
   * Constructor that provides a way to select the data source by passing the Enum value. This gets
   * a reference to the api singleton of that type. It essentially make a call to the base class and
   * does this.
   */
  public ModelExtnImpl(APITypes datasourcetype) {
    super(datasourcetype);
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

    readTransactionFromPortfolioFile(jsonObject);
    readStrategiesFromPortfolioFile(jsonObject);

  }

  @Override
  public void dollarCostAveraging(
          int portfolioNumber,
          String strategyName,
          LocalDateTime startDate,
          LocalDateTime endDate,
          int freaquencyInDays
  ) {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }
    if (strategyName == null || startDate == null) {
      throw new IllegalStateException("strategy name or start date cannot be null");
    }

    PortfolioExtn portfolio = portfolios.get(portfolioNumber);
    StrategyData strategy = portfolio.getStrategyByName(strategyName);
    LocalDateTime currentDate = startDate;
    if (endDate == null) {
      endDate = LocalDateTime.now();
    }

    while (currentDate.isBefore(endDate)) {
      if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY){
        currentDate =  currentDate.plusDays(1);
        continue;
      }
      portfolio.investWithStrategy(strategyName, currentDate);

      currentDate = currentDate.plusDays(freaquencyInDays);
    }
  }

  @Override
  public void addStrategyToPortfolio(int portfolioNumber, String strategyName, Map<String, Double> tickerWeightMap, double inverstmentAmount, double commission) {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }
    portfolios.get(portfolioNumber).createAndUpdateStrategy(strategyName, tickerWeightMap, inverstmentAmount, commission);
  }

  @Override
  public List<String> getStrategyListFrompPortfolio(int portfolioNumber) {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }
    return portfolios.get(portfolioNumber).getStrategiesList();
  }


  @Override
  public void investWithStrategy(int portfolioNumber, String strategyName, LocalDateTime investmentDate) {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }
    portfolios.get(portfolioNumber).investWithStrategy(strategyName, investmentDate);
  }

  private void readTransactionFromPortfolioFile(JSONObject jsonObject) {
    JSONArray transactions = (JSONArray) jsonObject.get("transactions");
    Iterator<JSONObject> iterator = transactions.iterator();
    while (iterator.hasNext()) {
      JSONObject stockObj = iterator.next();
      buyStock(
              portfolios.size() - 1,
              LocalDateTime.parse((String) stockObj.get("purchaseDate")),
              (String) stockObj.get("ticker"),
              (double) stockObj.get("costPerUnit") * (double) stockObj.get("quantity"),
              (double) stockObj.get("commission")
      );
    }
  }

  private void readStrategiesFromPortfolioFile(JSONObject jsonObject) {
    if (!jsonObject.containsKey("strategies")){
      return;
    }
    JSONArray strategies = (JSONArray) jsonObject.get("strategies");
    Iterator<JSONObject> iterator = strategies.iterator();
    while (iterator.hasNext()) {
      JSONObject strategyObj = iterator.next();
      addStrategyToPortfolio(
              portfolios.size() - 1,
              (String) strategyObj.get("strategyName"),
              (Map<String, Double>) strategyObj.get("tickerWeightMap"),
              (double) strategyObj.get("investmentAmount"),
              (double) strategyObj.get("commission")
      );
    }
  }


}



