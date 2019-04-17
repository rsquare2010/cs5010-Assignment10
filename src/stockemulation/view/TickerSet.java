package stockemulation.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

/**
 * Extended JPanel to create custom input field for stock and its weights.
 */
class TickerSet extends JPanel {

  private List<Ticker> entries;

  /**
   * Constructor of the TickerSet class that creates an instance of the Ticker Set class with one
   * field for ticker name and one field for weight.
   */
  public TickerSet() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setAlignmentX(SwingConstants.CENTER);
    this.entries = new ArrayList<Ticker>();

    Ticker initial = new Ticker("", "", this);
    addItem(initial);
  }

  /**
   * Create another field that can take in input for ticker name and weight.
   * @param entry add a new ticker item to the list of tickers.
   */
  public void cloneEntry(Ticker entry) {
    Ticker theClone = new Ticker("", "", this);

    addItem(theClone);
  }

  private void addItem(Ticker entry) {
    entries.add(entry);

    add(entry);
    refresh();
  }

  /**
   * Remove a ticker field from that ticker list that might not be needed.
   * @param entry the entry to be removed.
   */
  public void removeItem(Ticker entry) {
    entries.remove(entry);
    remove(entry);
    refresh();
  }

  /**
   * Get the contents of the entire ticker list. This returns a Map of keys of type String and
   * value of type String. The map will will have as many entries as there are elements in the
   * list. And each key entry will be the ticker symbol and each weight is the weight associated
   * to it.
   * @return A map of ticker symbol key and the associated weights. Te key and value in the map
   *         are of type String.
   */
  public Map<String, String> getContents() {
    Map<String, String> map = new HashMap<>();
    for (Ticker ticker : entries) {
      map.put(ticker.getTickerText(), ticker.getWeight());
    }
    return map;
  }

  private void refresh() {
    revalidate();

    if (entries.size() == 1) {
      entries.get(0).enableMinus(false);
      entries.get(0).setWeightField("100.0");
    } else {
      for (Ticker e : entries) {
        e.enableMinus(true);
        Double value = 100.0 / entries.size();
        DecimalFormat two = new DecimalFormat("#0.00");
        e.setWeightField(two.format(value));
      }
    }
  }
}
