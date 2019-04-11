package stockemulation.controller;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

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
  public void verifyDates(Date date) {
    if (date == null) {
      isAllFieldsValid = false;
      view.setBuyFormDateError("Date cannot be empty");
    }
  }

  @Override
  public void verifyTime(LocalTime time) {
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
  public void verifyTicker(String ticker) {
    try {
      StockInfoSanity.isTickerValid(ticker);
    } catch (IllegalArgumentException e) {
      isAllFieldsValid = false;
      view.setBuyFormTickerError(e.getMessage());
    }
  }

  @Override
  public void verifyCost(String price) {
    if (price == null || price.isEmpty()) {
      isAllFieldsValid = false;
      view.setBuyFormPriceError("Price cannot be empty");
    } else {
      if (validatePrice(price)) {
        try {
          StockInfoSanity.isPriceValid(Double.parseDouble(price));
        } catch (IllegalArgumentException e) {
          isAllFieldsValid = false;
          view.setBuyFormTickerError(e.getMessage());
        }
      } else {
        view.setBuyFormPriceError("Price has to be a number");
        isAllFieldsValid = false;
      }
    }
  }

  @Override
  public void verifyCommission(String commission) {
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
    verifyDates(date);
    verifyTime(time);
    verifyTicker(ticker);
    verifyCost(cost);
    verifyCommission(commission);
    if (isAllFieldsValid) {
      LocalDateTime localDateTime = convertDateToLocalDate(date).atTime(time);
      model.buyStock(portfolioIndex, localDateTime, ticker, Double.parseDouble(cost),
              Double.parseDouble(commission));
      view.closeBuyStocksForm();
    }
  }

  @Override
  public void closeForm() {
    view.closeBuyStocksForm();
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
}
