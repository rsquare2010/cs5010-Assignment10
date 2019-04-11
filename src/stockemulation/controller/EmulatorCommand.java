package stockemulation.controller;

import stockemulation.model.ModelExtn;
import stockemulation.view.EmulatorView;

/**
 * Interface for all the commands supported by the Emulator Controller. an implementation of this
 * class represents a specific command, which takes in inputs, performs business logic related
 * operations using the model and displays the results using the view. This interface is an atomic
 * version of the controller in that it supports one specific command.
 */
public interface EmulatorCommand {

  /**
   * Perform a command specific action. An impementation of this method will aim to perform the
   * actions of a single command eg: buy stocks or create a portfolio. This method takes in an
   * Emulator model to help perform the actions and generate a result. The method also takes in
   * an EmulatorView to convey the result to the user.
   * @param model This method takes in an Emulator model to help perform the actions and generate
   *             a result.
   * @param view The method also takes in an EmulatorView to convey the result to the user as
   *             well as to provide prompts to get user input to perform actions.
   */
  void execute(ModelExtn model, EmulatorView view);
}
