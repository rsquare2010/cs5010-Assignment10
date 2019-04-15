package stockemulation.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * This is a test class to test {@link ModelExtnImpl} and uses JUni4 as testing framework.
 * getCostBasis uses the same function as base class so only the value after providing commission is
 * checked here. Rest of the tests are in {@link EmulatorModelImplTest}.
 */
public class ModelExtnImplTest {

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Before
  public void setReadFile() throws Exception {
    ModelExtn testModel = new ModelExtnImpl(APITypes.MOCK_API);
    testModel.createPortfolio("sample");
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    testModel.buyStock(0, testTime1, "AAPL", 300.0, 10);

    testModel.writePortfolioToFile("modelread.json", 0);
  }

  // Constructor Tests //

  @Test
  public void testConstructorNormal() {
    ModelExtn testEmulator = new ModelExtnImpl();
    assertEquals("Number of Portfolios: 0", testEmulator.toString());
  }

  @Test
  public void testConstructorChoosingAPI() {
    Model testEmulator = new ModelImpl(APITypes.ALPHA_VANTAGE);
    assertEquals("Number of Portfolios: 0", testEmulator.toString());
  }


  // buyStocks Tests //

  @Test
  public void testBuyStocksExceptionInvalidPortfolioNumber() {
    ModelExtn testEmulator = new ModelExtnImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Invalid Portfolio number");
    testEmulator.buyStock(2, dateTime, "AAPL", 4000.0, 10);
  }

  @Test
  public void testBuyStocksExceptionErrorPropagation() {
    ModelExtn testEmulator = new ModelExtnImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Commission for a transaction cannot be negative");
    testEmulator.buyStock(1, dateTime, "AAPL", 4000.0, -10);
  }


  @Test
  public void testBuyStockValid() {
    ModelExtn testEmulator = new ModelExtnImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    testEmulator.buyStock(1, dateTime, "AAPL", 4000.0, 10);

    Map<String, Double> testMap = new HashMap<>();
    testMap.put("AAPL", 133.33333333333334);
    assertEquals(
            testMap.toString(),
            testEmulator.getPortfolioDetails(1).toString()
    );
  }

  @Test
  public void testBuyStockValidMultiple() {
    ModelExtn testEmulator = new ModelExtnImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    testEmulator.buyStock(1, dateTime, "AAPL", 4000.0, 10);
    testEmulator.buyStock(1, dateTime, "AAPL", 4000.0, 10);
    Map<String, Double> testMap = new HashMap<>();
    testMap.put("AAPL", 266.6666666666667);
    assertEquals(
            testMap.toString(),
            testEmulator.getPortfolioDetails(1).toString()
    );
  }

  // cost basis tests //
  @Test
  public void testCostBasisValid() {
    ModelExtn testEmulator = new ModelExtnImpl(APITypes.MOCK_API);
    testEmulator.createPortfolio("sample1");
    testEmulator.createPortfolio("sample2");
    LocalDateTime dateTime = LocalDateTime.parse("2014-02-03T12:34");
    testEmulator.buyStock(0, dateTime, "AAPL", 4000.0, 10);
    assertEquals(4010, testEmulator.getCostBasis(0, dateTime.plusDays(1)),
            0.001);
  }

  // write Tests //


  @Test(expected = IllegalArgumentException.class)
  public void testWriteException() throws Exception {
    ModelExtn testModel = new ModelExtnImpl(APITypes.MOCK_API);
    testModel.createPortfolio("sample");
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    testModel.buyStock(0, testTime1, "AAPL", 300.0, 10);

    testModel.writePortfolioToFile("", 0);
  }

  @Test(expected = IOException.class)
  public void testWriteExceptionWrongPath() throws Exception {
    ModelExtn testModel = new ModelExtnImpl(APITypes.MOCK_API);
    testModel.createPortfolio("sample");
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    testModel.buyStock(0, testTime1, "AAPL", 300.0, 10);

    testModel.writePortfolioToFile("/usr//modelwrit", 0);
  }

  @Test
  public void testWriteFileContentAsString() throws Exception {
    ModelExtn testModel = new ModelExtnImpl(APITypes.MOCK_API);
    testModel.createPortfolio("sample");
    LocalDateTime testTime1 = LocalDateTime.parse("2014-04-03T12:32");
    testModel.buyStock(0, testTime1, "AAPL", 300.0, 10);

    testModel.writePortfolioToFile("modelwrite.json", 0);
    String contents = new String(Files.readAllBytes(Paths.get("modelwrite.json")));
    assertEquals(
            "{\"title\":\"sample\",\"transactions\":[{\"ticker\":\"AAPL\","
                    + "\"purchaseDate\":\"2014-04-03T12:32\",\"quantity\":10.0,\"commission\""
                    + ":10.0,\"costPerUnit\":30.0}]}",
            contents);
  }


  // Read tests //

  @Test
  public void testReadException() throws Exception {
    ModelExtn testModel = new ModelExtnImpl(APITypes.MOCK_API);

    exception.expect(FileNotFoundException.class);
    testModel.readPortfolioFromFile("modelrad.json");
  }

  @Test
  public void testReadValid() throws Exception {
    ModelExtn testModel = new ModelExtnImpl(APITypes.MOCK_API);
    testModel.readPortfolioFromFile("modelread.json");
    assertEquals("{AAPL=10.0}", testModel.getPortfolioDetails(0).toString());
  }

}