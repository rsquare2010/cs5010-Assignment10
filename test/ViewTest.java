import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import stockemulation.view.EmulatorView;
import stockemulation.view.EmulatorViewImpl;

import static org.junit.Assert.assertTrue;

/**
 * This is a test class used to test the View part fo the Stock emulator application by testing
 * the EmulatorViewImpl class.
 */
public class ViewTest {

  private EmulatorView view;

  @Test
  public void testIfViewPrintsTheRightInput() {
    ByteArrayOutputStream fos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(fos);

    view = new EmulatorViewImpl(ps);
    String testString = "testMessage";
    view.showMessage(testString);
    assertTrue(fos.toString().equals(testString + "\n"));
  }

  @Test
  public void testNonStringWithWhitespaceInput() {
    ByteArrayOutputStream fos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(fos);

    view = new EmulatorViewImpl(ps);
    String testString = "testMessage\n\n\t\t";
    view.showMessage(testString);
    assertTrue(fos.toString().equals(testString + "\n"));
  }

  @Test
  public void testMultipleStringCalls() {
    ByteArrayOutputStream fos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(fos);

    view = new EmulatorViewImpl(ps);
    String testString = "testMessage\n\n\t\t";
    String testString1 = "part1";
    String testString2 = "part2";
    view.showMessage(testString);
    view.showMessage(testString1);
    view.showMessage(testString2);
    assertTrue(fos.toString().equals(testString + "\n" + testString1 + "\n" + testString2 + "\n"));
  }
}
