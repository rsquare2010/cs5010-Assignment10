package stockemulation.view;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.event.FocusListener;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import stockemulation.controller.Features;
import stockemulation.controller.GUIController;

/**
 * This is custom Dialog created by extending the CustomDialog class. It generates a form to collect
 * information from the user to facilitate the buy stocks operation.
 */
class BuyStockDialog extends CustomDialog {

  private JTextField priceTextField;
  private JTextField commissionTextField;
  private JTextField tickerTextField;
  private JComboBox hourBox;
  private JComboBox minuteBox;

  private JLabel dateErrorLabel;
  private JLabel timeErrorLabel;
  private JLabel tickerErrorLabel;
  private JLabel priceErrorLabel;
  private JLabel commissionErrorLabel;

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
  BuyStockDialog(Frame frame, String text, GUIController controller) {
    super(frame, text);

    initialiseFields();

    addComponentsToRootPanel("Date to buy stocks:", dateErrorLabel, getDateComponent());
    addComponentsToRootPanel("Time of purchase:", timeErrorLabel, getTimeComponent());
    addComponentsToRootPanel("Ticker name of the stock", tickerErrorLabel,
            tickerTextField);
    addComponentsToRootPanel("Price of the stocks you want to buy", priceErrorLabel,
            priceTextField);
    addComponentsToRootPanel("Commission for this transaction", commissionErrorLabel,
            commissionTextField);

    addOptionsToRootPanel();

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
            priceTextField.getText(), commissionTextField.getText(), portfolioIndex));
    noButton.addActionListener(l -> f.closeBuyForm());
  }



  private void addFocusListenerToUIComponents(FocusListener focusListener) {
    dateChooser.addFocusListener(focusListener);
    hourBox.addFocusListener(focusListener);
    minuteBox.addFocusListener(focusListener);
    tickerTextField.addFocusListener(focusListener);
    priceTextField.addFocusListener(focusListener);
    commissionTextField.addFocusListener(focusListener);
  }

  private Map<String, Runnable> getFormValidationListeners(Features f) {
    Map<String, Runnable> formValidation = new HashMap<>();
    formValidation.put(dateChooser.getName(), () -> f.verifyDatesForBuyForm(dateChooser.getDate()));
    formValidation.put(hourBox.getName(), () -> f.verifyTimeForBuyForm(getSelectedTime()));
    formValidation.put(minuteBox.getName(), () -> f.verifyTimeForBuyForm(getSelectedTime()));

    formValidation.put(tickerTextField.getName(),
        () -> f.verifyTickerForBuyForm(tickerTextField.getText()));
    formValidation.put(priceTextField.getName(),
        () -> f.verifyCostForBuyForm(priceTextField.getText()));
    formValidation.put(commissionTextField.getName(),
        () -> f.verifyCommissionForBuyForm(commissionTextField.getText()));
    return formValidation;
  }

  private Map<String, Runnable> getHideErrorListeners() {
    Map<String, Runnable> hideError = new HashMap<>();
    hideError.put(dateChooser.getName(), () -> resetAndHideErrorLabel(dateErrorLabel));
    hideError.put(hourBox.getName(), () -> resetAndHideErrorLabel(timeErrorLabel));
    hideError.put(minuteBox.getName(), () -> resetAndHideErrorLabel(timeErrorLabel));
    hideError.put(tickerTextField.getName(), () -> resetAndHideErrorLabel(tickerErrorLabel));
    hideError.put(priceTextField.getName(), () -> resetAndHideErrorLabel(priceErrorLabel));
    hideError.put(commissionTextField.getName(),
        () -> resetAndHideErrorLabel(commissionErrorLabel));
    return hideError;
  }

  private void initialiseFields() {
    tickerTextField = createTextFieldWithName("ticker");
    priceTextField = createTextFieldWithName("price");
    commissionTextField = createTextFieldWithName("commission");
    dateErrorLabel = createErrorField();
    timeErrorLabel = createErrorField();
    tickerErrorLabel = createErrorField();
    priceErrorLabel = createErrorField();
    commissionErrorLabel = createErrorField();
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

  void setPriceErrorLabel(String message) {
    setErrorMessage(priceErrorLabel, message);
  }

  private LocalTime getSelectedTime() {
    String hour = hours[hourBox.getSelectedIndex()];
    String minute = minutes[minuteBox.getSelectedIndex()];
    return LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute));
  }
}