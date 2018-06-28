package com.globant.pages.landing;

import java.time.LocalDate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.globant.pages.BasePage;
import com.globant.pages.results.TravelocityHotelResultsPage;

public class TravelocityMainHotelPage extends BasePage implements TravelocityLandingPage {

  //Stable locators
  @FindBy(id = "package-destination-hp-package")
  private WebElement to;
  @FindBy(id = "package-departing-hp-package")
  private WebElement departureDate;
  @FindBy(id = "package-returning-hp-package")
  private WebElement returnDate;
  @FindBy(css = "#package-departing-wrapper-hp-package>div>div>button.datepicker-paging.datepicker-next.btn-paging.btn-secondary.next")
  private WebElement nextMonth;
  @FindBy(id = "search-button-hp-package")
  private WebElement search;
  //Stale locators
  private By flightHotelLocator = By
      .cssSelector("#gcw-packages-form-hp-package>fieldset>div>div:nth-child(1)>label");
  private final By fromLocator = By.id("package-origin-hp-package");
  private final By firstOptionLocator = By.id("aria-option-0");

  public TravelocityMainHotelPage(WebDriver driver,
      FluentWait<WebDriver> wait) {
    super(driver, wait);
    isRetryingDisplayed(flightHotelLocator);
  }

  @Override
  public void flights() {

  }

  @Override
  public TravelocityLandingPage flightsAndHotels() {
    return null;
  }

  @Override
  public void flyFrom(String fromString) {
    retryingFindClick(fromLocator);
    retryingSendKeys(fromLocator, fromString);
    retryingFindClick(firstOptionLocator);
  }

  @Override
  public void flyTo(String toString) {
    getWait().until(ExpectedConditions.elementToBeClickable(to));
    to.click();
    to.sendKeys(toString);
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
  public TravelocityHotelResultsPage search() {
    getWait().until(ExpectedConditions.elementToBeClickable(search));
    search.click();
    TravelocityHotelResultsPage page = new TravelocityHotelResultsPage(getDriver(), getWait());
    PageFactory.initElements(getDriver(), page);
    return page;
  }
}
