package com.globant.pages.landing;

import java.time.LocalDate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.globant.pages.BasePage;
import com.globant.pages.results.TravelocityFlightResultsPage;

public class TravelocityFlightsHotelPage extends BasePage implements TravelocityLandingPage {

  //Stable locators
  @FindBy(id = "package-destination-flp-fh")
  private WebElement to;
  @FindBy(id = "flight-departing-flp")
  private WebElement departureDate;
  @FindBy(id = "flight-returning-flp")
  private WebElement returnDate;
  @FindBy(css = "#flight-departing-wrapper-flp>div>div>button.datepicker-paging.datepicker-next.btn-paging.btn-secondary.next")
  private WebElement nextMonth;
  //Stale locators
  private final By fromLocator = By.id("package-origin-flp-fh");
  private final By firstOptionLocator = By.cssSelector("#aria-option-0>span.text");

  public TravelocityFlightsHotelPage(WebDriver driver,
      FluentWait<WebDriver> wait) {
    super(driver, wait);
    isRetryingDisplayed(fromLocator);
  }

  @Override
  public void flights() {

  }

  @Override
  public TravelocityLandingPage flightsAndHotels() {
    return null;
  }

  @Override
  public void flyFrom(String departure) {
    retryingFindClick(fromLocator);
    retryingSendKeys(fromLocator, departure);
    retryingFindClick(firstOptionLocator);
  }

  @Override
  public void flyTo(String arrival) {
    getWait().until(ExpectedConditions.elementToBeClickable(to));
    to.click();
    to.sendKeys(arrival);
    retryingFindClick(firstOptionLocator);
  }

  @Override
  public void pickDepartureDate() {
    //Get the day in two months
    LocalDate localDate = LocalDate.now().plusMonths(2);
    int year = localDate.getYear();
    int month = localDate.getMonthValue() - 1;
    int day = localDate.getDayOfMonth();
    //Open the datepicker and set it to the day in two months
    getWait().until(ExpectedConditions.elementToBeClickable(departureDate));
    departureDate.click();
    WebDriverWait newWait = new WebDriverWait(getDriver(), 120);
    newWait.until(ExpectedConditions.elementToBeClickable(nextMonth));
    nextMonth.click();
    //Use the data you got to locate the link you are looking for.
    String selector = String
        .format("button.datepicker-cal-date[data-year='%s'][data-month='%s'][data-day='%s']", year,
            month, day);
    WebElement pick = getDriver().findElement(By.cssSelector(selector));
    pick.click();
  }

  @Override
  public void pickReturnDate(int days) {
    LocalDate localDate = LocalDate.now().plusMonths(2).plusDays(days);
    int year = localDate.getYear();
    int month = localDate.getMonthValue() - 1;
    int day = localDate.getDayOfMonth();
    getWait().until(ExpectedConditions.elementToBeClickable(returnDate));
    returnDate.click();
    String selector = String
        .format("button.datepicker-cal-date[data-year='%s'][data-month='%s'][data-day='%s']", year,
            month, day);
    WebElement pick = getDriver().findElement(By.cssSelector(selector));
    pick.click();
  }

  @Override
  public TravelocityFlightResultsPage search() {
    return null;
  }
}
