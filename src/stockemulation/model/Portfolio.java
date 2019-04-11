package stockemulation.model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Interface class that represents a stock portfolio of the user. A portfolio of stocks is simply a
 * collection of stocks. It also provides information about what this portfolio is created for and
 * also a detailed information about the composition of the portfolio. It facilitates a way to buy
 * new stocks and add them to the portfolio. Given a specific time it also provides information
 * about the cost of all the stocks present and the value of the stock at that time.
 */
interface Portfolio {

  /**
   * Buy shares of a particular stock. The quantity of shares to be bought is computed from the
   * specified total worth of shares which have to be bought. The user also has to specify when this
   * particular stock has to be bought.
   *
   * @param tickerName    the ticker name of stock of format "XXXX" (all in caps).
   * @param value         the total worth of shares of the particular stock which has to be bought.
   * @param specifiedDate the date at which the shares of the stock has to be bought.
   * @throws IllegalArgumentException if the share's worth (value) is entered as negative.
   * @throws IllegalArgumentException if the specifiedDate or tickerName is entered as null.
   * @throws IllegalArgumentException if the tickerName is not of the right format.
   * @throws IllegalArgumentException by propagating error in doing an api call or stock data
   *                                  creation.
   */
  void buyShares(String tickerName, double value, LocalDateTime specifiedDate)
          throws IllegalArgumentException;

  /**
   * Returns the cost basis of this portfolio at the specified time. The cost basis is the sum of
   * the total worth of the shares of the stocks at the time when they were bought. The date at
   * which this has to be computed is passed as an argument. If some stocks were not purchased on
   * the requested date they will not be counted.
   *
   * @param specifiedDate the date at which the cost basis of the portfolio has to be computed.
   * @return the cost basis on a given date as a double.
   * @throws IllegalArgumentException if the specified date is null.
   * @throws IllegalArgumentException by propagating error from api if the data doesnt't exist for
   *                                  that data.
   */
  double getCostBasis(LocalDateTime specifiedDate)
          throws IllegalArgumentException;

  /**
   * Returns the total worth of all the stocks in the portfolio at the date specified as the
   * argument. If some stocks were not purchased on the requested date they will not be counted.
   *
   * @param specifiedDate the date at which the total value of the portfolio has to be computed.
   * @return the value of this portfolio on a given date as a double.
   * @throws IllegalArgumentException if the specified date is null.
   * @throws IllegalArgumentException by propagating error from api if the data doesnt't exist for
   *                                  that data.
   */
  double getTotalValue(LocalDateTime specifiedDate)
          throws IllegalArgumentException;

  /**
   * Returns the title of the portfolio as a string which tells about what this portfolio is for.
   *
   * @return the title of the portfolio as a string which tells about what this portfolio is for.
   */
  String getPorfolioTitle();

  /**
   * Returns the information on what stocks are present in the portfolio, and their quantity.
   *
   * @return the information on what stocks are present in the portfolio, and their quantity.
   */
  Map<String, Double> getCompositionSimple();

}
