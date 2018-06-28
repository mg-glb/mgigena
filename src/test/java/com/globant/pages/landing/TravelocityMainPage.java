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
import com.globant.pages.results.TravelocityFlightResultsPage;

public class TravelocityMainPage extends BasePage implements TravelocityLandingPage {

  private static final String PATH = "https://www.travelocity.com/";
  @FindBy(id = "tab-flight-tab-hp")
  private WebElement flights;
  @FindBy(css = "#tab-package-tab-hp>span.icon.icon-packages-double")
  private WebElement flightAndHotel;
  @FindBy(id = "flight-destination-hp-flight")
  private WebElement to;
  @FindBy(id = "flight-departing-hp-flight")
  private WebElement departureDate;
  @FindBy(css = "#flight-departing-wrapper-hp-flight>div>div>button.datepicker-paging.datepicker-next.btn-paging.btn-secondary.next")
  private WebElement nextMonth;
  @FindBy(css = "#gcw-flights-form-hp-flight>div:nth-child(22)>label>button")
  private WebElement search;
  @FindBy(id = "primary-header-flight")
  private WebElement flightPage;
  //Stale locators
  private final By flightsTabLocator = By.id("tab-flight-tab-hp");
  private final By fromLocator = By.id("flight-origin-hp-flight");
  private final By firstOptionLocator = By.cssSelector("#aria-option-0>span.text>div");

  public TravelocityMainPage(WebDriver driver, FluentWait<WebDriver> wait) {
    super(driver, wait);
    driver.navigate().to(PATH);
    isRetryingDisplayed(flightsTabLocator);
  }

  /**
   * This method waits for the element flights to be clickable, and then proceeds to click such
   * element.
   *
   * @author Maximiliano Gigena
   */
  @Override
  public void flights() {
    getWait().until(ExpectedConditions.elementToBeClickable(flights));
    flights.click();
  }

  @Override
  public TravelocityLandingPage flightsAndHotels() {
    TravelocityMainHotelPage page;
    getWait().until(ExpectedConditions.elementToBeClickable(flightAndHotel));
    flightAndHotel.click();
    page = new TravelocityMainHotelPage(getDriver(),getWait());
    PageFactory.initElements(getDriver(),page);
    return page;
  }

  /**
   * This method fills the Fly From text box, and waits for the first option to show up.
   *
   * @param fromString Make sure that this string is a valid airport name (i.e: LAS or LAX).
   * @author Maximiliano Gigena
   */
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
