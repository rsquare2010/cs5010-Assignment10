package stockemulation.model;

import java.util.Map;

import stockemulation.util.StockInfoSanity;

class StrategyDataImpl implements StrategyData {
  private String strategyName;
  private Map<String, Double> tickerAndWeights;
  private double investmentAmount;
  private double commission;

  StrategyDataImpl(
          String strategyName,
          Map<String, Double> tickerAndWeights,
          double investmentAmount,
          double commission
  ) throws IllegalArgumentException{
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
}
