package stockemulation.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An implementation of the {@link API} interface. This implementation helps fetch stock related
 * information from the AlphaVantage API. This implementation also caches stock information when a
 * certain stock is purchased.
 */
class APIImpl implements API {

  private static String API_KEY = "0QTSDSALGR0P8GOR";

  private static Map<String, Cache> cache;

  private static APIImpl instance;

  /**
   * Private constructor, does not let an outside class create an instance of this class. It is a
   * singleton and so an instance of this can be obtained using the getInstance method.
   */
  private APIImpl() {
    cache = new HashMap<>();
  }

  /**
   * This helps get an instance of the APIImpl class.
   *
   * @return the instance of APIImpl class.
   */
  static API getInstance() {
    if (instance == null) {
      instance = new APIImpl();
    }
    return instance;
  }

  /**
   * This method hits the AlphaVantage API and fetches the price of a stock whose ticker is passed
   * as a parameter on a given date which is an additional parameter. The price that is fetched is
   * the opening price of the stock on that day. If the market was shut on the given date an
   * exception is thrown.
   *
   * @param date        The date on which to obtain the opening price of a stock.
   * @param stockSymbol The ticker of the stock whose price has to be fetched using the API.
   * @return double that represents the price of the stock.
   * @throws IllegalArgumentException thrown when trying to fetch data for a date when the market
   *                                  was not open eg. weekends or public holidays
   */
  public double getPriceAtTime(LocalDate date, String stockSymbol) throws IllegalArgumentException {
    if (cache.containsKey(stockSymbol)) {
      if (cache.get(stockSymbol).getDate().isAfter(date)) {
        if (cache.get(stockSymbol).getStockData().containsKey(date.toString())) {
          return Double.parseDouble(cache.get(stockSymbol).getStockData().get(date.toString()));
        } else {
          throw new IllegalArgumentException("Data unavailable for this day!");
        }
      }
    }
    try {
      return Double.parseDouble(parseTimeSeriesJSON(date, stockSymbol,
              makeHttpRequest(constructTimeSeriesURL(stockSymbol))));
    } catch (Exception e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private URL constructTimeSeriesURL(String stockSymbol) throws MalformedURLException {
    URL url;
    try {
      url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + stockSymbol + "&apikey=" + API_KEY + "&datatype=json");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage APIImpl has either changed or "
              + "no longer works");
    }
    return url;
  }

  private JSONObject makeHttpRequest(URL url) {
    URLConnection request = null;
    JSONObject root = null;
    try {
      if (url == null) {
        System.out.println("url is null");
      }
      request = url.openConnection();
      request.connect();

      JSONParser jp = new JSONParser();

      // input stream to a json element
      Object obj = jp.parse(new InputStreamReader((InputStream) request.getContent()));
      root = (JSONObject) obj;
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }
    return root;
  }

  private String parseTimeSeriesJSON(LocalDate time, String ticker, JSONObject result)
          throws IllegalArgumentException {
    HashMap<String, String> map = new HashMap<>();
    String value = "";
    if (result.containsKey("Note")) {
      throw new IllegalArgumentException("Try again later");
    }
    if (result.containsKey("Error Message")) {
      throw new IllegalArgumentException("Ticker entered does not exist in the stock market");
    }
    if (result.containsKey("Time Series (Daily)")) {
      JSONObject timeSeriesObject = (JSONObject) result.get("Time Series (Daily)");
      for (Iterator iterator = timeSeriesObject.keySet().iterator(); iterator.hasNext(); ) {
        String key = (String) iterator.next();
        JSONObject prices = (JSONObject) timeSeriesObject.get(key);
        if (key.equals(time.toString())) {
          value = prices.get("4. close").toString();
        }
        map.put(key, prices.get("4. close").toString());
      }
      cache.put(ticker, new CacheImpl(LocalDate.now(), map));
    } else {
      throw new IllegalArgumentException("Something went wrong");
    }
    return value;
  }
}
