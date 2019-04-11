package stockemulation.view;

import com.toedter.calendar.JDateChooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.WindowConstants;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import stockemulation.controller.Features;
import stockemulation.controller.GUIController;

/**
 * An implementation of the IView interface and the JFrame interface. This implementation contains
 * the methods to provide a responsive graphical user interface to the user. It creates a window
 * that contains a JFrame which holds all the UI components.
 */
public class View extends JFrame implements IView {

  private String[] portfolios = {};
  private JComboBox portfolioList;

  private JButton createPortfolioButton;
  private JButton buyStockButton;
  private JLabel selectedDate;
  private JMenuItem save;
  private JMenuItem open;
  private JLabel statusLabel;
  private JDateChooser dateChooser;
  private JLabel costBasis;
  private JLabel costBasisLabel;
  private JLabel value;
  private JLabel valueLabel;
  private BuyStockDialog buyStockDialog;
  private JTabbedPane tabbedPane;
  private String[] column = {"Serial no", "Ticker", "Number of stocks"};
  private JTable table;

  /**
   * A constructor to the view class that that provides the name of the window as a parameter as
   * well as an instance of the controller.
   * @param caption string that represents the name of the window.
   * @param controller an instance of the GUIController class.
   */
  public View(String caption, GUIController controller) {
    super(caption);

    this.setSize(1024, 720);
    this.setVisible(true);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    JPanel leftPanel = setupLeftPanel();
    JPanel topPanel = setupTopPanel();
    JPanel bottomPanel = setupBottomPanel();
    JPanel centerPanel = setupCenterPanel();

    this.add(centerPanel, BorderLayout.CENTER);
    this.add(leftPanel, BorderLayout.WEST);
    this.add(topPanel, BorderLayout.NORTH);
    this.add(bottomPanel, BorderLayout.SOUTH);

    JMenuBar menuBar = setupMenuBar();
    this.setJMenuBar(menuBar);

    setupBuyStockDialog();
  }

  @Override
  public void setFeatures(Features f) {
    save.addActionListener(l -> saveToFile(f));
    open.addActionListener(l -> readFromFile(f));
    buyStockDialog.setFeatures(f);
    buyStockButton.addActionListener(l -> f.buyStocks());
    createPortfolioButton.addActionListener(l -> f.createPortfolio(showInputDialog()));
    dateChooser.addPropertyChangeListener(l -> setDateAndFetchDetails(dateChooser.getDate(), f));
    tabbedPane.addChangeListener(l -> fetchInformationBasedOnTab(tabbedPane.getSelectedIndex(), f));
    portfolioList.addActionListener(l -> f.getPortfolioSummary(portfolioList.getSelectedIndex()));
  }


  private String showInputDialog() {
    return JOptionPane.showInputDialog(this, "Enter new portfolio name",
            "");
  }

  private void saveToFile(Features features) {
    final JFileChooser fchooser = new JFileChooser(".");
    int retvalue = fchooser.showSaveDialog(this);
    if (retvalue == JFileChooser.APPROVE_OPTION) {
      File fileName = fchooser.getSelectedFile();
      features.writeToFile(fileName.getAbsolutePath(), portfolioList.getSelectedIndex());
    }
  }

  private void readFromFile(Features features) {
    final JFileChooser fchooser = new JFileChooser(".");
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Text based files", "csv", "json");
    fchooser.setFileFilter(filter);
    int retvalue = fchooser.showOpenDialog(this);
    if (retvalue == JFileChooser.APPROVE_OPTION) {
      File f = fchooser.getSelectedFile();
      features.readFromFile(f.getAbsolutePath());
    }
  }

  @Override
  public void setPortfolioList(String[] portfolioList) {
    portfolios = portfolioList;
    ComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(portfolioList);
    this.portfolioList.setModel(comboBoxModel);
    this.portfolioList.setSelectedIndex(this.portfolioList.getSelectedIndex());
  }

  @Override
  public void showBuyStocksForm() {
    buyStockDialog.pack();
    buyStockDialog.setSelectedPortfolioIndex(portfolioList.getSelectedIndex());
    buyStockDialog.setVisible(true);
  }

  @Override
  public void closeBuyStocksForm() {
    buyStockDialog.clearAndHide();
    portfolioList.setSelectedIndex(portfolioList.getSelectedIndex());
  }

  @Override
  public void setBuyFormDateError(String message) {
    buyStockDialog.setDateErrorLabel(message);
  }

  @Override
  public void setBuyFormTimeError(String message) {
    buyStockDialog.setTimeErrorLabel(message);
  }

  @Override
  public void setBuyFormTickerError(String message) {
    buyStockDialog.setTickerErrorLabel(message);
  }

  @Override
  public void setBuyFormPriceError(String message) {
    buyStockDialog.setPriceErrorLabel(message);
  }

  @Override
  public void setBuyFormCommissionError(String message) {
    buyStockDialog.setCommissionErrorLabel(message);
  }

  @Override
  public void setPortfolioSummary(Map<String, Double> portfolioSummary) {
    tabbedPane.setVisible(true);
    tabbedPane.setSelectedIndex(0);

    String[][] data = new String[portfolioSummary.size()][3];
    Set entries = portfolioSummary.entrySet();
    Iterator entriesIterator = entries.iterator();

    int i = 0;
    while (entriesIterator.hasNext()) {

      Map.Entry mapping = (Map.Entry) entriesIterator.next();
      data[i][0] = "" + (i + 1);
      data[i][1] = mapping.getKey().toString();
      data[i][2] = mapping.getValue().toString();

      i++;
    }

    TableModel tableModel = new DefaultTableModel(data, column);
    table.setModel(tableModel);

  }

  @Override
  public void setPortfolioValueOnDate(Double costBasis, Double value) {
    valueLabel.setVisible(true);
    costBasisLabel.setVisible(true);
    this.costBasis.setText(costBasis.toString());
    this.value.setText(value.toString());
  }

  @Override
  public void showMessage(String message) {
    statusLabel.setForeground(Color.BLACK);
    statusLabel.setText(message);
  }

  @Override
  public void showErrorMessage(String message) {
    statusLabel.setText(message);
    statusLabel.setForeground(Color.RED);
  }

  private JPanel setupLeftPanel() {
    JPanel leftPanel = new JPanel(new BorderLayout());
    portfolioList = new JComboBox(portfolios);
    portfolioList.setBounds(0, 0, 120, 330);

    JPanel createPortfolioButtonPanel = new JPanel();
    createPortfolioButton = new JButton("Create portfolio");

    createPortfolioButtonPanel.add(createPortfolioButton);


    leftPanel.add(portfolioList);
    leftPanel.add(createPortfolioButtonPanel, BorderLayout.PAGE_END);
    leftPanel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.LINE_END);
    leftPanel.setBounds(50, 0, 1024, 700);
    return leftPanel;
  }

  private JPanel setupTopPanel() {
    JPanel topPanel = new JPanel(new BorderLayout());
    JLabel introLabel = new JLabel("Welcome to the Stock Emulator application");
    introLabel.setHorizontalAlignment(JLabel.CENTER);
    topPanel.add(introLabel);
    topPanel.add(new JSeparator(), BorderLayout.SOUTH);
    topPanel.setBounds(0, 0, 1024, 50);
    return topPanel;
  }

  private JPanel setupBottomPanel() {
    JPanel bottomPanel = new JPanel(new BorderLayout());
    statusLabel = new JLabel("Create a portfolio to get started");
    statusLabel.setHorizontalAlignment(JLabel.CENTER);
    bottomPanel.add(statusLabel);
    bottomPanel.add(new JSeparator(), BorderLayout.NORTH);
    return bottomPanel;
  }

  private JPanel setupCenterPanel() {

    JPanel centerPanel = new JPanel(new BorderLayout());
    JPanel secondTab = setupSecondTabForCenterPanel();

    String[][] data = {};
    table = new JTable(data, column);

    tabbedPane = new JTabbedPane();
    tabbedPane.setBounds(50, 50, 200, 200);
    tabbedPane.add("summary", new JScrollPane(table));
    tabbedPane.add("detail", secondTab);
    centerPanel.add(tabbedPane);

    JPanel buyStockButtonPanel = new JPanel();
    buyStockButton = new JButton("Buy Stocks");
    buyStockButtonPanel.add(buyStockButton);
    centerPanel.add(buyStockButtonPanel, BorderLayout.SOUTH);
    return centerPanel;
  }

  private JPanel setupSecondTabForCenterPanel() {
    JPanel secondTab = new JPanel();
    JLabel centerLabel = new JLabel("Select the date to check the portfolio value ");
    selectedDate = new JLabel("");
    dateChooser = new JDateChooser();
    dateChooser.setMaxSelectableDate(new Date());
    dateChooser.setDateFormatString("yyyy-MM-dd");


    costBasisLabel = new JLabel("The cost basis of the portfolio on the selected date is: $");
    costBasisLabel.setVisible(false);
    costBasis = new JLabel("");
    valueLabel = new JLabel("The value of the portfolio on the selected date is: $");
    valueLabel.setVisible(false);
    value = new JLabel("");
    secondTab.add(centerLabel);
    secondTab.add(dateChooser.getCalendarButton());
    secondTab.add(selectedDate);
    secondTab.add(costBasisLabel);
    secondTab.add(costBasis);
    secondTab.add(valueLabel);
    secondTab.add(value);
    return secondTab;
  }

  private JMenuBar setupMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu menu;
    menu = new JMenu("Files");
    save = new JMenuItem("Save");
    open = new JMenuItem("Read");

    menu.add(save);
    menu.add(open);
    menuBar.add(menu);
    return menuBar;
  }

  private void setupBuyStockDialog() {
    buyStockDialog = new BuyStockDialog(this, "Buy Stocks", null);
  }

  private void fetchInformationBasedOnTab(int index, Features f) {
    if (index == 1) {
      if (dateChooser.getDate() != null) {
        f.getPortfolioValue(portfolioList.getSelectedIndex(), dateChooser.getDate());
      }
    }
  }

  private void setDateAndFetchDetails(Date date, Features features) {
    selectedDate.setText(date.toString());
    features.getPortfolioValue(portfolioList.getSelectedIndex(),
            dateChooser.getDate());
  }
}
