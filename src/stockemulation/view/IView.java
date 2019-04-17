package stockemulation.view;

import java.util.Map;

import stockemulation.controller.Features;

/**
 * An Interface that extends from the EmulatorView interface which provides methods to support a
 * graphical user interface. This GUI will facilitate the user perform actions like creating a new
 * portfolio, checking the contents of a portfolio, checking the cost basis and value of a
 * portfolio on a given date, providing appropriate fields to buy stocks. It will also display
 * relevant informational and error messages.
 */
public interface IView extends EmulatorView {

  /**
   * Get the set of feature callbacks that the view can use.
   * @param f the set of feature callbacks as a Features object
   */
  void setFeatures(Features f);

  /**
   * Update the UI to show the list of portfolios to the user in the appropriate format.
   * @param portfolioList the list of portfolios that have to be displayed to the user.
   */
  void setPortfolioList(String[] portfolioList);

  /**
   * Show informational messages to the user.
   * @param message the message to be displayed to the user.
   */
  void showMessage(String message);

  /**
   * Show error messages to the user.
   * @param message the error message to be shown.
   */
  void showErrorMessage(String message);

  /**
   * Show relevant UI to the users to get the data to perform the Buy stocks operation.
   */
  void showBuyStocksForm();

  /**
   * Show relevant UI to the users to get the data to perform the Buy stocks operation.
   */
  void showCreateStrategyForm();

  /**
   * Close/hide the UI that was shown to facilitate the user to buy stocks.
   */
  void closeBuyStocksForm();

  /**
   * Show an error message when the date entered by the user is invalid.
   * @param message to be displayed when invalid date is entered.
   */
  void setBuyFormDateError(String message);

  /**
   * Show an error message when the time entered by the user is invalid.
   * @param message to be displayed when invalid time is entered.
   */
  void setBuyFormTimeError(String message);

  /**
   * Show an error message when the ticker information entered by the user is invalid.
   * @param message to be displayed when invalid ticker information is entered.
   */
  void setBuyFormTickerError(String message);

  /**
   * Show an error message when the price entered by the user is invalid.
   * @param message to be displayed when invalid price is entered.
   */
  void setBuyFormPriceError(String message);

  /**
   * Show an error message when the commission fees entered by the user is invalid.
   * @param message to be displayed when invalid commission fees is entered.
   */
  void setBuyFormCommissionError(String message);

  /**
   * Display the contents of a portfolio to the user using appropriate UI.
   * @param portfolioSummary represents the contents of a portfolio.
   */
  void setPortfolioSummary(Map<String, Double> portfolioSummary);

  /**
   * Display the cost basis and value of a portfolio to the user using appropriate UI.
   * @param costBasis the cost basis of the portfolio on a given date.
   * @param value the value of a portfolio on a given date.
   */
  void setPortfolioValueOnDate(Double costBasis, Double value);

  /**
   * Show an error message when the strategy name entered by the user is invalid.
   * @param message to be displayed when invalid strategy name information is entered.
   */
  void setStrategyFormNameError(String message);

  /**
   * Show an error message when the ticker information entered by the user is invalid.
   * @param message to be displayed when invalid ticker information is entered.
   */
  void setStrategyFormTickerError(String message);

  /**
   * Show an error message when the price entered by the user is invalid.
   * @param message to be displayed when invalid price is entered.
   */
  void setStrategyFormPriceError(String message);

  /**
   * Show an error message when the commission fees entered by the user is invalid.
   * @param message to be displayed when invalid commission fees is entered.
   */
  void setStrategyFormCommissionError(String message);

  /**
   * method to close the form used to create a new strategy.
   */
  void closeAddStrategyForm();

  /**
   * Show the form to perform a single buy with distributed weights for multiple stocks in a
   * portfolio.
   * @param strategies the weights of different stocks and the ticker name associated with them.
   */
  void showSingleBuyStrategy(String[] strategies);

  /**
   * Method to close the single strategy buy form.
   */
  void closeSingleStrategyBuyForm();

  /**
   * Show an error message when the date entered by the user is invalid.
   * @param message to be displayed when invalid date is entered.
   */
  void setSingleBuyStrategyDateError(String message);

  /**
   * Show the form used to perform dollar cost averaging buy operation.
   * @param strategies the list of weight and associated tickers that can be used to perform DCA.
   */
  void showDollarCostAverageStrategy(String[] strategies);

  /**
   * Close the form used to perform dollar cost averaging buy operation.
   */
  void closeDollarCostAverageForm();

  /**
   * Show an error message when the interval number of days specified by the user is invalid.
   * @param message to be displayed when invalid interval number of days is entered.
   */
  void setIntervalDCAError(String message);

  /**
   * Show an error message when the date entered by the user is invalid.
   * @param message to be displayed when invalid date is entered.
   */
  void setStartDateDCAError(String message);

  /**
   * Show an error message when the date entered by the user is invalid.
   * @param message to be displayed when invalid date is entered.
   */
  void setEndDateDCAError(String message);

  /**
   * Show the form to select a strategy from a list of strategies to be saved.
   * @param strategies the list of strategies from which a strategy will be chosen.
   */
  void showSelectStrategyForm(String[] strategies);

  /**
   * hide the form which lets you choose a strategy from a list of strategies.
   */
  void hideSelectStrategyForm();
}
