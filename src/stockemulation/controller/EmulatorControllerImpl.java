package stockemulation.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import stockemulation.controller.commands.BuyWithComissionCommand;
import stockemulation.controller.commands.CreatePortfolioCommand;
import stockemulation.controller.commands.DetailPortfolioCommand;
import stockemulation.controller.commands.LoadPortfolioFromFileCommand;
import stockemulation.controller.commands.OpenPortfolioCommand;
import stockemulation.controller.commands.WritePortfolioToFileCommand;
import stockemulation.model.ModelExtn;
import stockemulation.view.EmulatorView;

/**
 * An implementation of the {@link EmulatorController}. This class contains methods to take and
 * validate input from the user in the contest of a stock market emulator application. It further
 * performs actions based on the user's input and generates information to be displayed to the user.
 * This generated information is passed to the {@link EmulatorView} from this class.
 */
public class EmulatorControllerImpl implements EmulatorController {

  private Map<String, Function<Scanner, EmulatorCommand>> knownCommands = new HashMap<>();
  private Readable readable;

  /**
   * Create an instance of {@link EmulatorControllerImpl} class which takes in inputs of different
   * types i.e string, int, double.... from the user. Input is passed to as a Readable object, it is
   * processed one by one based on the action being performed.
   *
   * @param readable takes in an input of type Readable to obtain input for the application.
   * @throws IllegalArgumentException when the readable passed to this object is null.
   */
  public EmulatorControllerImpl(Readable readable) throws IllegalArgumentException {
    if (readable == null) {
      throw new IllegalArgumentException("readable or appendable cannot be null");
    }

    this.readable = readable;
  }

  @Override
  public void start(ModelExtn model, EmulatorView view) {
    view.showMessage("Welcome to Stock Emulator application!");
    Scanner scanner = new Scanner(this.readable);

    showMenu(view);
    knownCommands.put("1", s -> new CreatePortfolioCommand(s));
    knownCommands.put("2", s -> new OpenPortfolioCommand(s));
    knownCommands.put("3", s -> new BuyWithComissionCommand(s));
    knownCommands.put("4", s -> new DetailPortfolioCommand(s));
    knownCommands.put("5", s -> new WritePortfolioToFileCommand(s));
    knownCommands.put("6", s -> new LoadPortfolioFromFileCommand(s));


    while (scanner.hasNext()) {
      EmulatorCommand c;
      String in = scanner.next();
      if (in.equalsIgnoreCase("q") || in.equalsIgnoreCase("quit")) {
        return;
      }

      Function<Scanner, EmulatorCommand> cmd = knownCommands.getOrDefault(in,
              null);
      if (cmd == null) {
        view.showMessage("Please enter a valid command.");
      } else {
        c = cmd.apply(scanner);
        c.execute(model, view);
      }
      showMenu(view);
    }
  }

  protected void showMenu(EmulatorView view) {
    view.showMessage("Supported operations(Enter a number):");
    view.showMessage("1. to create a new portfolio");
    view.showMessage("2. to see the contents of a portfolio");
    view.showMessage("3. to buy stocks");
    view.showMessage("4. to check cost basis and value of a portfolio on a date");
    view.showMessage("5. write to file");
    view.showMessage("6. read from file");
    view.showMessage("press \"q\" or \"quit\" to exit this application");
  }
}