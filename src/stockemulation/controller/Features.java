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

  void createAStrategy(String strategyName, Map<String, String> tickerWeights,
                       String price,
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

  /**
   * View callback to trigger a create strategy form.
   */
  void createStrategy();

  /**
   * Close the form which provides the ability to create a new strategy.
   */
  void closeAddStrategyForm();

  /**
   * Perform a single buy operation using a strategy, in  particular portfolio on a given date.
   * @param portfolioIndex the portfolio under which this operation will happen.
   * @param strategyName the strategy used to perform this operation.
   * @param date the date on which this transaction will happen.
   */
  void verifyStrategyFormAndBuy(int portfolioIndex, String strategyName, Date date);

  /**
   * Show the form to perform a single buy operation using weights.
   * @param portfolioIndex the portfolio under which this buy operation happens.
   */
  void showSingleBuyStrategyForm(int portfolioIndex);

  /**
   * Close the form which helps perform a single buy operation using strategies.
   */
  void closeSingleStrategyBuyForm();

  /**
   * Verify if the date entered for an operation is valid.
   * @param date the date to be verified.
   */
  void verifyDatesForSingleStrategyBuyForm(Date date);

  /**
   * Show the form which helps perform the dollar cost average investment strategy.
   * @param portfolioIndex the portfolio under which this investment should happen.
   */
  void showDollarCostAverageForm(int portfolioIndex);

  /**
   * Close the form which helps perform the dollar cost average investment strategy.
   */
  void closeDollarCostAverageForm();

  /**
   * Verify the different inputs and perform the dollar cost average investment strategy buy.
   * @param portfolioIndex the portfolio under which the transaction happens.
   * @param strategyName the strategy used to perform the operation.
   * @param startDate the date on which the DCA starts.
   * @param endDate the date on which the DCA ends.
   * @param frequency the number of days between every periodic investment.
   */
  void verifyAndBuyDollarCostAverage(int portfolioIndex, String strategyName, Date startDate,
                                     Date endDate,
                                     String frequency);

  void verifyStartDateForDCA(Date date);

  void verifyEndDateForDCA(Date startDate, Date endDate);

  void verifyInterval(Date startDate, Date endDate, String interval);

  void saveStrategy(String filePath, String strategyName);

  void readStrategyFromFile(String filePath);

  void showSelectStrategyForm();

}
