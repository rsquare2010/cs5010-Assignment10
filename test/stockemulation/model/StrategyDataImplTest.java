package stockemulation.model;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StrategyDataImplTest {

  // Constructor Tests //
  @Test
  public void testConstructorValid() {
    Map<String, Double> testTickerweights = new HashMap<>();
    testTickerweights.put("AAPL", 20.0);
    testTickerweights.put("GOOG", 25.0);
    StrategyData testData = new StrategyDataImpl("test", testTickerweights, 30.0, 30.0);
    assertEquals("", testData.toString());
  }

}