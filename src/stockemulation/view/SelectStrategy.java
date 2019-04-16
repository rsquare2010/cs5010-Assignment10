package stockemulation.view;

import java.awt.Frame;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;

import stockemulation.controller.Features;

/**
 * This is custom Dialog created by extending the JDialog class. It generates a form to select
 * the strategy from a list of strategies so that it can be saved to a file.
 */
public class SelectStrategy extends CustomDialog {

  private String[] strategies = {};
  private JComboBox strategyList;

  SelectStrategy(Frame frame, String text) {
    super(frame, text);
    strategyList = new JComboBox(strategies);
    rootPanel.add(createAlignedLabel("Choose a strategy to save:"));
    rootPanel.add(strategyList);
    addOptionsToRootPanel();
  }

  void setFeatures(Features f) {
    yesButton.addActionListener(l -> saveToFile(f, strategies[strategyList.getSelectedIndex()]));
    noButton.addActionListener(l -> setVisible(false));
  }

  void setStrategies(String[] strategies) {
    this.strategies = strategies;
    ComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(this.strategies);
    this.strategyList.setModel(comboBoxModel);
    this.strategyList.setSelectedIndex(this.strategyList.getSelectedIndex());
  }

  private void saveToFile(Features features, String strategyName) {
    final JFileChooser fchooser = new JFileChooser(".");
    int retvalue = fchooser.showSaveDialog(this);
    if (retvalue == JFileChooser.APPROVE_OPTION) {
      File fileName = fchooser.getSelectedFile();
      features.saveStrategy(fileName.getAbsolutePath(), strategyName);
    }
  }
}
