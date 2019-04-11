package stockemulation.controller;

import stockemulation.model.ModelExtn;
import stockemulation.view.EmulatorView;

/**
 * Controller class used in the Stock emulation application. An implementation of this class will
 * contain methods to process and validate input from the user. It performs business logic
 * operations on valid user input and generates results for the user to act on top of it. It
 * generates results with the help fo the {@link ModelExtn}. The generated results are passed
 * to the {@link EmulatorView} to be displayed to the user in an appropriate format.
 */
public interface EmulatorController {

  /**
   * Start the stock Market application which takes in an implementation of the
   * {@link ModelExtn} and an implementation of the {@link EmulatorView} interface. An
   * instance of the controller takes in user input. Performs operations on it with the help of
   * the model and sends the results to be displayed to the user via the view.
   * @param model an implementation of the  {@link ModelExtn} interface.
   * @param view an implementation of the  {@link EmulatorView} interface.
   */
  void start(ModelExtn model, EmulatorView view);

}
