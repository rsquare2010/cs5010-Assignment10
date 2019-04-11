import java.io.InputStreamReader;
import java.io.Reader;

import stockemulation.controller.EmulatorController;
import stockemulation.controller.EmulatorControllerImpl;
import stockemulation.controller.GUIController;
import stockemulation.controller.GUIControllerImpl;
import stockemulation.model.APITypes;
import stockemulation.model.ModelExtn;
import stockemulation.model.ModelExtnImpl;
import stockemulation.view.EmulatorView;
import stockemulation.view.EmulatorViewImpl;
import stockemulation.view.IView;
import stockemulation.view.View;

/**
 * Main method to run the stock emulation application. This is a command line application that takes
 * in input from the user step by step to perform actions like: 1.Create a portfolio. 2.Check
 * contents of a portfolio. 3.Buy stocks. 4.Check value of a portfolio.
 */
public class Main {

  /**
   * The main method which sets up the model, view and controller for the Stock Emulator
   * application.
   *
   * @param args string arguments from the command line.
   */
  public static void main(String[] args) {

    if (args.length == 0) {
      openGUI(APITypes.ALPHA_VANTAGE);
    }

    if (args.length == 1) {
      APITypes apiTypes = getAPITypeFromInput(args[0]);
      openGUI(apiTypes);
    }

    if (args.length == 2) {

      APITypes apiTypes = getAPITypeFromInput(args[0]);
      String uiType = args[1];

      System.out.println(uiType);
      if ("cli".matches(uiType.toLowerCase())) {
        openCLI(apiTypes);
      } else {
        openGUI(apiTypes);
      }
    }
  }

  private static APITypes getAPITypeFromInput(String input) {
    if ("mock".matches(input.toLowerCase())) {
      return APITypes.MOCK_API;
    } else {
      return APITypes.ALPHA_VANTAGE;
    }
  }

  private static void openGUI(APITypes apiTypes) {
    ModelExtn model = new ModelExtnImpl(apiTypes);
    GUIController controller = new GUIControllerImpl();
    IView view = new View("StockEmulator", controller);
    controller.start(model, view);
  }

  private static void openCLI(APITypes apiTypes) {
    ModelExtn model = new ModelExtnImpl(apiTypes);
    Reader reader = new InputStreamReader(System.in);
    EmulatorView view = new EmulatorViewImpl(System.out);
    EmulatorController emulatorController = new EmulatorControllerImpl(reader);
    emulatorController.start(model, view);
  }
}



