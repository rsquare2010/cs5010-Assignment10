import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import stockemulation.controller.EmulatorController;
import stockemulation.controller.EmulatorControllerImpl;
import stockemulation.model.MockModel;
import stockemulation.model.ModelExtn;
import stockemulation.view.EmulatorView;
import stockemulation.view.EmulatorViewImpl;

import static org.junit.Assert.assertTrue;

/**
 * This is a test class used to test the EmulatorControllerImpl class of the StockEmulator
 * application.
 */
public class ControllerWithMockModelTest {

  private EmulatorController controller;
  private ModelExtn model;
  private EmulatorView view;
  private ByteArrayOutputStream fos;

  @Before
  public void setup() {
    model = new MockModel();
    fos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(fos);
    view = new EmulatorViewImpl(ps);
  }

  @Test
  public void controllerApplicationFirstScreenTest() {
    ByteArrayInputStream in = new ByteArrayInputStream(("").getBytes());
    controller = new EmulatorControllerImpl(new InputStreamReader(in));

    controller.start(model, view);
    String expectedOutput = "Welcome to Stock Emulator application!\n";
    expectedOutput = appendMenu(expectedOutput);
    System.out.println(fos.toString());
    System.out.println(expectedOutput);
    assertTrue(fos.toString().equals(expectedOutput));
  }

  @Test
  public void controllerTestCreatePortfolioScreen() {
    ByteArrayInputStream in = new ByteArrayInputStream(("1 test").getBytes());
    controller = new EmulatorControllerImpl(new InputStreamReader(in));
    controller.start(model, view);
    String expectedOutput = "Enter the name of the portfolio\n";
    expectedOutput += "Portfolio test created successfully\n";
    expectedOutput = appendMenu(expectedOutput);
    assertTrue(fos.toString().endsWith(expectedOutput));
  }

  @Test
  public void controllerTestBuyStocksScreen() {
    ByteArrayInputStream in =
            new ByteArrayInputStream(("1 test 3 1 2014 07 12 12 21 AAPL 5000 10 q").getBytes());
    controller = new EmulatorControllerImpl(new InputStreamReader(in));

    controller.start(model, view);
    System.out.println(fos.toString());
    String expectedOutput = "Stocks Successfully bought\n";
    expectedOutput = appendMenu(expectedOutput);
    assertTrue(fos.toString().endsWith(expectedOutput));
  }

  @Test
  public void controllerTestSummaryPortfolioScreen() {
    ByteArrayInputStream in =
            new ByteArrayInputStream(("1 test 3 1 2014 07 12 12 21 AAPL 5000 10 4 1 2017 02 16 q")
                    .getBytes());
    controller = new EmulatorControllerImpl(new InputStreamReader(in));

    controller.start(model, view);
    String expectedOutput = "Cost basis is: $\t100.0\n";
    expectedOutput += "Value is: $\t500.0\n";
    expectedOutput = appendMenu(expectedOutput);
    System.out.println(fos.toString());
    assertTrue(fos.toString().endsWith(expectedOutput));
  }


  private String appendMenu(String input) {
    StringBuilder sb = new StringBuilder(input);
    sb.append("Supported operations(Enter a number):\n");
    sb.append("1. to create a new portfolio\n");
    sb.append("2. to see the contents of a portfolio\n");
    sb.append("3. to buy stocks\n");
    sb.append("4. to check cost basis and value of a portfolio on a date\n");
    sb.append("5. write to file\n");
    sb.append("6. read from file\n");
    sb.append("7. create a strategy\n");
    sb.append( "8. One time buy with strategy\n");
    sb.append( "9. dollar cost average with strategy\n");
    sb.append("10. read a strategy from a file\n");
    sb.append("11. save a strategy to a file\n");
    sb.append("press \"q\" or \"quit\" to exit this application\n");

    return sb.toString();
  }
}

