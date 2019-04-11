package stockemulation.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test class to test the implementation of the Portfolio interface. The getCompositionSimple()
 * tests are clubbed with buyShare() test.
 */
public class PortfolioImplTest {

  private API testAPI;

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Before
  public void startUp() {
    testAPI = API.getInstance(APITypes.MOCK_API);
  }

  @Test
  public void testConstructorrExceptionTitleNull() {
    try {
      Portfolio testPortfolio = new PortfolioImpl(null, testAPI);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Portfolio title cannot be null or empty string.", e.getMessage());
      // test success
    }
  }

  @Test
  public void testConstructorrExceptionTitleEmpty() {
    try {
      Portfolio testPortfolio = new PortfolioImpl("", testAPI);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("Portfolio title cannot be null or empty string.", e.getMessage());
      // test success
    }
  }

  @Test
  public void testConstructorNormal() {
    Portfolio testPortfolio = new PortfolioImpl("testPortfolio", testAPI);
    assertEquals(
            "Portfolio:" + "testPortfolio"
                    + "Datasource:" + testAPI,
            testPortfolio.toString());
  }


  // bysShares Tests //

  @Test
  public void testBuySharesExceptionInvalidTicker() {
    Portfolio testPortfolio = new PortfolioImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    try {
      testPortfolio.buyShares(null, 300, testTime1);
    } catch (IllegalArgumentException e) {
      assertEquals("Ticker name cannot be empty", e.getMessage());
    }

    try {
      testPortfolio.buyShares("", 300, testTime1);
    } catch (IllegalArgumentException e) {
      assertEquals("Ticker name cannot be empty", e.getMessage());
    }

    try {
      testPortfolio.buyShares("A23", 300, testTime1);
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid format for ticker name", e.getMessage());
    }

    try {
      testPortfolio.buyShares("ABCDEF", 300, testTime1);
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid format for ticker name", e.getMessage());
    }
  }

  @Test
  public void testBuyShareExceptionInvalidDate() {
    Portfolio testPortfolio = new PortfolioImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = null;

    try {
      testPortfolio.buyShares("AAPL", 300, testTime1);
    } catch (IllegalArgumentException e) {
      assertEquals("DateTime of purchase cannot be null", e.getMessage());
    }
  }

  @Test
  public void testBuyShareExceptionInvalidValue() {
    Portfolio testPortfolio = new PortfolioImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    try {
      testPortfolio.buyShares("AAPL", -300, testTime1);
    } catch (IllegalArgumentException e) {
      assertEquals("Price of stock to be bought cannot be negative", e.getMessage());
    }
  }

  @Test
  public void testBuyShareOnEmpty() {
    Portfolio testPortfolio = new PortfolioImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1);
    assertEquals(
            "{AAPL=10.0}",
            testPortfolio.getCompositionSimple().toString());
  }

  @Test
  public void testBuyShareMultiple() {
    Portfolio testPortfolio = new PortfolioImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1);
    testPortfolio.buyShares("GOOG", 3000, testTime1);
    assertEquals(
            "{GOOG=100.0, AAPL=10.0}",
            testPortfolio.getCompositionSimple().toString());
  }


  // getPortfolioTitle Tests //
  @Test
  public void testGetPortfolioTitle() {
    Portfolio testPortfolio = new PortfolioImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    assertEquals("testPortfolio", testPortfolio.getPorfolioTitle());
  }


  // getCompositionSimple Tests //
  // already tested in buyshare


  // getCostBasis Tests //

  @Test
  public void testGetCostBasisBeforeStocksWereBought() {
    Portfolio testPortfolio = new PortfolioImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    LocalDateTime testTime2 = LocalDateTime.parse("2015-04-03T12:32");
    LocalDateTime testTime3 = LocalDateTime.parse("2013-04-04T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1);
    testPortfolio.buyShares("GOOG", 3000, testTime1);
    testPortfolio.buyShares("AAPL", 600, testTime2);
    assertEquals(0, testPortfolio.getCostBasis(testTime3), 0.001);
  }

  @Test
  public void testGetCostBasisAfterAllStocksWereBought() {
    Portfolio testPortfolio = new PortfolioImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    LocalDateTime testTime2 = LocalDateTime.parse("2015-04-03T12:32");
    LocalDateTime testTime3 = LocalDateTime.parse("2015-05-04T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1);
    testPortfolio.buyShares("GOOG", 3000, testTime1);
    testPortfolio.buyShares("AAPL", 600, testTime2);
    assertEquals(3900, testPortfolio.getCostBasis(testTime3), 0.001);
  }

  @Test
  public void testGetCostBasisAfterSomeStocksWereBought() {
    Portfolio testPortfolio = new PortfolioImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    LocalDateTime testTime2 = LocalDateTime.parse("2015-04-03T12:32");
    LocalDateTime testTime3 = LocalDateTime.parse("2014-05-04T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1);
    testPortfolio.buyShares("GOOG", 3000, testTime1);
    testPortfolio.buyShares("AAPL", 600, testTime2);
    assertEquals(3300, testPortfolio.getCostBasis(testTime3), 0.001);
  }


  // getTotalValue Tests //

  @Test
  public void testGetTotalValueBeforerStocksWereBought() {
    Portfolio testPortfolio = new PortfolioImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    LocalDateTime testTime2 = LocalDateTime.parse("2015-04-03T12:32");
    LocalDateTime testTime3 = LocalDateTime.parse("2013-05-04T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1);
    testPortfolio.buyShares("GOOG", 3000, testTime1);
    testPortfolio.buyShares("AAPL", 600, testTime2);
    assertEquals(0, testPortfolio.getTotalValue(testTime3), 0.001);
  }

  @Test
  public void testGetTotalValueAfterSomeStocksWereBought() {
    Portfolio testPortfolio = new PortfolioImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    LocalDateTime testTime2 = LocalDateTime.parse("2015-04-03T12:32");
    LocalDateTime testTime3 = LocalDateTime.parse("2014-05-04T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1);
    testPortfolio.buyShares("GOOG", 3000, testTime1);
    testPortfolio.buyShares("AAPL", 600, testTime2);
    assertEquals(3300, testPortfolio.getTotalValue(testTime3), 0.001);
  }

  @Test
  public void testGetTotalValueAfterStocksWereBought() {
    Portfolio testPortfolio = new PortfolioImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    LocalDateTime testTime2 = LocalDateTime.parse("2015-04-03T12:32");
    LocalDateTime testTime3 = LocalDateTime.parse("2017-04-04T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1);
    testPortfolio.buyShares("GOOG", 3000, testTime1);
    testPortfolio.buyShares("AAPL", 600, testTime2);
    assertEquals(7800, testPortfolio.getTotalValue(testTime3), 0.001);
  }

}
