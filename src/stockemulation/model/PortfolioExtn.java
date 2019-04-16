package stockemulation.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * This is the extended interface class for {@link Portfolio}. The extension includes buying shares
 * of a stock along with a commission cost fot tha transaction. having a feature to compute the cost
 * basis of the portfolio along with the commission details and the ability to write portfolio to a
 * file. This extension also includes elementary/one-time investment functions along with investing
 * using a strategy. Ways to create new strategy data of the type {@link StrategyData} and getters
 * to fetch individual and a list of strategies.
 */
interface PortfolioExtn extends Portfolio {

  /**
   * Buy shares of a particular stock. The quantity of shares to be bought is computed from the
   * specified total worth of shares which have to be bought. The user also has to specify when this
   * particular stock has to be bought. The user also has to specify the commission cost for making
   * this transaction.
   *
   * @param tickerName    the ticker name of stock of format "XXXX" (all in caps).
   * @param value         the total worth of shares of the particular stock which has to be bought.
   * @param specifiedDate the date at which the shares of the stock has to be bought.
   * @param commission    the commission cost for making this transaction.
   * @throws IllegalArgumentException if the share's worth (value) is entered as negative.
   * @throws IllegalArgumentException if the specifiedDate or tickerName is entered as null.
   * @throws IllegalArgumentException if the tickerName is not of the right format.
   * @throws IllegalArgumentException by propagating error in doing an api call or stock data
   *                                  creation.
   */
  void buyShares(String tickerName, double value, LocalDateTime specifiedDate, double commission)
          throws IllegalArgumentException;

  /**
   * Add shares of a particular stock. The quantity of shares to be bought is computed from the
   * specified total worth of shares which have to be bought. The user also has to specify when this
   * particular stock has to be bought. The user also has to specify the commission cost for making
   * this transaction.This is useful to add transaction data without making a purchase or doing a
   * api call.
   *
   * @param tickerName    the ticker name of stock of format "XXXX" (all in caps).
   * @param costPerUnit   the cost of one uit share of the stock on the purchase date.
   * @param quantity      the total shares of the stock bought in the transaction.
   * @param specifiedDate the date at which the shares of the stock has to be bought.
   * @param commission    the commission cost for making this transaction.
   * @throws IllegalArgumentException if the share's worth (value) is entered as negative.
   * @throws IllegalArgumentException if the specifiedDate or tickerName is entered as null.
   * @throws IllegalArgumentException if the tickerName is not of the right format.
   * @throws IllegalArgumentException by propagating error in doing an api call or stock data
   *                                  creation.
   */
  void addShares(
          String tickerName,
          double costPerUnit,
          double quantity,
          LocalDateTime specifiedDate,
          double commission)
          throws IllegalArgumentException;

  /**
   * Returns the cost basis of this portfolio at the specified time and also includes the commission
   * costs. The cost basis is the sum of the total worth of the shares of the stocks at the time
   * when they were bought. The date at which this has to be computed is passed as an argument. If
   * some stocks were not purchased on the requested date they will not be counted.
   *
   * @param specifiedDate the date at which the cost basis of the portfolio has to be computed.
   * @return the cost basis on a given date as a double.
   * @throws IllegalArgumentException if the specified date is null.
   * @throws IllegalArgumentException by propagating error from api if the data doesnt't exist for
   *                                  that data.
   */
  @Override
  double getCostBasis(LocalDateTime specifiedDate)
          throws IllegalArgumentException;

  /**
   * This method writes the details stored in the portfolio into a json file specified in the path.
   * Throws an error if it is not able to complete the process.
   *
   * @param filepath the path where the portfolio has to be written.
   * @throws IOException if the write process is not successful.
   */
  void writeToFile(String filepath) throws IOException;


  /**
   * This is an elementary investment operation. This takes in a composition of stocks to invest in
   * and their distribution weights for the total investment amount, along with the commission. Then
   * buys shares for the stocks, don't exist in the portfolio it gets added while buying. The whole
   * operation is performed on a specific date. if it is not possible to invest on that date because
   * of a holiday. It will make the investment in the next possible date.
   *
   * @param investmentDate        the date at which the investment has to be made.
   * @param totalInvestmentAmount the amount that has to be invested,
   * @param stockWeights          the map of stock composition and distribution weights.
   * @param commission            the amount to be paid for this each buy in this investment.
   */
  void investWeighted(
          LocalDateTime investmentDate,
          double totalInvestmentAmount,
          Map<String, Double> stockWeights,
          double commission
  ) throws IllegalArgumentException;

  /**
   * This is an elementary investment operation. This invests a certain amount by distributing the
   * value on each of the stock that exists in this portfolio equally. The whole operation is
   * performed on a specific date. if it is not possible to invest on that date because of a
   * holiday. It will make the investment in the next possible date.
   *
   * @param investmentDate        the date at which this investment has to be made.
   * @param totalInvestmentAmount the amount that has to be invested,
   * @param commission            the amount to be paid for this each buy in this investment.
   */
  void investEqual(
          LocalDateTime investmentDate,
          double totalInvestmentAmount,
          double commission
  ) throws IllegalArgumentException;
}
