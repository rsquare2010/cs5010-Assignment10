package stockemulation.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

public class EntrySet extends JPanel {

  private List<Ticker> entries;

  public EntrySet() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setAlignmentX(SwingConstants.CENTER);
    this.entries = new ArrayList<Ticker>();

    Ticker initial = new Ticker("", "", this);
    addItem(initial);
  }

  public void cloneEntry(Ticker entry) {
    Ticker theClone = new Ticker("", "", this);

    addItem(theClone);
  }

  private void addItem(Ticker entry) {
    entries.add(entry);

    add(entry);
    refresh();
  }

  public void removeItem(Ticker entry) {
    entries.remove(entry);
    remove(entry);
    refresh();
  }

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
    }
    else {
      for (Ticker e : entries) {
        e.enableMinus(true);
      }
    }
  }
}
