package stockemulation.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;


/**
 * This is a test suite to test stock data implementation and uses JUnit4 testing framework.
 */
public class StockDataImplTest {

  private LocalDateTime testDate;

  @Rule
  public final ExpectedException exception = ExpectedException.none();


  @Before
  public void setUp() {
    testDate = LocalDateTime.parse("2012-06-29T12:30:40");
  }


  // Constructor Tests //

  @Test
  public void testConstructorExceptionTickerNameNull() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Ticker name cannot be empty");
    StockData testStock = new StockDataImpl(
            null, testDate, 10, 10);
  }

  @Test
  public void testConstructorExceptionPurchaseDateNull() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("DateTime of purchase cannot be null");
    StockData testStock = new StockDataImpl(
            "APPL", null, 10, 10);
  }

  @Test
  public void testConstructorExceptionTicketNameInvalidFormat() {
    // Empty string
    try {
      StockData testStock = new StockDataImpl(
              "", testDate, 10, 10);
      fail("Test should have thrown exception but didn't");
    } catch (IllegalArgumentException e) {
      assertEquals("Ticker name cannot be empty", e.getMessage());
    }

    // Exceeding length
    try {
      StockData testStock = new StockDataImpl(
              "APPLED", testDate, 10, 10);
      fail("Test should have thrown exception but didn't");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid format for ticker name", e.getMessage());
    }

    // Invalid characters
    try {
      StockData testStock = new StockDataImpl(
              "APP3L", testDate, 10, 10);
      fail("Test should have thrown exception but didn't");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid format for ticker name", e.getMessage());
    }

    // Invalid characters small cases
    try {
      StockData testStock = new StockDataImpl(
              "appl", testDate, 10, 10);
      fail("Test should have thrown exception but didn't");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid format for ticker name", e.getMessage());
    }
  }

  @Test
  public void testConstructorInvalidCostPriceNegative() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Cost price or quantity cannot be negative");
    StockData testStock = new StockDataImpl(
            "APPL", testDate, -10, 10);
  }

  @Test
  public void testConstructorInvalidQuantityNegative() {
    exception.expect(IllegalArgumentException.class);
    exception.expectMessage("Cost price or quantity cannot be negative");
    StockData testStock = new StockDataImpl(
            "APPL", testDate, 10, -10);
  }

  @Test
  public void testConstructorNormal() {
    StockData testStock = new StockDataImpl(
            "APPL", testDate, 10, 10);
    assertEquals(
            "Ticker name:" + "APPL"
                    + " Purchase date:" + "2012-06-29T12:30:40"
                    + " Cost per Stock:" + "10.0"
                    + " Quantity:" + "10.0",
            testStock.toString()
    );
  }


  // getName Tests //

  @Test
  public void testGetName() {
    StockData testStock = new StockDataImpl(
            "APPL", testDate, 10, 10);
    assertEquals("APPL", testStock.getName());
  }


  // getPurchaseDate Tests //

  @Test
  public void testGetPurchaseDate() {
    StockData testStock = new StockDataImpl(
            "APPL", testDate, 10, 10);
    assertEquals("2012-06-29T12:30:40", testStock.getPurchaseDate().toString());
  }


  //  getCostPrice Tests //

  @Test
  public void testGetCostPrice() {
    StockData testStock = new StockDataImpl(
            "APPL", testDate, 10, 90);
    assertEquals(10.0, testStock.getCostPrice(), 0.0001);
  }


  // getQuantity Tests //

  @Test
  public void testGetQuantity() {
    StockData testStock = new StockDataImpl(
            "APPL", testDate, 10, 90);
    assertEquals(90.0, testStock.getQuantity(), 0.0001);
  }
}