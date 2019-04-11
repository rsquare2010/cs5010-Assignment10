package stockemulation.model;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * This is the extended interface class for {@link EmulatorModel}. The extension includes buying
 * shares of a stock along with a commission cost fot tha transaction, the ability to write
 * portfolio to a file and read portfolio from a file.
 */
public interface ModelExtn extends EmulatorModel {

  /**
   * This methods lets the controller add a stock purchase to the selected portfolio. In the process
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
}
