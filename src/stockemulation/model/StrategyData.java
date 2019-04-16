package stockemulation.model;

import java.util.Map;

/**
 * Interface class that holds information of stocks to invest, their weights, a commission value and
 * the total amount to invest. Multiple instances of this interface provides different combinations
 * of these value. The class implementing this will only have the data and the getters. This doesn't
 * hold any information related to the date of investment as the user of this interface can use it
 * for investment on any date.
 */
public interface StrategyData {

  /**
   * Returns the name of the strategy as a String.
   *
   * @return the name of the strategy as a String.
   */
  String getStrategyName();


  /**
   * Returns the total amount to be invested on the stocks using this strategy.
   *
   * @return the total amount to be invested on the stocks using this strategy.
   */
  double getInvestmentAmount();


  /**
   * Returns a map of stock composition for the strategy and the distribution weights on the total
   * investment amount.
   *
   * @return a map of stock composition and the distribution weights.
   */
  Map<String, Double> getTickerAndWeights();


  /**
   * Returns the commission value to pay for the investment.
   * @return the commission value to pay for the investment.
   */
  double getCommission();
}
