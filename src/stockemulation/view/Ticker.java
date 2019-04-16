package stockemulation.view;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
 * A class that represents one instance of the Ticker field. This class contains a textfield that
 * accepts user input for ticker symbol, another textfield for weight and two buttons, one to add
 * more Ticker fields and one to remove this field.
 */
public class Ticker extends JPanel {

  private JTextField tickerField;
  private JTextField weightField;
  private JButton add;
  private JButton remove;
  private TickerSet parent;

  /**
   * Create an instance of the Ticker class with preset stings for the ticker symbol, the weight
   * associated with it as well as the list to which this ticker needs ot be added.
   * @param tickerFieldText the string to prepopulate the ticker symbol text field.
   * @param weightFieldText the string to prepopulate the weight text field.
   * @param list the list of Tickers to which this element will be added.
   */
  public Ticker(String tickerFieldText, String weightFieldText, TickerSet list) {
    this.parent = list;
    this.add = new JButton(" + ");
    add.setBounds(0, 0, 30, 30);
    this.remove = new JButton(" - ");
    remove.setBounds(0, 0, 30, 30);
    this.tickerField = new JTextField(10);
    this.tickerField.setText(tickerFieldText);
    this.weightField = new JTextField(10);
    this.weightField.setText(weightFieldText);
    this.add.addActionListener(l -> parent.cloneEntry(Ticker.this));
    this.remove.addActionListener(l -> parent.removeItem(Ticker.this));
    add(this.tickerField);
    add(this.weightField);
    add(this.add);
    add(this.remove);
  }

  /**
   * Getter method to obtain the current contents of the ticker symbol text field.
   * @return A string that represents the contents of the ticker symbol text field.
   */
  public String getTickerText() {
    return tickerField.getText();
  }

  /**
   * Getter method to obtain the current contents of the weight text field.
   * @return A string that represents the contents of the weight text field.
   */
  public String getWeight() {
    return weightField.getText();
  }

  /**
   * Setter method to update the contents of the weight text field.
   * @param weight a double which will be populated into the weight text field as a string.
   */
  public void setWeightField(Double weight) {
    weightField.setText(weight.toString());
  }

  /**
   * Set a flag that denotes if the add button for this field is enabled or not.
   * @param enabled a boolean parameter which enables the add button if true, disables it if false.
   */
  public void enableAdd(boolean enabled) {
    this.add.setEnabled(enabled);
  }

  /**
   * Set a flag that denotes if the remove button for this field is enabled or not.
   * @param enabled a boolean parameter which enables the remove button if true, disables it if
   *                false.
   */
  public void enableMinus(boolean enabled) {
    this.remove.setEnabled(enabled);
  }

}
