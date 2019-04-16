package stockemulation.view;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class Ticker extends JPanel {

  private JTextField tickerField;
  private JTextField weightField;
  private JButton add;
  private JButton remove;
  private EntrySet parent;

  public Ticker(String tickerFieldText, String weightFieldText, EntrySet list) {
    this.parent = list;
    this.add = new JButton(new AddEntryAction());
    add.setBounds(0,0, 30,30);
    this.remove = new JButton(new RemoveEntryAction());
    remove.setBounds(0,0, 30,30);
    this.tickerField = new JTextField(10);
    this.tickerField.setText(tickerFieldText);
    this.weightField = new JTextField(10);
    this.weightField.setText(weightFieldText);
    add(this.tickerField);
    add(this.weightField);
    add(this.add);
    add(this.remove);
  }

  public String getTickerText() {
    return tickerField.getText();
  }

  public String getWeight() {
    return weightField.getText();
  }

  public class AddEntryAction extends AbstractAction {

    public AddEntryAction() {
      super("+");
    }

    public void actionPerformed(ActionEvent e) {
      parent.cloneEntry(Ticker.this);
    }

  }

  public class RemoveEntryAction extends AbstractAction {

    public RemoveEntryAction() {
      super("-");
    }

    public void actionPerformed(ActionEvent e) {
      parent.removeItem(Ticker.this);
    }
  }

  public void enableAdd(boolean enabled) {
    this.add.setEnabled(enabled);
  }

  public void enableMinus(boolean enabled) {
    this.remove.setEnabled(enabled);
  }

}