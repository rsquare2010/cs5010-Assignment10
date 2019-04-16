package stockemulation.view;

import java.awt.*;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import stockemulation.controller.Features;
import stockemulation.controller.GUIController;

public class SingleStrategyBuyDialog extends CustomDialog {

  private JLabel dateErrorLabel;

  private String[] strategies = {};
  private JComboBox strategyList;
  private int portfolioIndex;

  SingleStrategyBuyDialog(Frame frame, String text, GUIController controller) {
    super(frame, text);


    initialiseFields();

    rootPanel.add(createAlignedLabel("Choose a strategy to invest in:"));
    rootPanel.add(strategyList);
    addComponentsToRootPanel("Date to buy stocks:", dateErrorLabel, getDateComponent());

    addOptionsToRootPanel();
  }


  private void initialiseFields() {
    dateErrorLabel = createErrorField();
  }

  void setFeatures(Features f) {
    Map<String, Runnable> formValidation = getFormValidationListeners(f);
    Map<String, Runnable> hideError = getHideErrorListeners();

    FocusListener inputVerificationListener = getFormFocusListener(formValidation, hideError);
    addFocusListenerToUIComponents(inputVerificationListener);

//    yesButton.addActionListener(l -> f.verifyFormAndBuy();
//    noButton.addActionListener(l -> f.closeSingleStrategyBuyForm());
  }

  void setSelectedPortfolioIndex(int portfolioIndex) {
    this.portfolioIndex = portfolioIndex;
  }

  private Map<String, Runnable> getFormValidationListeners(Features f) {
    Map<String, Runnable> formValidation = new HashMap<>();
    formValidation.put(dateChooser.getName(), () -> f.verifyDatesForBuyForm(dateChooser.getDate()));
    return formValidation;
  }

  private void addFocusListenerToUIComponents(FocusListener focusListener) {
    dateChooser.addFocusListener(focusListener);
  }

  private Map<String, Runnable> getHideErrorListeners() {
    Map<String, Runnable> hideError = new HashMap<>();
    hideError.put(dateChooser.getName(), () -> resetAndHideErrorLabel(dateErrorLabel));
    return hideError;
  }

}
