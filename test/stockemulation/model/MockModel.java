package stockemulation.model;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This is an implementation of the Model interface. It is a mock model used to test the controller
 * class.
 */
public class MockModel implements ModelExtn {

  @Override
  public Map<String, Double> getPortfolioDetails(int portfolioNumber)
          throws IllegalArgumentException {
    Map<String, Double> outMap = new HashMap<>();
    return outMap;
  }

  @Override
  public double getCostBasis(int portfolioNumber, LocalDateTime specifiedDate)
          throws IllegalArgumentException {
    return 100;
  }

  @Override
  public double getTotalValue(int portfolioNumber, LocalDateTime specifiedDate)
          throws IllegalArgumentException {
    return 500;
  }

  @Override
  public void buyStock(int portfolioNumber, LocalDateTime date, String ticker, Double price)
          throws IllegalArgumentException {
    //Not doing anything here.
  }

  @Override
  public void buyStock(int portfolioNumber, LocalDateTime date, String ticker, Double price,
                       double commission) throws IllegalArgumentException {
    if (date.getYear() != 2014 || date.getMonthValue() != 07 || date.getDayOfMonth() != 12) {
      throw new IllegalArgumentException("date was modified");
    }
    if (portfolioNumber != 1 || date.getHour() != 12 || date.getMinute() != 21) {
      throw new IllegalArgumentException("date was modified");
    }
    if (!ticker.matches("AAPL")) {
      throw new IllegalArgumentException("ticker was changed");
    }
    if (price != 5000 || commission != 10) {
      throw new IllegalArgumentException("prices were changed");
    }
  }

  @Override
  public void addStock(int portfolioNumber, LocalDateTime date, String ticker, double costPerUnit,
                       double quantity, double commission) throws IllegalArgumentException {
    //Not do anything here.
  }

  @Override
  public boolean createPortfolio(String portfolioName) {
    return "test".matches(portfolioName);
  }

  @Override
  public List<String> getPortfolioList() {
    List<String> outStringList = new LinkedList<>();
    outStringList.add("First");
    outStringList.add("'Second");
    return outStringList;
  }

  @Override
  public int getPortfolioCount() {
    return 2;
  }

  @Override
  public void writePortfolioToFile(String filepath, int portfolioNumber)
          throws IllegalArgumentException, IOException {
    //Not do anything here.
  }

  @Override
  public void readPortfolioFromFile(String filepath)
          throws IllegalArgumentException, IOException, ParseException {
    //Not do anything here.
  }

  @Override
  public void investWithStrategy(int portfolioNumber, String strategyName,
                                 LocalDateTime investmentDate) {
    //Not do anything here.
  }

  @Override
  public void dollarCostAveraging(int portfolioNumber, String strategyName, LocalDateTime startDate,
                                  LocalDateTime endDate, int frequencyInDays) {
    //Not do anything here.
  }

  @Override
  public void addStrategyToPortfolio(int portfolioNumber, String strategyName, Map<String, Double>
          tickerWeightMap, double inverstmentAmount, double commission) {
    //Not do anything here
  }

  @Override
  public List<String> getStrategyListFrompPortfolio(int portfolioNumber) {
    return null;
  }
}
