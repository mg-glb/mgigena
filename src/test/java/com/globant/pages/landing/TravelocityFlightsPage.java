package com.globant.pages.landing;

import java.time.LocalDate;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.globant.pages.BasePage;
import com.globant.pages.results.TravelocityFlightResultsPage;

public class TravelocityFlightsPage extends BasePage implements TravelocityLandingPage {

  private static final String PATH = "https://www.travelocity.com/Flights";

  @FindBy(css = "#tab-flight-tab-flp>span.tab-label")
  private WebElement flights;
  @FindBy(css = "#tab-flightHotel-tab-flp-fh>span.icon.icon-packages-double")
  private WebElement flightAndHotel;
  @FindBy(id = "flight-origin-flp")
  private WebElement from;
  @FindBy(id = "flight-destination-flp")
  private WebElement to;
  @FindBy(css = "#aria-option-0>span.text>div")
  private WebElement firstOption;
  @FindBy(id = "flight-departing-flp")
  private WebElement departureDate;
  @FindBy(css = "#flight-departing-wrapper-flp>div>div>button.datepicker-paging.datepicker-next.btn-paging.btn-secondary.next")
  private WebElement nextMonth;
  @FindBy(id = "primary-header-flight")
  private WebElement flightPage;
  @FindBy(css = "#gcw-flights-form-flp>div:nth-child(22)>label>button")
  private WebElement search;
  //Stale locators
  private final By flightsTabLocator = By.cssSelector("#tab-flight-tab-flp>span.tab-label");

  public TravelocityFlightsPage(WebDriver webDriver, FluentWait<WebDriver> webDriverFluentWait) {
    super(webDriver, webDriverFluentWait);
    webDriver.navigate().to(PATH);
    isRetryingDisplayed(flightsTabLocator);
  }

  @Override
  public void flights() {
    getWait().until(ExpectedConditions.elementToBeClickable(flights));
    flights.click();
  }

  @Override
  public TravelocityLandingPage flightsAndHotels() {
    TravelocityFlightsHotelPage page;
    getWait().until(ExpectedConditions.elementToBeClickable(flightAndHotel));
    flightAndHotel.click();
    page = new TravelocityFlightsHotelPage(getDriver(),getWait());
    return page;
  }

  @Override
  public void flyFrom(String fromString) {
    try {
      getWait().until(ExpectedConditions.elementToBeClickable(from));
      from.click();
      from.sendKeys(fromString);
    } catch (NoSuchElementException e) {
      e.printStackTrace();
    }
    WebDriverWait newWait = new WebDriverWait(getDriver(), 120);
    newWait.until(ExpectedConditions.elementToBeClickable(firstOption));
    firstOption.click();
  }

  @Override
  public void flyTo(String toString) {
    getWait().until(ExpectedConditions.elementToBeClickable(to));
    to.click();
    to.sendKeys(toString);
    WebDriverWait newWait = new WebDriverWait(getDriver(), 120);
    newWait.until(ExpectedConditions.elementToBeClickable(firstOption));
    try {
      firstOption.click();
    } catch (StaleElementReferenceException e) {
      e.printStackTrace();
    }
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
  public TravelocityFlightResultsPage search() {
    getWait().until(ExpectedConditions.elementToBeClickable(search));
    search.click();
    TravelocityFlightResultsPage page = new TravelocityFlightResultsPage(getDriver(), getWait());
    PageFactory.initElements(getDriver(), page);
    return page;
  }

  @Override
  public void pickReturnDate(int i) {

  }
}
