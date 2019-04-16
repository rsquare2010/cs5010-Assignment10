package stockemulation.view;

import com.toedter.calendar.JDateChooser;

import java.awt.Frame;
import java.awt.event.FocusListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import stockemulation.controller.Features;
import stockemulation.controller.GUIController;

public class DollarCostAverageDialog extends CustomDialog {

  private JLabel startDateErrorLabel;
  private JLabel endDateErrorLabel;
  private JLabel frequencyErrorLabel;

  private String[] strategies = {};
  private JComboBox strategyList;
  private JDateChooser endDateChooser;
  private JTextField frequencyTextField;
  private int portfolioIndex;


  DollarCostAverageDialog(Frame frame, String text, GUIController controller) {
    super(frame, text);

    initialiseFields();

    strategyList = new JComboBox(strategies);
    rootPanel.add(createAlignedLabel("Choose a strategy to invest in:"));
    rootPanel.add(strategyList);
    addComponentsToRootPanel("Date to start purchases:", startDateErrorLabel, getDateComponent());
    endDateChooser = new JDateChooser();
    endDateChooser.setName("date");
    endDateChooser.setDateFormatString("yyyy-MM-dd");
    endDateChooser.setMaxSelectableDate(new Date());
    endDateChooser.setAlignmentX(SwingConstants.CENTER);
    addComponentsToRootPanel("Date to end purchases:", endDateErrorLabel, endDateChooser);
    addComponentsToRootPanel("Frequency of purchase:", frequencyErrorLabel, frequencyTextField);

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

    yesButton.addActionListener(l -> f.verifyAndBuyDollarCostAverage(portfolioIndex,
            getSelectedStrategy(), dateChooser.getDate(), endDateChooser.getDate(),
            frequencyTextField.getText()));
    noButton.addActionListener(l -> f.closeDollarCostAverageForm());
  }

  void setStrategies(String[] strategies) {
    this.strategies = strategies;
    ComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(this.strategies);
    this.strategyList.setModel(comboBoxModel);
    this.strategyList.setSelectedIndex(this.strategyList.getSelectedIndex());
  }

  private void initialiseFields() {
    frequencyTextField = createTextFieldWithName("Frequency of purchase");
    startDateErrorLabel = createErrorField();
    endDateErrorLabel = createErrorField();
    frequencyErrorLabel = createErrorField();
  }


  private Map<String, Runnable> getFormValidationListeners(Features f) {
    Map<String, Runnable> formValidation = new HashMap<>();
    formValidation.put(dateChooser.getName(),
        () -> verifyDates(f, dateChooser.getDate()));
    formValidation.put(endDateChooser.getName(),
        () -> f.verifyEndDateForDCA(dateChooser.getDate(), endDateChooser.getDate()));
    formValidation.put(frequencyTextField.getName(), () -> f.verifyInterval(dateChooser.getDate(),
        endDateChooser.getDate(), frequencyTextField.getText()));
    return formValidation;
  }

  private void addFocusListenerToUIComponents(FocusListener focusListener) {
    dateChooser.addFocusListener(focusListener);
    endDateChooser.addFocusListener(focusListener);
    frequencyTextField.addFocusListener(focusListener);
  }

  private Map<String, Runnable> getHideErrorListeners() {
    Map<String, Runnable> hideError = new HashMap<>();
    hideError.put(dateChooser.getName(), () -> resetAndHideErrorLabel(startDateErrorLabel));
    hideError.put(endDateChooser.getName(), () -> resetAndHideErrorLabel(endDateErrorLabel));
    hideError.put(frequencyTextField.getName(), () -> resetAndHideErrorLabel(frequencyErrorLabel));
    return hideError;
  }

  void verifyDates(Features f, Date date) {
    endDateChooser.setMinSelectableDate(date);
    f.verifyStartDateForDCA(date);
  }

  void setStartDateErrorLabel(String message) {
    setErrorMessage(startDateErrorLabel, message);
  }

  void setEndDateErrorLabel(String message) {
    setErrorMessage(endDateErrorLabel, message);
  }

  void setFrequencyErrorLabel(String message) {
    setErrorMessage(frequencyErrorLabel, message);
  }

  private String getSelectedStrategy() {
    return strategies[strategyList.getSelectedIndex()];
  }

  void clearAndHide() {
    setVisible(false);
  }
}
