package stockemulation.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * This is a test class used to test ModelImpl which is an implementation of the Emulator
 * Model class.
 */
public class EmulatorModelImplTest {

  @Rule
  public final ExpectedException exception = ExpectedException.none();


  // Constructor Tests //

  @Test
  public void testConstructor() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    assertEquals("Number of Portfolios: 0", testEmulator.toString());
  }

  // createPortfolio Tests //

  @Test
  public void testCreatePortfolioOnEmpty() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample");
    List<String> testStringList = new LinkedList<>();
    testStringList.add("sample");
    assertEquals(testStringList.toString(), testEmulator.getPortfolioList().toString());
  }

  @Test
  public void testCreatePortfolioOnNonEmpty() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");

    List<String> testStringList = new LinkedList<>();
    testStringList.add("sample1");
    testStringList.add("sample2");
    assertEquals(
            testStringList.toString(),
            testEmulator.getPortfolioList().toString());
  }

  @Test
  public void testCreatePortfolioExceptionOnExisting() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Portfolio title already exists");
    testEmulator.createPortfolio("sample1");

  }


  // buyStock Tests //

  @Test
  public void testBuyStockValid() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    testEmulator.buyStock(1, dateTime, "AAPL", 4000.0);

    Map<String, Double> testMap = new HashMap<>();
    testMap.put("AAPL", 133.33333333333334);
    assertEquals( testMap.toString(),
            testEmulator.getPortfolioDetails(1).toString()
    );
  }

  @Test
  public void testBuyStockValidMultiple() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    testEmulator.buyStock(1, dateTime, "AAPL", 4000.0);
    testEmulator.buyStock(1, dateTime, "AAPL", 4000.0);

    Map<String, Double> testMap = new HashMap<>();
    testMap.put("AAPL", 266.6666666666667);
    assertEquals(
            testMap.toString(),
            testEmulator.getPortfolioDetails(1).toString()
    );
  }

  @Test
  public void testBuyStockInvalidPortfolioNumber() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Invalid Portfolio number");
    testEmulator.buyStock(2, dateTime, "AAPL", 4000.0);
  }

  @Test
  public void testBuyStockInvalidPortfolioNumberNegative() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Invalid Portfolio number");
    testEmulator.buyStock(-2, dateTime, "AAPL", 4000.0);
  }

  @Test
  public void testBuyStockInvalidTickerName() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Invalid format for ticker name");
    testEmulator.buyStock(1, dateTime, "AAPL3", 4000.0);

  }

  @Test
  public void testBuyStockInvalidValue() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Price of stock to be bought cannot be negative");
    testEmulator.buyStock(1, dateTime, "AAPL", -4000.0);

  }

  @Test
  public void testBuyStockExceptionNullTickerOrDateTime() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Ticker name cannot be empty");
    testEmulator.buyStock(1, dateTime, null, 4000.0);

  }


  // getCostBasis Tests //

  @Test
  public void testGetCostBasisInvalidPortfolioNumber() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Invalid Portfolio number");
    testEmulator.buyStock(2, dateTime, "AAPL", 4000.0);
  }

  @Test
  public void testCostBasisInvalidPortfolioNumberNegative() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Invalid Portfolio number");
    testEmulator.buyStock(-2, dateTime, "AAPL", 4000.0);
  }

  @Test
  public void testCostBasisValid() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    testEmulator.buyStock(1, dateTime, "AAPL", 3000.0);
    testEmulator.buyStock(1, dateTime, "GOOG", 3000.0);
    assertEquals(
            6000.0,
            testEmulator.getCostBasis(1, dateTime.plusDays(1)),
            0.001
    );
  }


  // getTotalValue Tests //

  @Test
  public void testGetTotalValueInvalidPortfolioNumber() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Invalid Portfolio number");
    testEmulator.buyStock(2, dateTime, "AAPL", 4000.0);
  }

  @Test
  public void testGetTotalValueInvalidPortfolioNumberNegative() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Invalid Portfolio number");
    testEmulator.buyStock(-2, dateTime, "AAPL", 4000.0);
  }

  @Test
  public void testGetTotalValueBasisValid() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    testEmulator.buyStock(1, dateTime, "AAPL", 3000.0);
    testEmulator.buyStock(1, dateTime, "GOOG", 3000.0);
    assertEquals(
            6000.0,
            testEmulator.getTotalValue(1, dateTime.plusDays(1)),
            0.01
    );
  }

  @Test
  public void testGetTotalValueBasisValidLaterDate() {
    Model testEmulator = new ModelImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    LocalDateTime dateTime1 = LocalDateTime.parse("2017-02-03T12:34");
    testEmulator.buyStock(1, dateTime, "AAPL", 3000.0);
    testEmulator.buyStock(1, dateTime, "GOOG", 3000.0);
    assertEquals(12000.0,
            testEmulator.getTotalValue(1, dateTime1.plusDays(1)),
            0.01
    );
  }

}