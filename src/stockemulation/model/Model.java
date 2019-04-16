package stockemulation.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * An interface that represents the operations performed by a model object of the Stock Market
 * Emulator application. This allows a user to perform operations like: 1.Create a portfolio.
 * 2.Check contents of a portfolio. 3.Buy stocks. 4.Check value of a portfolio.
 */
public interface Model {


  // CORE FEATURES //

  /**
   * This lets the controller create a new stock portfolio of Portfolio type for the user. This
   * takes in the name/title of the portfolio and associates the portfolio with that name by
   * assigning it while creation. If the portfolio name already exists then it throws an error. It
   * also assigns the data source for that portfolio.
   *
   * @param portfolioName the name/title of the portfolio.
   * @return true if portfolio creation was successful.
   * @throws IllegalArgumentException if the name already exists.
   */
  boolean createPortfolio(String portfolioName)
          throws IllegalArgumentException;

  /**
   * This returns a string containing the composition of the requested portfolio. The portfolio is
   * requested by providing the position of the portfolio as and how it was added. Throws an error
   * if this position is invalid. The description it returns could be detailed or simple based on
   * the value set by the isSummary argument.
   *
   * @param portfolioNumber the position of the portfolio in the order it was added.
   * @return the string containing information of the composition of the selected portfolio.
   * @throws IllegalArgumentException if the portfolio number is invalid.
   */
  Map<String, Double> getPortfolioDetails(int portfolioNumber)
          throws IllegalArgumentException;


  /**
   * This methods lets the controller add a stock purchase to the selected portfolio. In the process
   * if any error occurred it is propagated to the controller to be handled. The stock purchase
   * include the following details: the name of the portfolio in which the purchase has to be added,
   * the stock name, date of purchase, the worth of stocks that have to bought.
   *
   * @param portfolioNumber the portfolio to which the purchase has to be made.
   * @param date            the date at which the purchase has to be made.
   * @param ticker          the name of the stock.
   * @param price           the total value of the stock that has to be bought in that purchase.
   * @throws IllegalArgumentException if any of the input is invalid.
   * @throws IllegalArgumentException if any of the input causes member class to throw and error.
   */
  void buyStock(int portfolioNumber, LocalDateTime date, String ticker, Double price)
          throws IllegalArgumentException;


  /**
   * Returns the cost basis of the given portfolio as a string on a selected date. If existing
   * stocks were not purchased on or before that date they wont be counted. Cost basis is the total
   * cost of the stock at the time of purchase.
   *
   * @param portfolioNumber the portfolio to which the purchase has to be made.
   * @param specifiedDate   the date at which the purchase has to be made.
   * @return A string that represents the cost basis of a portfolio on a given date.
   * @throws IllegalArgumentException if any of the input is invalid.
   * @throws IllegalArgumentException if any of the input causes member class to throw and error.
   */
  double getCostBasis(int portfolioNumber, LocalDateTime specifiedDate)
          throws IllegalArgumentException;


  /**
   * Returns the cost basis of the given portfolio as a string on a selected date. If existing
   * stocks were not purchased on or before that date they wont be counted. Total value is the sum
   * price of the stock at the specified time.
   *
   * @param portfolioNumber the portfolio to which the purchase has to be made.
   * @param specifiedDate   the date at which the purchase has to be made.
   * @return A string that represents the value of a portfolio on a given date.
   * @throws IllegalArgumentException if any of the input is invalid.
   * @throws IllegalArgumentException if any of the input causes member class to throw and error.
   */
  double getTotalValue(int portfolioNumber, LocalDateTime specifiedDate)
          throws IllegalArgumentException;


  // EXTRA FEATURES //

  /**
   * This returns a string of the list of portfolios in the order in which they were added.
   *
   * @return a string of the list of portfolios in the order in which they were added.
   */
  List<String> getPortfolioList();

  /**
   * Returns a count of all the portfolios currently present in the model.
   *
   * @return a count of all the portfolios currently present in the model.
   */
  int getPortfolioCount();
}
