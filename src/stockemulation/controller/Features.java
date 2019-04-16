package stockemulation.controller;

import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

/**
 * This interface represents a set of features that the program offers. Each
 * feature is exposed as a function in this interface. This function is used
 * suitably as a callback by the view, to pass control to the controller.
 * Each function is designed to take in the necessary data to complete that
 * functionality.
 */
public interface Features {

  /**
   * Create a new portfolio with portfolioName as the name of the portfolio.
   * @param portfolioName the name of the portfolio to be created.
   */
  void createPortfolio(String portfolioName);


  void verifyDatesForBuyForm(Date date);

  void verifyTimeForBuyForm(LocalTime time);

  void verifyTickerForBuyForm(String ticker);

  void verifyCostForBuyForm(String price);

  void verifyCommissionForBuyForm(String commission);

  void verifyPriceForStrategyForm(String price);

  void verifyCommissionForStrategyForm(String commission);

  void verifyTickerNameForStrategyForm(String tickerName);

  void createAStrategy(String strategyName, Map<String, String> tickerWeights, String price,
                       String commission);



  /**
   * Show UI to the user to facilitate them to buy stocks.
   */
  void buyStocks();

  /**
   * Verify the information provided as parameters and then buy stocks if information is valid.
   * @param date the date of purchase of stocks.
   * @param time the time at which stocks have to be bought.
   * @param ticker the ticker symbol.
   * @param cost the cost in US Dollars of the amount of stocks to be bought
   * @param commission the commission fees for this transaction.
   * @param portfolioIndex the index vale of the portfolio for which this transaction occurs.
   */
  void verifyFormAndBuy(Date date, LocalTime time, String ticker, String cost,
                        String commission, int portfolioIndex);

  /**
   * Close the UI which helped the user to buys new stocks.
   */
  void closeBuyForm();

  /**
   * Display the contents of a portfolio to the user whose portfolio index is passed as parameter.
   * @param portfolioIndex the portfolio whose contents have to be displayed.
   */
  void getPortfolioSummary(int portfolioIndex);

  /**
   * Display the cost basis and value of a portfolio on a given date.
   * @param portfolioIndex the index of the portfolio whose cost basis and value have to be shown.
   * @param date the date on which the cost basis and value of portfolio need to be shown.
   */
  void getPortfolioValue(int portfolioIndex, Date date);

  /**
   * Write the contents of a portfolio to a given file path.
   * @param filePath the location where the contents of a portfolio have to be written.
   * @param portfolioIndex the index of the portfolio whose contents have to be written to a file.
   */
  void writeToFile(String filePath, int portfolioIndex);

  /**
   * Read the contents of a file and add as a portfolio and update the UI.
   * @param filePath the location of the file which has to be read.
   */
  void readFromFile(String filePath);

  void createStrategy();

  void closeStrategyForm();

}
