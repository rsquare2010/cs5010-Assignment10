package stockemulation.view;

import java.io.PrintStream;

/**
 * An implementation of {@link EmulatorView} interface. This implementation helps the application
 * provide information to the user through the command line terminal.
 */
public class EmulatorViewImpl implements EmulatorView {

  private PrintStream printStream;

  /**
   * Create an instance of {@link EmulatorViewImpl} class. This takes in an output stream to
   * which the output of the application are written.
   * @param printer an object of {@link PrintStream} class.
   * @throws IllegalArgumentException is thrown if the printer passed is null.
   */
  public EmulatorViewImpl(PrintStream printer) throws IllegalArgumentException {
    if (printer != null) {
      this.printStream = printer;
    } else {
      throw new IllegalArgumentException("Appendable can't be null");
    }
  }

  /**
   * Takes in a message to be displayed to the user via the command line interface on a new line.
   *
   * @param output the information to be conveyed to the user. If null is passed, "null" string
   *               will be printed.
   */
  @Override
  public void showMessage(String output) throws IllegalArgumentException {
    printStream.println(output);
  }
}
