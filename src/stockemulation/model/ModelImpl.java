package stockemulation.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import stockemulation.util.StockInfoSanity;

/**
 * An implementation of the StockMarketEmulator Model Interface, An instance of this class
 * facilitates the user in performing the operations supported by the Stock Market Emulator
 * application. Maintains a list of stock portfolios of Portfolio type that the user can create and
 * do virtual trading. Maintains the data source name from which the user has access to stock data.
 * Uses the the api package and the data source name to fetch the stock data from.
 */
public class ModelImpl implements Model {

  protected final API dataSource;        /// Name of th stock data source.
  protected List<PortfolioExtn> portfolios;    /// List containing the portfolios that user created.

  /**
   * Constructor the creates an empty portfolio list and assigns the data source for the model.
   */
  public ModelImpl() {

    portfolios = new LinkedList<>();
    dataSource = API.getInstance(APITypes.ALPHA_VANTAGE);
  }

  /**
   * Constructor the creates an empty portfolio list and assigns the data source for the model.
   */
  ModelImpl(APITypes datasouceType) {

    portfolios = new LinkedList<>();
    dataSource = API.getInstance(datasouceType);
  }

  @Override
  public boolean createPortfolio(String portfolioName)
          throws IllegalArgumentException {
    for (Portfolio portfolio : portfolios) {
      if (portfolioName.equals(portfolio.getPorfolioTitle())) {
        throw new IllegalArgumentException("Portfolio title already exists");
      }
    }

    portfolios.add(new PortfolioExtnImpl(portfolioName, dataSource));
    return true;
  }

  @Override
  public void buyStock(int portfolioNumber, LocalDateTime date, String ticker, Double price)
          throws IllegalArgumentException {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }

    portfolios.get(portfolioNumber).buyShares(ticker, price, date);
  }

  @Override
  public Map<String, Double> getPortfolioDetails(int portfolioNumber)
          throws IllegalArgumentException {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }
    return portfolios.get(portfolioNumber).getCompositionSimple();
  }

  @Override
  public double getCostBasis(int portfolioNumber, LocalDateTime specifiedDate)
          throws IllegalArgumentException {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }


    return portfolios.get(portfolioNumber).getCostBasis(specifiedDate);
  }

  @Override
  public double getTotalValue(int portfolioNumber, LocalDateTime specifiedDate)
          throws IllegalArgumentException {
    if (portfolioNumber >= portfolios.size() || portfolioNumber < 0) {
      throw new IllegalArgumentException("Invalid Portfolio number");
    }

    return portfolios.get(portfolioNumber).getTotalValue(specifiedDate);
  }


  @Override
  public List<String> getPortfolioList() {
    List<String> outList = new LinkedList<>();
    for (Portfolio portfolio : portfolios) {
      outList.add(portfolio.getPorfolioTitle());
    }
    return outList;
  }

  @Override
  public int getPortfolioCount() {
    return portfolios.size();
  }

  @Override
  public String toString() {
    return "Number of Portfolios: " + getPortfolioCount();
  }

  private LocalDateTime getMarketOpenDate(LocalDateTime investmentDate) {
    while (investmentDate.isBefore(LocalDateTime.now())) {
      try {
        StockInfoSanity.isDateTimeValid(investmentDate);
        break;
      } catch (IllegalArgumentException e) {
        investmentDate = investmentDate.minusDays(1);
      }
    }
    return investmentDate;
  }
}
