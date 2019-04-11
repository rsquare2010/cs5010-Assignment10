package stockemulation.model;

import java.time.LocalDate;

/**
 * An interface for the API part of the model, this interface provides methods which is used to
 * obtain information about stocks.
 */
interface API {

  /**
   * This is a factory mehtod which returns a static singleton reference to one of the
   * implementations of the {@link API} interface.
   * @param apiName the string parameter which helps identify the implementation to be returned.
   * @return a static singleton reference to an implementation fo the API interface.
   * @throws IllegalArgumentException if the provided string parameter is not tied to a class that
   *                                  implements the {@link API} interface.
   */
  static API getInstance(APITypes apiName) throws IllegalArgumentException {
    if (apiName == APITypes.ALPHA_VANTAGE) {
      return APIImpl.getInstance();
    } else if (apiName == APITypes.MOCK_API) {
      return APIMock.getInstance();
    }
    throw new IllegalArgumentException("Invalid Datasource");
  }

  /**
   * This method takes in the stock ticker and the date to provide the value of the opening value of
   * the stock on that day provided the markets were open on that day.
   *
   * @param date        The date on which to obtain the opening price of a stock.
   * @param stockSymbol The ticker of the stock whose price has to be fetched using the API.
   * @return The opening price of the stock represented using the stockSymbol on the date provided,
   *         the price is a double value.
   * @throws IllegalArgumentException is thrown if there are issues related to obtaining the price
   *                                  through an API.
   */
  double getPriceAtTime(LocalDate date, String stockSymbol) throws IllegalArgumentException;
}
