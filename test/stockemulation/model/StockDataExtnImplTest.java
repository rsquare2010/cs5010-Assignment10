package stockemulation.model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

/**
 * This is a test class used to test the StockDataExtnImpl class. StockDataExtnImpl is an
 * implementation of the StockDataExtn interface. This interface extends over the StockData
 * interface and adds new methods. The stockDataExtnImpl extends StockDataImpl.
 */
public class StockDataExtnImplTest {
  private LocalDateTime testDate;

  @Before
  public void setUp() {
    testDate = LocalDateTime.parse("2012-06-29T12:30:40");
  }

  @Test
  public void testConstructorNormal() {
    StockDataExtn testStock = new StockDataExtnImpl(
            "APPL", testDate, 10, 10, 10);
    assertEquals(
            "Ticker name:" + "APPL"
                    + " Purchase date:" + "2012-06-29T12:30:40"
                    + " Cost per Stock:" + "10.0"
                    + " Quantity:" + "10.0"
                    + "Commission:" + "10.0",
            testStock.toString()
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testErrorPropogationFromSuper() {
    StockDataExtn testStock = new StockDataExtnImpl(
            "APPL", null, 10, 10, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCommissionValue() {
    StockDataExtn testStock = new StockDataExtnImpl(
            "APPL", testDate, 10, 10, -10);
  }

  @Test
  public void testGetCommissison() {
    StockDataExtn testStock = new StockDataExtnImpl(
            "APPL", testDate, 10, 10, 10);
    assertEquals(10, testStock.getCommission(), 0.001);
  }

}