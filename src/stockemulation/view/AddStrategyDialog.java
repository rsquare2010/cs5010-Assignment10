package stockemulation.view;

import java.awt.Frame;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;

import stockemulation.controller.Features;
import stockemulation.controller.GUIController;

/**
 * This is custom Dialog created by extending the JDialog class. It generates a form to collect
 * information from the user to facilitate the buy stocks operation.
 */
class AddStrategyDialog extends CustomDialog {

  private JTextField strategyNameTextField;
  private JTextField commissionTextField;
  private JTextField tickerTextField;
  private JTextField priceTextField;

  private JLabel tickerErrorLabel;
  private JLabel strategyNameErrorLabel;
  private JLabel commissionErrorLabel;
  private JLabel priceErrorLabel;

  private TickerSet weightSet;
  private List<String> tickerList;


  /**
   * Create an instance of this class by providing the parent Frame, the window title as a string
   * and an instance of the GUIController.
   *
   * @param frame      the parent from over which this dialog pops up.
   * @param text       the title of this Dialog box.
   * @param controller an instance of the GUIController class.
   */
  AddStrategyDialog(Frame frame, String text, GUIController controller) {
    super(frame, text);


    initialiseFields();

    addComponentsToRootPanel("Enter the name of the strategy", strategyNameErrorLabel,
            strategyNameTextField);
    weightSet = new TickerSet();
    weightSet.setAlignmentX(SwingConstants.CENTER);
    addComponentsToRootPanel("Ticker name and weight of the stock", tickerErrorLabel,
            weightSet);
    addComponentsToRootPanel("Please enter the amount you want to invest",
            priceErrorLabel, priceTextField);

    addComponentsToRootPanel("Commission for this transaction", commissionErrorLabel,
            commissionTextField);
    addOptionsToRootPanel();

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setViewportView(rootPanel);
    getContentPane().add(scrollPane);

  }

  void setFeatures(Features f) {
    Map<String, Runnable> formValidation = getFormValidationListeners(f);
    Map<String, Runnable> hideError = getHideErrorListeners();

    FocusListener inputVerificationListener = getFormFocusListener(formValidation, hideError);
    addFocusListenerToUIComponents(inputVerificationListener);

    yesButton.addActionListener(l -> f.createAStrategy(strategyNameTextField.getText(),
            weightSet.getContents(), priceTextField.getText(), commissionTextField.getText()));
    noButton.addActionListener(l -> f.closeAddStrategyForm());
  }


  private void addFocusListenerToUIComponents(FocusListener focusListener) {
    tickerTextField.addFocusListener(focusListener);
    strategyNameTextField.addFocusListener(focusListener);
    priceTextField.addFocusListener(focusListener);
    commissionTextField.addFocusListener(focusListener);
  }

  private Map<String, Runnable> getFormValidationListeners(Features f) {
    Map<String, Runnable> formValidation = new HashMap<>();

    formValidation.put(tickerTextField.getName(),
        () -> f.verifyTickerNameForStrategyForm(tickerTextField.getText()));
    formValidation.put(priceTextField.getName(),
        () -> f.verifyPriceForStrategyForm(priceTextField.getText()));
    formValidation.put(commissionTextField.getName(),
        () -> f.verifyCommissionForStrategyForm(commissionTextField.getText()));
    return formValidation;
  }

  private Map<String, Runnable> getHideErrorListeners() {
    Map<String, Runnable> hideError = new HashMap<>();
    hideError.put(tickerTextField.getName(), () -> resetAndHideErrorLabel(tickerErrorLabel));
    hideError.put(strategyNameTextField.getName(),
        () -> resetAndHideErrorLabel(strategyNameErrorLabel));
    hideError.put(priceTextField.getName(), () -> resetAndHideErrorLabel(priceErrorLabel));
    hideError.put(commissionTextField.getName(),
        () -> resetAndHideErrorLabel(commissionErrorLabel));
    return hideError;
  }

  private void initialiseFields() {
    tickerTextField = createTextFieldWithName("ticker");
    strategyNameTextField = createTextFieldWithName("strategy name");
    priceTextField = createTextFieldWithName("price");
    commissionTextField = createTextFieldWithName("commission");
    tickerErrorLabel = createErrorField();
    priceErrorLabel = createErrorField();
    strategyNameErrorLabel = createErrorField();
    commissionErrorLabel = createErrorField();
  }


  /**
   * This method clears the dialog and hides it.
   */
  void clearAndHide() {
    setVisible(false);
  }

  void setCommissionErrorLabel(String message) {
    setErrorMessage(commissionErrorLabel, message);
  }

  void setTickerErrorLabel(String message) {
    setErrorMessage(tickerErrorLabel, message);
  }

  void setStrategyNameErrorLabel(String message) {
    setErrorMessage(strategyNameErrorLabel, message);
  }

  void setStrategyFormPriceErrorLabel(String message) {
    setErrorMessage(priceErrorLabel, message);
  }
}