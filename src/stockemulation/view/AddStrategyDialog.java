package stockemulation.view;

import com.toedter.calendar.JDateChooser;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
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
  private JDateChooser dateChooser;
  private JComboBox hourBox;
  private JComboBox minuteBox;

  private JLabel dateErrorLabel;
  private JLabel timeErrorLabel;
  private JLabel tickerErrorLabel;
  private JLabel strategyNameErrorLabel;
  private JLabel commissionErrorLabel;

  private JButton yesButton;
  private JButton noButton;
  private JPanel rootPanel;
  private int portfolioIndex;
  private String[] hours = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
          "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
  private String[] minutes = {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45",
          "50", "55"};


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

    addComponentsToRootPanel("Date to buy stocks:", dateErrorLabel, getDateComponent());
    addComponentsToRootPanel("Time of purchase:", timeErrorLabel, getTimeComponent());
    addComponentsToRootPanel("Ticker name of the stock", tickerErrorLabel,
            tickerTextField);
    addComponentsToRootPanel("Enter the name of the strategy", strategyNameErrorLabel,
            strategyNameTextField);
    addComponentsToRootPanel("Commission for this transaction", commissionErrorLabel,
            commissionTextField);
    addOptionsToRootPanel();

    getContentPane().add(rootPanel);
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

    yesButton.addActionListener(l -> f.verifyFormAndBuy(dateChooser.getDate(),
            getSelectedTime(), tickerTextField.getText(),
            strategyNameTextField.getText(), commissionTextField.getText(), portfolioIndex));
    noButton.addActionListener(l -> f.closeForm());
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
    dateChooser.addFocusListener(focusListener);
    hourBox.addFocusListener(focusListener);
    minuteBox.addFocusListener(focusListener);
    tickerTextField.addFocusListener(focusListener);
    strategyNameTextField.addFocusListener(focusListener);
    commissionTextField.addFocusListener(focusListener);
  }

  private Map<String, Runnable> getFormValidationListeners(Features f) {
    Map<String, Runnable> formValidation = new HashMap<>();
    formValidation.put(dateChooser.getName(), () -> f.verifyDates(dateChooser.getDate()));
    formValidation.put(hourBox.getName(), () -> f.verifyTime(getSelectedTime()));
    formValidation.put(minuteBox.getName(), () -> f.verifyTime(getSelectedTime()));

    formValidation.put(tickerTextField.getName(), () -> f.verifyTicker(tickerTextField.getText()));
    formValidation.put(strategyNameTextField.getName(), () -> f.verifyCost(strategyNameTextField.getText()));
    formValidation.put(commissionTextField.getName(),
            () -> f.verifyCommission(commissionTextField.getText()));
    return formValidation;
  }

  private Map<String, Runnable> getHideErrorListeners() {
    Map<String, Runnable> hideError = new HashMap<>();
    hideError.put(dateChooser.getName(), () -> resetAndHideErrorLabel(dateErrorLabel));
    hideError.put(hourBox.getName(), () -> resetAndHideErrorLabel(timeErrorLabel));
    hideError.put(minuteBox.getName(), () -> resetAndHideErrorLabel(timeErrorLabel));
    hideError.put(tickerTextField.getName(), () -> resetAndHideErrorLabel(tickerErrorLabel));
    hideError.put(strategyNameTextField.getName(), () -> resetAndHideErrorLabel(strategyNameErrorLabel));
    hideError.put(commissionTextField.getName(),
            () -> resetAndHideErrorLabel(commissionErrorLabel));
    return hideError;
  }

  private void initialiseFields() {
    tickerTextField = createTextFieldWithName("ticker");
    strategyNameTextField = createTextFieldWithName("price");
    commissionTextField = createTextFieldWithName("commission");
    dateErrorLabel = createErrorField();
    timeErrorLabel = createErrorField();
    tickerErrorLabel = createErrorField();
    strategyNameErrorLabel = createErrorField();
    commissionErrorLabel = createErrorField();
  }

  private void addComponentsToRootPanel(String message, JLabel errorLabel,
                                        Component specificComponent) {
    rootPanel.add(createAlignedLabel(message));
    rootPanel.add(errorLabel);
    rootPanel.add(specificComponent);
  }

  private Component getDateComponent() {
    dateChooser = new JDateChooser();
    dateChooser.setName("date");
    dateChooser.setDateFormatString("yyyy-MM-dd");
    dateChooser.setMaxSelectableDate(new Date());
    dateChooser.setAlignmentX(SwingConstants.CENTER);
    return dateChooser;
  }

  private Component getTimeComponent() {
    JPanel timePanel = new JPanel(new GridLayout(1, 0));
    hourBox = new JComboBox(hours);
    hourBox.setName("hour");
    minuteBox = new JComboBox(minutes);
    minuteBox.setName("minute");
    timePanel.add(hourBox);
    timePanel.add(minuteBox);
    timePanel.setAlignmentX(SwingConstants.CENTER);
    return timePanel;
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

  void setDateErrorLabel(String message) {
    setErrorMessage(dateErrorLabel, message);
  }

  void setTimeErrorLabel(String message) {
    setErrorMessage(timeErrorLabel, message);
  }

  void setTickerErrorLabel(String message) {
    setErrorMessage(tickerErrorLabel, message);
  }

  void setStrategyNameErrorLabel(String message) {
    setErrorMessage(strategyNameErrorLabel, message);
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

  private LocalTime getSelectedTime() {
    String hour = hours[hourBox.getSelectedIndex()];
    String minute = minutes[minuteBox.getSelectedIndex()];
    return LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute));
  }
}