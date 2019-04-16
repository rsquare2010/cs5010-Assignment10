package stockemulation.controller;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stockemulation.model.ModelExtn;
import stockemulation.util.StockInfoSanity;
import stockemulation.view.EmulatorView;
import stockemulation.view.IView;

/**
 * An implementation of the {@link GUIController}. This class contains methods to facilitate
 * validate input from the user in the contest of a stock market emulator application. It further
 * performs actions based on the user's input and generates information to be displayed to the user.
 * This generated information is passed to the {@link EmulatorView} from this class.
 */
public class GUIControllerImpl implements GUIController, Features {

  private ModelExtn model;
  private IView view;
  private boolean isAllFieldsValid = false;


  @Override
  public void start(ModelExtn model, IView view) {
    this.model = model;
    this.view = view;
    this.view.setFeatures(this);
    this.view.setPortfolioList(this.model.getPortfolioList().toArray(new String[0]));
  }


  @Override
  public void createPortfolio(String portfolioName) {
    boolean isCreatedSuccessfully = false;
    if (portfolioName != null && !(portfolioName.trim().isEmpty())) {
      try {
        isCreatedSuccessfully = model.createPortfolio(portfolioName.trim());
      } catch (IllegalArgumentException e) {
        view.showErrorMessage("Portfolio Already exists");
      }
    } else {
      view.showErrorMessage("Please enter a non empty name to create a portfolio");
    }
    if (isCreatedSuccessfully) {
      view.setPortfolioList(this.model.getPortfolioList().toArray(new String[0]));
      view.showMessage("Portfolio " + portfolioName.trim() + " created successfully");
    }
  }

  @Override
  public void verifyDatesForBuyForm(Date date) {
    if (date == null) {
      isAllFieldsValid = false;
      view.setBuyFormDateError("Date cannot be empty");
    }
  }

  @Override
  public void verifyTimeForBuyForm(LocalTime time) {
    if (time == null) {
      isAllFieldsValid = false;
      view.setBuyFormTimeError("Time cannot be empty");
    } else {
      try {
        StockInfoSanity.isTimeValid(time);
      } catch (IllegalArgumentException e) {
        isAllFieldsValid = false;
        view.setBuyFormTimeError(e.getMessage());
      }
    }
  }

  @Override
  public void verifyTickerForBuyForm(String ticker) {
    try {
      StockInfoSanity.isTickerValid(ticker);
    } catch (IllegalArgumentException e) {
      isAllFieldsValid = false;
      view.setBuyFormTickerError(e.getMessage());
    }
  }

  @Override
  public void verifyCostForBuyForm(String price) {
    if (price == null || price.isEmpty()) {
      isAllFieldsValid = false;
      view.setBuyFormPriceError("Price cannot be empty");
    } else {
      if (validatePrice(price)) {
        try {
          StockInfoSanity.isPriceValid(Double.parseDouble(price));
        } catch (IllegalArgumentException e) {
          isAllFieldsValid = false;
          view.setBuyFormPriceError(e.getMessage());
        }
      } else {
        view.setBuyFormPriceError("Price has to be a number");
        isAllFieldsValid = false;
      }
    }
  }

  @Override
  public void verifyCommissionForBuyForm(String commission) {
    if (commission == null) {
      isAllFieldsValid = false;
      view.setBuyFormCommissionError("Commission is invalid");
    } else {
      if (validatePrice(commission)) {
        try {
          StockInfoSanity.isCommissionValid(Double.parseDouble(commission));
        } catch (IllegalArgumentException e) {
          isAllFieldsValid = false;
          view.setBuyFormCommissionError(e.getMessage());
        }
      } else {
        view.setBuyFormCommissionError("Commission has to be a number");
        isAllFieldsValid = false;
      }
    }
  }

  @Override
  public void verifyPriceForStrategyForm(String price) {
    if (price == null || price.isEmpty()) {
      isAllFieldsValid = false;
      view.setStrategyFormPriceError("Price cannot be empty");
    } else {
      if (validatePrice(price)) {
        try {
          StockInfoSanity.isPriceValid(Double.parseDouble(price));
        } catch (IllegalArgumentException e) {
          isAllFieldsValid = false;
          view.setStrategyFormTickerError(e.getMessage());
        }
      } else {
        view.setStrategyFormPriceError("Price has to be a number");
        isAllFieldsValid = false;
      }
    }
  }

  @Override
  public void verifyCommissionForStrategyForm(String commission) {
    if (commission == null) {
      isAllFieldsValid = false;
      view.setStrategyFormCommissionError("Commission is invalid");
    } else {
      if (validatePrice(commission)) {
        try {
          StockInfoSanity.isCommissionValid(Double.parseDouble(commission));
        } catch (IllegalArgumentException e) {
          isAllFieldsValid = false;
          view.setStrategyFormCommissionError(e.getMessage());
        }
      } else {
        view.setStrategyFormCommissionError("Commission has to be a number");
        isAllFieldsValid = false;
      }
    }
  }

  @Override
  public void verifyTickerNameForStrategyForm(String tickerName) {
    try {
      StockInfoSanity.isTickerValid(tickerName);
    } catch (IllegalArgumentException e) {
      isAllFieldsValid = false;
      view.setStrategyFormTickerError(e.getMessage());
    }
  }

  @Override
  public void createAStrategy(int portfolioNumber,String strategyName,
                              Map<String, String> tickerWeights, String price, String commission) {

    isAllFieldsValid = true;
    verifyStrategyName(portfolioNumber, strategyName);
    verifyWeights(tickerWeights);
    verifyPriceForStrategyForm(price);
    verifyCommissionForStrategyForm(commission);
    if(isAllFieldsValid) {
      Map<String, Double> weights = new HashMap<>();
      for(Map.Entry entry : tickerWeights.entrySet()){
        weights.put(entry.getKey().toString(), Double.parseDouble(entry.getValue().toString()));
      }
      model.addStrategyToPortfolio(portfolioNumber, strategyName, weights,
              Double.parseDouble(price), Double.parseDouble(commission));//FIXME catch exception.
      view.closeStrategyForm();
    }
  }

  private void verifyStrategyName(int portfolioNumber, String name) {
    if (name != null && !(name.trim().isEmpty())) {
      List<String> strategyList = model.getStrategyListFrompPortfolio(portfolioNumber);
      if(strategyList.contains(name)) {
        isAllFieldsValid = false;
        view.setStrategyFormNameError("A strategy with the same name already exists for the " +
                "portfolio");
      }
    } else {
      view.setStrategyFormNameError(" Please Enter a valid strategy");
      isAllFieldsValid = false;
    }
  }

  private void verifyWeights(Map<String, String> weights) {
    double sum = 0.0;
    for(Map.Entry entry : weights.entrySet()){
      verifyTickerNameForStrategyForm(entry.getKey().toString());
      if (validatePrice(entry.getValue().toString())) {
        try {
          StockInfoSanity.isWeightValid(Double.parseDouble(entry.getValue().toString()));
        } catch (IllegalArgumentException e) {
          isAllFieldsValid = false;
          view.setStrategyFormTickerError(e.getMessage());
        }
        if(isAllFieldsValid) {
          sum += Double.parseDouble(entry.getValue().toString());
        }
      } else {
        view.setStrategyFormTickerError("Weight has to be a number between 0 and 100");
        isAllFieldsValid = false;
      }
    }
    if(100 - sum > 0.01 || sum - 100 > 0.01) { //Check weights with precision.
      isAllFieldsValid = false;
      view.setStrategyFormTickerError("sum of weights should be 100");
    }
  }

  @Override
  public void buyStocks() {
    if (model.getPortfolioCount() == 0) {
      view.showErrorMessage("Please create a portfolio before you buy stocks");
    } else {
      view.showBuyStocksForm();
    }
  }

  @Override
  public void verifyFormAndBuy(Date date, LocalTime time, String ticker,
                               String cost, String commission, int portfolioIndex) {
    isAllFieldsValid = true;
    verifyDatesForBuyForm(date);
    verifyTimeForBuyForm(time);
    verifyTickerForBuyForm(ticker);
    verifyCostForBuyForm(cost);
    verifyCommissionForBuyForm(commission);
    if (isAllFieldsValid) {
      LocalDateTime localDateTime = convertDateToLocalDate(date).atTime(time);
      model.buyStock(portfolioIndex, localDateTime, ticker, Double.parseDouble(cost),
              Double.parseDouble(commission)); //FIXME catch exceptions.
      view.closeBuyStocksForm();
    }
  }

  @Override
  public void closeBuyForm() {
    view.closeBuyStocksForm();
  }

  @Override
  public void closeStrategyForm() {
    view.closeStrategyForm();
  }

  @Override
  public void getPortfolioSummary(int portfolioIndex) {
    if (model.getPortfolioCount() != 0) {
      view.setPortfolioSummary(model.getPortfolioDetails(portfolioIndex));
    }
  }

  @Override
  public void getPortfolioValue(int portfolioIndex, Date date) {
    if (model.getPortfolioCount() != 0) {
      LocalDateTime dateTime = LocalDateTime.of(convertDateToLocalDate(date), LocalTime.NOON);
      Double costBasis = model.getCostBasis(portfolioIndex, dateTime);
      Double value = model.getTotalValue(portfolioIndex, dateTime);
      view.setPortfolioValueOnDate(costBasis, value);
    }
  }

  @Override
  public void writeToFile(String filePath, int portfolioIndex) {
    try {
      model.writePortfolioToFile(filePath, portfolioIndex);
    } catch (IOException | IllegalArgumentException e) {
      view.showErrorMessage(e.getMessage());
    }
  }

  @Override
  public void readFromFile(String filePath) {
    boolean isError = false;
    try {
      model.readPortfolioFromFile(filePath);
    } catch (IllegalArgumentException | ParseException | IOException e) {
      isError = true;
      view.showErrorMessage(e.getMessage());
    }
    if (!isError) {
      view.setPortfolioList(this.model.getPortfolioList().toArray(new String[0]));
    }
  }

//  @Override
//  public void createAStrategy() {
//    if (model.getPortfolioCount() == 0) {
//      view.showErrorMessage("Please create a portfolio before you buy stocks");
//    } else {
//      view.showCreateStrategyForm();
//    }
//  }

  private LocalDate convertDateToLocalDate(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  private boolean validatePrice(String cost) {
    try {
      Double.parseDouble(cost);
    } catch (NumberFormatException exception) {
      return false;
    }
    return true;
  }

  @Override
  public void createStrategy() {
    if (model.getPortfolioCount() == 0) {
      view.showErrorMessage("Please create a portfolio before you create a Strategy");
    } else {
      view.showCreateStrategyForm();
    }
  }
}
