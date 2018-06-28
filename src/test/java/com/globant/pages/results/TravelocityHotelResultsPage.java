package com.globant.pages.results;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.globant.pages.BasePage;
import com.globant.pages.TravelocityReturnFlightPage;

public class TravelocityHotelResultsPage extends BasePage implements TravelocityResultsPage {
  //Stable locators
  @FindBy(css = "#multiStepIndicatorContainer>div>div.msi-active-state>h3>span.msi-label")
  private WebElement hotelTag;
  @FindBy(css = "#multiStepIndicatorContainer>div>div:nth-child(2)>h3>span.msi-label")
  private WebElement roomTag;
  @FindBy(css = "#multiStepIndicatorContainer>div>div:nth-child(3)>h3>span.msi-label")
  private WebElement flightsTag;
  @FindBy(id = "airline_fees_link")
  public WebElement flightDisclaimer;
  //Stale locators
  private final By hotelTitle = By.cssSelector("#hotelResultTitle>h1");

  public TravelocityHotelResultsPage(WebDriver driver,
      FluentWait<WebDriver> wait) {
    super(driver, wait);
    WebDriverWait newWait = new WebDriverWait(driver,120);
    newWait.until(ExpectedConditions.presenceOfElementLocated(hotelTitle));
  }

  @Override
  public boolean checkSort() {
    return false;
  }

  @Override
  public boolean checkSelectButtonForAll() {
    return false;
  }

  @Override
  public boolean checkFlightDurationForAll() {
    return false;
  }

  @Override
  public boolean checkDetailsForAll() {
    return false;
  }

  @Override
  public void sortByDistanceShortest() {

  }

  @Override
  public boolean checkSortedByDistance(int i) {
    return false;
  }

  @Override
  public TravelocityReturnFlightPage selectFirstResult() {
    return null;
  }

  @Override
  public String[] selectedDepartureAndArrivalTimes() {
    return new String[0];
  }

  @Override
  public String hotelTextTag() {
    getWait().until(ExpectedConditions.visibilityOf(hotelTag));
    System.out.println(flightDisclaimer.getText());
    return hotelTag.getText();
  }

  @Override
  public String roomTextTag() {
    getWait().until(ExpectedConditions.visibilityOf(roomTag));
    return roomTag.getText();
  }

  @Override
  public String flightTextTag() {
    getWait().until(ExpectedConditions.visibilityOf(flightsTag));
    return flightsTag.getText();
  }

  @Override
  public String titleTextTag() {
    return retryingGetText(hotelTitle);
  }
}
