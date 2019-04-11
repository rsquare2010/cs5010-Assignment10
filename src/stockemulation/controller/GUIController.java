package stockemulation.controller;

import stockemulation.model.ModelExtn;
import stockemulation.view.IView;

/**
 * Controller class used in the Stock emulation application. An implementation of this class will
 * contain methods to process and validate and handle callbacks from a View that supports GUI. It
 * performs business logic operations on valid user input and generates results for the user to act
 * on top of it. It generates results with the help fo the {@link ModelExtn}. The generated results
 * are passed to the {@link IView} to be displayed to the user in a GUI.
 */
public interface GUIController {

  /**
   * Start the stock Market application which takes in an implementation of the {@link ModelExtn}
   * and an implementation of the {@link IView} interface. An instance of the controller takes in
   * user input as callbacks from the view. Performs operations on it with the help of the model and
   * sends the results to be displayed to the user via the view.
   * @param model an implementation of the  {@link ModelExtn} interface.
   * @param view  an implementation of the  {@link IView} interface.
   */
  void start(ModelExtn model, IView view);
}
