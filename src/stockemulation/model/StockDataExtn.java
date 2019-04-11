package stockemulation.model;

/**
 * This interface class is an extension to the {@link StockData} interface. This adds the method to
 * get the commission value that the user has to pay to make this transaction.
 */
interface StockDataExtn extends StockData {

  /**
   * Returns commission user had to pay when making this stock transaction as double in dollars.
   *
   * @return commission user had to pay when making this stock transactionas double in dollars.
   */
  double getCommission();

}
