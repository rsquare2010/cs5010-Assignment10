package stockemulation.view;

/**
 * An interface that represents the view module of the stock market emulator application. A
 * view helps the application communicate with the user by providing questions/information to the
 * user.
 */
public interface EmulatorView {

  /**
   * Helps write a message passed to this method to an output stream. The message will be added
   * to the output stream on a new line.
   * @param output a string that has to be added the output stream on a new line.
   */
  void showMessage(String output) throws IllegalArgumentException, IllegalStateException;
}
