package stockemulation.view;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import stockemulation.controller.GUIController;

public class SingleStrategyBuyDialog extends BuyStockDialog {

  private JDateChooser dateChooser;
  private JLabel dateErrorLabel;

  private JPanel rootPanel;
  private String[] portfolios = {};
  private JComboBox portfolioList;

  SingleStrategyBuyDialog(Frame frame, String text, GUIController controller) {
    super(frame, text, controller);

    rootPanel = new JPanel();
    rootPanel.setBorder(new EmptyBorder(10, 10, 10, 30));
    rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));

    initialiseFields();

    rootPanel.add(createAlignedLabel("Choose a strategy to invest in:"));
    rootPanel.add(portfolioList);
    addComponentsToRootPanel("Date to buy stocks:", dateErrorLabel, getDateComponent());
  }


  private void initialiseFields() {
    dateErrorLabel = createErrorField();
  }

}
