package stockemulation.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static junit.framework.TestCase.assertEquals;

/**
 * This is a test class for {@link PortfolioExtnImpl} and uses JUni4 for testing.
 */
public class PortfolioExtnImplTest {

  private API testAPI;

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Before
  public void startUp() {
    testAPI = API.getInstance(APITypes.MOCK_API);
  }


  // Constructor Tests //

  @Test
  public void testConstructorNormal() {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    assertEquals(
            "Portfolio:" + "testPortfolio"
                    + "Datasource:" + testAPI,
            testPortfolio.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorErrorPropagationFromBase() {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("", testAPI);
  }


  // buyShares Tests //

  @Test
  public void testBuySharesExceptionInvalidTicker() {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    try {
      testPortfolio.buyShares(null, 300, testTime1, 10);
    } catch (IllegalArgumentException e) {
      assertEquals("Ticker name cannot be empty", e.getMessage());
    }

    try {
      testPortfolio.buyShares("", 300, testTime1, 10);
    } catch (IllegalArgumentException e) {
      assertEquals("Ticker name cannot be empty", e.getMessage());
    }

    try {
      testPortfolio.buyShares("A23", 300, testTime1, 10);
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid format for ticker name", e.getMessage());
    }

    try {
      testPortfolio.buyShares("ABCDEF", 300, testTime1, 10);
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid format for ticker name", e.getMessage());
    }
  }

  @Test
  public void testBuyShareExceptionInvalidDate() {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = null;

    try {
      testPortfolio.buyShares("AAPL", 300, testTime1, 10);
    } catch (IllegalArgumentException e) {
      assertEquals("DateTime of purchase cannot be null", e.getMessage());
    }
  }

  @Test
  public void testBuyShareExceptionInvalidValue() {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    try {
      testPortfolio.buyShares("AAPL", -300, testTime1, 10);
    } catch (IllegalArgumentException e) {
      assertEquals("Price of stock to be bought cannot be negative", e.getMessage());
    }
  }

  @Test
  public void testBuyShareExceptionInvalidCommisssion() {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    try {
      testPortfolio.buyShares("AAPL", 300, testTime1, -10);
    } catch (IllegalArgumentException e) {
      assertEquals("Commission for a transaction cannot be negative", e.getMessage());
    }
  }


  @Test
  public void testBuyShareOnEmpty() {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1, 10);
    assertEquals(
            "{AAPL=10.0}",
            testPortfolio.getCompositionSimple().toString());
  }

  @Test
  public void testBuyShareMultiple() {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1, 10);
    testPortfolio.buyShares("GOOG", 3000, testTime1, 10);
    assertEquals(
            "{GOOG=100.0, AAPL=10.0}",
            testPortfolio.getCompositionSimple().toString());
  }

  @Test
  public void testBuyShareMultipleDifferentDates() {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    LocalDateTime testTime2 = LocalDateTime.parse("2015-04-03T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1, 10);
    testPortfolio.buyShares("AAPL", 3000, testTime2, 10);
    assertEquals(
            "{AAPL=110.0}",
            testPortfolio.getCompositionSimple().toString());
  }


  // getCostBasis Tests //

  @Test
  public void testGetCostBasisBeforeStocksWereBought() {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    LocalDateTime testTime2 = LocalDateTime.parse("2015-04-03T12:32");
    LocalDateTime testTime3 = LocalDateTime.parse("2013-04-04T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1, 10);
    testPortfolio.buyShares("GOOG", 3000, testTime1, 20);
    testPortfolio.buyShares("AAPL", 600, testTime2, 30);
    assertEquals(0, testPortfolio.getCostBasis(testTime3), 0.001);
  }

  @Test
  public void testGetCostBasisAfterAllStocksWereBought() {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    LocalDateTime testTime2 = LocalDateTime.parse("2015-04-03T12:32");
    LocalDateTime testTime3 = LocalDateTime.parse("2015-05-04T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1, 10);
    testPortfolio.buyShares("GOOG", 3000, testTime1, 20);
    testPortfolio.buyShares("AAPL", 600, testTime2, 30);
    assertEquals(3960, testPortfolio.getCostBasis(testTime3), 0.001);
  }

  @Test
  public void testGetCostBasisAfterSomeStocksWereBought() {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    LocalDateTime testTime2 = LocalDateTime.parse("2015-04-03T12:32");
    LocalDateTime testTime3 = LocalDateTime.parse("2014-05-04T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1, 10);
    testPortfolio.buyShares("GOOG", 3000, testTime1, 10);
    testPortfolio.buyShares("AAPL", 600, testTime2, 10);
    assertEquals(3320, testPortfolio.getCostBasis(testTime3), 0.001);
  }

  // File Write Tests

  @Test(expected = IOException.class)
  public void testWriteException() throws Exception {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1, 10);

    testPortfolio.writeToFile("");
  }

  @Test(expected = IOException.class)
  public void testWriteExceptionWrongPath() throws Exception {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1, 10);

    testPortfolio.writeToFile("/usr/sample/jkjk");
  }

  @Test
  public void testWriteFileContentAsString() throws Exception {
    PortfolioExtn testPortfolio = new PortfolioExtnImpl("testPortfolio", testAPI);
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    testPortfolio.buyShares("AAPL", 300, testTime1, 10);

    testPortfolio.writeToFile("sample.json");
    String contents = new String(Files.readAllBytes(Paths.get("sample.json")));
    assertEquals(
            "{\"title\":\"testPortfolio\",\"transactions\":[{\"ticker\":\"AAPL\","
                    + "\"purchaseDate\":\"2014-04-03T12:32\",\"quantity\":10.0,\"commission\""
                    + ":10.0,\"costPerUnit\":30.0}]}",
            contents);
  }
}