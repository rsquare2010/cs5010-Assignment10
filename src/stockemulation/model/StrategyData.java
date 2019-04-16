package stockemulation.model;

import java.util.Map;

public interface StrategyData {

  String getStrategyName();

  double getInvestmentAmount();

  Map<String, Double> getTickerAndWeights();

  double getCommission();

}
