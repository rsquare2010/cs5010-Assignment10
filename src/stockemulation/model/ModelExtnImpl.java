package stockemulation.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import stockemulation.util.StockInfoSanity;

/**
 * This is an implementation of {@link ModelExtn} and has some of the implementations directly taken
 * from {@link ModelImpl} by extending it. This provide two constructors, one to create the object
 * with default data source which is Alpha Vantage api and other constructor to choose the API.
 */
public class ModelExtnImpl extends ModelImpl implements ModelExtn {


  /**
   * Default constructor that calls the default constructor of the base class and sets the api to
   * the efault Alpha vantage api.
   */
  public ModelExtnImpl() {
    super();
  }

  /**
   * Constructor that provides a way to select the data source by passing the Enum value. This gets
   * a reference to the api singleton of that type. It essentially make a call to the base class and
   * does this.
   */
  public ModelExtnImpl(APITypes datasourcetype) {
    super(datasourcetype);
  }

  @Override
  public void buyStock(int portfolioNumber, LocalDateTime date, String ticker, Double price,
                       double commission) throws IllegalArgumentException {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }

    portfolios.get(portfolioNumber).buyShares(ticker, price, date, commission);
  }

  @Override
  public void writePortfolioToFile(String filepath, int portfolioNumber)
          throws IllegalArgumentException, IOException {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }
    if (filepath == null || filepath.equals("")) {
      throw new IllegalArgumentException("File path for writing portfolio cannot be empty");
    }
    portfolios.get(portfolioNumber).writeToFile(filepath);

  }

  @Override
  public void readPortfolioFromFile(String filepath) throws IllegalArgumentException, IOException,
          ParseException {
    JSONParser parser = new JSONParser();

    Object obj = parser.parse(new FileReader(filepath));

    JSONObject jsonObject = (JSONObject) obj;

    String title = (String) jsonObject.get("title");

    createPortfolio(title);

    JSONArray transactions = (JSONArray) jsonObject.get("transactions");
    Iterator<JSONObject> iterator = transactions.iterator();
    while (iterator.hasNext()) {
      JSONObject stockObj = iterator.next();
      String tickerName = (String) stockObj.get("ticker");
      String purchaseDate = (String) stockObj.get("purchaseDate");
      double costPerUnit = (double) stockObj.get("costPerUnit");
      double quantity = (double) stockObj.get("quantity");
      double commission = (double) stockObj.get("commission");
      buyStock(
              portfolios.size() - 1,
              LocalDateTime.parse(purchaseDate),
              tickerName,
              costPerUnit * quantity,
              commission
      );
    }
  }

  // TODO: ------- Assignment 10 features ---------------------------



  // TODO: ------------------END-------------------------------------


}
