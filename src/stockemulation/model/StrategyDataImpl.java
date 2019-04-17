package stockemulation.model;

import java.util.Map;

import stockemulation.util.StockInfoSanity;

/**
 * Implementation class for {@link StrategyData}. This holds the information strategy name, stock
 * composition and their distribution weights as member variables.
 */
class StrategyDataImpl implements StrategyData {
  private String strategyName;
  private Map<String, Double> tickerAndWeights;
  private double investmentAmount;
  private double commission;

  /**
   * Constructor for this implementation that sets the values for the member data.
   *
   * @param strategyName     the name of this strategy.
   * @param tickerAndWeights map of the stock composition and their distribution weights,
   * @param investmentAmount the total amount to be invested in this strategy.
   * @param commission       the commission amount to be paid to make this investment.
   * @throws IllegalArgumentException if the names of the inputs are invalid.
   */
  StrategyDataImpl(
          String strategyName,
          Map<String, Double> tickerAndWeights,
          double investmentAmount,
          double commission
  ) throws IllegalArgumentException {
    if (strategyName == null || strategyName.equals("")) {
      throw new IllegalArgumentException("Strategy name cannot be null or empty");
    }
    if (tickerAndWeights == null) {
      throw new IllegalArgumentException("Ticker name and weights map for strategy cannot be null");
    }
    StockInfoSanity.isPriceValid(investmentAmount);
    StockInfoSanity.isCommissionValid(commission);

    this.strategyName = strategyName;
    this.tickerAndWeights = tickerAndWeights;
    this.investmentAmount = investmentAmount;
    this.commission = commission;
  }

  @Override
  public String getStrategyName() {
    return strategyName;
  }

  @Override
  public double getInvestmentAmount() {
    return investmentAmount;
  }

  @Override
  public Map<String, Double> getTickerAndWeights() {
    return tickerAndWeights;
  }

  @Override
  public double getCommission() {
    return commission;
  }

  @Override
  public String toString() {
    return "{\"strategyName\":\"" + strategyName
            + "\",\"investmentAmount\":" + investmentAmount
            + ",\"commission\":" + commission
            + ",\"tickerWeightsMap\":" + tickerAndWeights.toString() + "}";
  }
}
