package stockemulation.view;

import java.util.Map;

import javax.swing.*;

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

  void setStrategyFormNameError(String message);

  void setStrategyFormTickerError(String message);

  void setStrategyFormPriceError(String message);

  void setStrategyFormCommissionError(String message);

  void closeStrategyForm();
}
