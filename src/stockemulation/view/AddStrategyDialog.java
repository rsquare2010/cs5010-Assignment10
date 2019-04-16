package stockemulation.view;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import stockemulation.controller.Features;
import stockemulation.controller.GUIController;

/**
 * This is custom Dialog created by extending the JDialog class. It generates a form to collect
 * information from the user to facilitate the buy stocks operation.
 */
class AddStrategyDialog extends JDialog {

  private JTextField strategyNameTextField;
  private JTextField commissionTextField;
  private JTextField tickerTextField;
  private JTextField priceTextField;

  private JLabel tickerErrorLabel;
  private JLabel strategyNameErrorLabel;
  private JLabel commissionErrorLabel;
  private JLabel priceErrorLabel;

  private JButton yesButton;
  private JButton noButton;
  private JPanel rootPanel;
  private EntrySet weightSet;
  private List<String> tickerList;
  private int portfolioIndex;


  /**
   * Create an instance of this class by providing the parent Frame, the window title as a string
   * and an instance of the GUIController.
   * @param frame the parent from over which this dialog pops up.
   * @param text the title of this Dialog box.
   * @param controller an instance of the GUIController class.
   */
  AddStrategyDialog(Frame frame, String text, GUIController controller) {
    super(frame, text, true);

    rootPanel = new JPanel();
    rootPanel.setBorder(new EmptyBorder(10, 10, 10, 30));
    rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));

    initialiseFields();

    addComponentsToRootPanel("Enter the name of the strategy", strategyNameErrorLabel,
            strategyNameTextField);
    weightSet = new EntrySet();
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


    pack();
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }


  void setSelectedPortfolioIndex(int portfolioIndex) {
    this.portfolioIndex = portfolioIndex;
  }

  void setFeatures(Features f) {
    Map<String, Runnable> formValidation = getFormValidationListeners(f);
    Map<String, Runnable> hideError = getHideErrorListeners();

    FocusListener inputVerificationListener = getFormFocusListener(formValidation, hideError);
    addFocusListenerToUIComponents(inputVerificationListener);

    yesButton.addActionListener(l-> f.createAStrategy(strategyNameTextField.getText(),
            weightSet.getContents(), priceTextField.getText(), commissionTextField.getText()));
    noButton.addActionListener(l -> f.closeStrategyForm());
  }

  private FocusListener getFormFocusListener(Map<String, Runnable> formValidation, Map<String,
          Runnable> hideError) {
    return new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (hideError.containsKey(e.getComponent().getName())) {
          hideError.get(e.getComponent().getName()).run();
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        if (formValidation.containsKey(e.getComponent().getName())) {
          formValidation.get(e.getComponent().getName()).run();
        }
      }
    };
  }

  private void addFocusListenerToUIComponents(FocusListener focusListener) {
    tickerTextField.addFocusListener(focusListener);
    strategyNameTextField.addFocusListener(focusListener);
    commissionTextField.addFocusListener(focusListener);
  }

  private Map<String, Runnable> getFormValidationListeners(Features f) {
    Map<String, Runnable> formValidation = new HashMap<>();

    formValidation.put(tickerTextField.getName(), () -> f.verifyTickerNameForStrategyForm(tickerTextField.getText()));
    formValidation.put(priceTextField.getName(),
            () -> f.verifyPriceForStrategyForm(priceTextField.getText()));
    formValidation.put(commissionTextField.getName(),
            () -> f.verifyCommissionForStrategyForm(commissionTextField.getText()));
    return formValidation;
  }

  private Map<String, Runnable> getHideErrorListeners() {
    Map<String, Runnable> hideError = new HashMap<>();
    hideError.put(tickerTextField.getName(), () -> resetAndHideErrorLabel(tickerErrorLabel));
    hideError.put(strategyNameTextField.getName(), () -> resetAndHideErrorLabel(strategyNameErrorLabel));
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

  private void addComponentsToRootPanel(String message, JLabel errorLabel,
                                        Component specificComponent) {
    rootPanel.add(createAlignedLabel(message));
    rootPanel.add(errorLabel);
    rootPanel.add(specificComponent);
  }

  private JLabel createErrorField() {
    JLabel errorLabel = new JLabel();
    errorLabel.setVisible(false);
    errorLabel.setForeground(Color.RED);
    errorLabel.setAlignmentX(SwingConstants.CENTER);
    return errorLabel;
  }

  private JLabel createAlignedLabel(String text) {
    JLabel label = new JLabel(text);
    label.setAlignmentX(SwingConstants.CENTER);
    return label;
  }

  private void addOptionsToRootPanel() {
    JPanel options = new JPanel(new FlowLayout());

    yesButton = new JButton("Yes");
    noButton = new JButton("no");
    options.add(yesButton);
    options.add(noButton);
    options.setAlignmentX(SwingConstants.CENTER);

    rootPanel.add(options);
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

  private void setErrorMessage(JLabel label, String message) {
    label.setText(message);
    label.setVisible(true);
  }

  private void resetAndHideErrorLabel(JLabel label) {
    label.setText("");
    label.setVisible(false);
  }

  private JTextField createTextFieldWithName(String name) {
    JTextField textField = new JTextField();
    textField.setName(name);
    textField.setAlignmentX(SwingConstants.CENTER);
    return textField;
  }
}