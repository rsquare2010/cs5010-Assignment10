package stockemulation.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


/**
 * This is a test class for {@link  StrategyData} and uses JUnit4 testing framework.
 */
import static org.junit.Assert.assertEquals;

public class StrategyDataImplTest {
  private Map<String, Double> testTickerweights;
  private StrategyData testData;

  @Before
  public void startup() {
    testTickerweights = new HashMap<>();
    testTickerweights.put("AAPL", 20.0);
    testTickerweights.put("GOOG", 25.0);
    testData = new StrategyDataImpl("test", testTickerweights, 30.0, 30.0);
  }


  // Getters Test //

  @Test
  public void testGetStrategyName() {
    assertEquals("test", testData.getStrategyName());
  }

  @Test
  public void testGetInvestAmount() {
    assertEquals(30.0, testData.getInvestmentAmount(), 0.001);
  }

  @Test
  public void testGetTickerAndWeights() {
    Map<String, Double> sample = new HashMap<>();
    sample.put("AAPL", 20.0);
    sample.put("GOOG", 25.0);
    assertEquals(sample.toString(), testData.getTickerAndWeights().toString());
  }

  @Test
  public void testGetCommission() {
    assertEquals(30.0, testData.getCommission(), 0.001);
  }
}