package stockemulation.model;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * This is the extended interface class for {@link Model}. The extension includes buying shares of a
 * stock along with a commission cost fot tha transaction, the ability to write portfolio to a file
 * and read portfolio from a file.
 */
public interface ModelExtn extends Model {

  /**
   * This methods lets the controller buy a stock purchase to the selected portfolio. In the process
   * if any error occurred it is propagated to the controller to be handled. The stock purchase
   * include the following details: the name of the portfolio in which the purchase has to be added,
   * the stock name, date of purchase, the worth of stocks that have to bought, commission for the
   * transaction.
   *
   * @param portfolioNumber the portfolio to which the purchase has to be made.
   * @param date            the date at which the purchase has to be made.
   * @param ticker          the name of the stock.
   * @param price           the total value of the stock that has to be bought in that purchase.
   * @param commission      the commission cost for making the transaction.
   * @throws IllegalArgumentException if any of the input is invalid.
   * @throws IllegalArgumentException if any of the input causes member class to throw and error.
   */
  void buyStock(
          int portfolioNumber, LocalDateTime date, String ticker, Double price, double commission)
          throws IllegalArgumentException;

  /**
   * This method writes the details stored in the selected portfolio into a json file specified in
   * the path. Throws an error if it is not able to complete the process or if the portfolio doesnt
   * exist.
   *
   * @param portfolioNumber the portfolio which has to be written to a json file.
   * @param filepath        the path where the portfolio has to be written.
   * @throws IOException if the write process is not successful or if portfolio doesnt exist.
   */
  void writePortfolioToFile(String filepath, int portfolioNumber)
          throws IllegalArgumentException, IOException;

  /**
   * This method reads the portfolio details stored in a file at the specified path and loads it
   * into the program. The portfolio file should be  JSON file of the following format for the
   * content: {"title":"titlename", "transactions":[{"ticker":"tickername",
   * "purchaseDate":"datestring in LocalDateTime format", "quantity":value,"commission":value,
   * "costPerUnit":value}, {}...]}.
   *
   * @param filepath the path where the portfolio file is located.
   * @throws IllegalArgumentException if the file path is empty or null.
   * @throws IOException              it cannot load the file or if it doesn't exist.
   * @throws ParseException           if it cannot read from the loaded file,
   */
  void readPortfolioFromFile(String filepath)
          throws IllegalArgumentException, IOException, ParseException;


  /**
   * This function adds a specified elemental investment strategy data. Takes in the following
   * inputs and throws error if the operation fails or if the inputs are invalid.
   *
   * @param strategyName     name of the strategy.
   * @param tickerWeightMap  map of stock composition to their distribution weights.
   * @param investmentAmount the total amount that has to be invested using this strategy.
   * @param commission       the amount to be paid for each stock purchase in this investment.
   */
  void addStrategyData(
          String strategyName,
          Map<String, Double> tickerWeightMap,
          double investmentAmount,
          double commission
  ) throws IllegalArgumentException;

  /**
   * This method writes the details stored in the selected strategy into a json file specified in
   * the path. Throws an error if it is not able to complete the process or if the strategy doesnt
   * exist.
   *
   * @param strategyName the strategy which has to be written to a json file.
   * @param filepath     the path where the portfolio has to be written.
   * @throws IOException if the write process is not successful or if portfolio doesnt exist.
   */
  void writeStrategyToFile(String filepath, String strategyName)
          throws IllegalArgumentException, IOException;

  /**
   * This method reads the strategy details stored in a file at the specified path and loads it into
   * the program. The strategy file should be  JSON file of the following format for the content:
   * {strategyName: name, tickerWeightsMap:{map}, investmentAmount:amount, commission:commission}.
   *
   * @param filepath the path where the strategy file is located.
   * @throws IllegalArgumentException if the file path is empty or null.
   * @throws IOException              it cannot load the file or if it doesn't exist.
   * @throws ParseException           if it cannot read from the loaded file,
   */
  void readStrategyFromFile(String filepath)
          throws IllegalArgumentException, IOException, ParseException;

  /**
   * This is a one time investment in a portfolio with a selected elementary strategy. If either of
   * the strategy or the portfolio doesn't exist it throws an error. The investment has to be made
   * on a selected date. If investment cant be made on that date if does it on the next possible
   * date.
   *
   * @param portfolioNumber the portfolio in which the investment has to be made.
   * @param strategyName    the name of the strategy inside the selected portfolio which has to e
   *                        used for this investment.
   * @param investmentDate  the date at which this investment has to be made.
   */
  void investWithStrategy(int portfolioNumber, String strategyName, LocalDateTime investmentDate)
          throws IllegalArgumentException;

  /**
   * This returns a list of the strategies as their string names.
   *
   * @return a list of the strategies as their string names.
   */
  List<String> getStrategyList();

  /**
   * This is a high level strategy investment in a portfolio. You can perform periodic buys with
   * a strategy. To perform this you need to specify the start date, end date and the interval in
   * which this operation will happen. The buy operation will happen to the specification of the
   * strategy every n'th day from the start date till the end date. The nth day is specified as
   * the interval/frequency it is always specified as days. If a transaction falls on a weekend
   * the transaction will be retried on the upcoming weekday.
   *
   * @param portfolioNumber the portfolio index where this investment will happen.
   * @param strategyName    the name of the strategy used to perform the buy operation.
   * @param startDate       the date on which the transactions will start.
   * @param endDate         the date on which the transactions will end.
   * @param frequencyInDays the frequency interval with which transactions will happen. It is
   *                        specified in days.
   */
  void dollarCostAveraging(
          int portfolioNumber,
          String strategyName,
          LocalDateTime startDate,
          LocalDateTime endDate,
          int frequencyInDays
  ) throws IllegalArgumentException;


}
