package stockemulation.view;

import com.toedter.calendar.JDateChooser;

import java.awt.Frame;
import java.awt.Component;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * An abstract class that contains common operations between other custom dialogs that are
 * created to perform various operations like buy stock or create a strategy. It extends the
 * JDialog class. This dialog will have a root frame and two buttons one for yes and one for no,
 * it will also have a single date component and methods to create aligned labels, error fields,
 * text fields.
 */
public abstract class CustomDialog extends JDialog {

  protected JButton yesButton;
  protected JButton noButton;
  protected JPanel rootPanel;
  protected JDateChooser dateChooser;

  CustomDialog(Frame frame, String text) {
    super(frame, text, true);
    rootPanel = new JPanel();
    rootPanel.setBorder(new EmptyBorder(10, 10, 10, 30));
    rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));

    pack();
    getContentPane().add(rootPanel);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  protected void addComponentsToRootPanel(String message, JLabel errorLabel,
                                          Component specificComponent) {
    rootPanel.add(createAlignedLabel(message));
    rootPanel.add(errorLabel);
    rootPanel.add(specificComponent);
  }

  protected JLabel createAlignedLabel(String text) {
    JLabel label = new JLabel(text);
    label.setAlignmentX(SwingConstants.CENTER);
    return label;
  }

  protected JLabel createErrorField() {
    JLabel errorLabel = new JLabel();
    errorLabel.setVisible(false);
    errorLabel.setForeground(Color.RED);
    errorLabel.setAlignmentX(SwingConstants.CENTER);
    return errorLabel;
  }

  protected void addOptionsToRootPanel() {
    JPanel options = new JPanel(new FlowLayout());

    yesButton = new JButton("Yes");
    noButton = new JButton("no");
    options.add(yesButton);
    options.add(noButton);
    options.setAlignmentX(SwingConstants.CENTER);

    rootPanel.add(options);
  }

  protected FocusListener getFormFocusListener(Map<String, Runnable> formValidation, Map<String,
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

  protected void setErrorMessage(JLabel label, String message) {
    label.setText(message);
    label.setVisible(true);
  }

  protected void resetAndHideErrorLabel(JLabel label) {
    label.setText("");
    label.setVisible(false);
  }

  protected JTextField createTextFieldWithName(String name) {
    JTextField textField = new JTextField();
    textField.setName(name);
    textField.setAlignmentX(SwingConstants.CENTER);
    return textField;
  }

  protected JDateChooser getDateComponent() {
    dateChooser = new JDateChooser();
    dateChooser.setName("date");
    dateChooser.setDateFormatString("yyyy-MM-dd");
    dateChooser.setMaxSelectableDate(new Date());
    dateChooser.setAlignmentX(SwingConstants.CENTER);
    return dateChooser;
  }
}
