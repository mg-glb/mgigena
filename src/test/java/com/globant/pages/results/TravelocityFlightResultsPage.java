package com.globant.pages.results;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.globant.pages.TravelocityReturnFlightPage;

public class TravelocityFlightResultsPage extends BasePage implements TravelocityResultsPage {

  //Stable locators;
  @FindBy(css = "#flightModuleList>li>div>div.uitk-grid.all-grid-fallback-alt")
  private List<WebElement> listingBodies;
  @FindBy(css = "#flightModuleList>li>div>div.uitk-grid.all-grid-fallback-alt>div.uitk-col.all-col-shrink>div>div.uitk-col.standard-col-l-margin.all-col-shrink.display-larger-screens-only>button")
  private List<WebElement> selectButtons;
  @FindBy(css = "#flightModuleList>li.flight-module.segment.offer-listing>div>div.uitk-grid.all-grid-fallback-alt>div.uitk-col.all-col-fill.custom-short-r-margin>div>div>div>div.uitk-col.tablet-col-1-2.desktop-col-1-2.all-col-fill>div.fluid-content.inline-children.custom-primary-padding>span.duration-emphasis")
  private List<WebElement> flightDurations;
  @FindBy(css = "#flightModuleList>li>div>div.details-container.standard-t-margin.secondary-content.link-style.display-larger-screens-only>span.custom-col-r-margin>a>span.show-flight-details")
  private List<WebElement> detailLists;
  @FindBy(css = "#flightModuleList>li:first-child>div.grid-container.standard-padding>div.uitk-grid.all-grid-fallback-alt>div.uitk-col.all-col-fill.custom-short-r-margin>div>div>div>div.uitk-col.tablet-col-1-3.desktop-col-1-3.custom-col-r-margin.min-width-large-screens-only>div.primary-content.no-wrap.custom-primary-padding>span>span:first-child")
  private WebElement selectedDepartureTime;
  @FindBy(css = "#flightModuleList>li:first-child>div.grid-container.standard-padding>div.uitk-grid.all-grid-fallback-alt>div.uitk-col.all-col-fill.custom-short-r-margin>div>div>div>div.uitk-col.tablet-col-1-3.desktop-col-1-3.custom-col-r-margin.min-width-large-screens-only>div.primary-content.no-wrap.custom-primary-padding>span>span:nth-child(2)")
  private WebElement selectedArrivalTime;

  //Stale locators
  private final By departureTitle = By.cssSelector("#titleBar>h1>div.title-departure");
  private final By sortLocator = By.cssSelector("#sortBar>div>fieldset>label>select");
  private final By listings = By.cssSelector(
      "#flightModuleList>li>div>div.uitk-grid.all-grid-fallback-alt>div.uitk-col.all-col-fill.custom-short-r-margin>div>div>div>div.uitk-col.tablet-col-1-2.desktop-col-1-2.all-col-fill>div.fluid-content.inline-children.custom-primary-padding>span.duration-emphasis");
  private final By fareLocator = By
      .cssSelector("#flightModuleList>li:first-child>div.basic-economy-tray.uitk-grid>button");
  private final By firstButtonSelector = By.cssSelector(
      "#flightModuleList>li:first-child>div>div.uitk-grid.all-grid-fallback-alt>div.uitk-col.all-col-shrink>div>div.uitk-col.standard-col-l-margin.all-col-shrink.display-larger-screens-only>button");
  private final By selectFare = By.cssSelector(
      "#flightModuleList>li:first-child>div.basic-economy-tray.uitk-grid>div>div>div.basic-economy-footer.uitk-grid.all-grid-nowrap>button");

  public TravelocityFlightResultsPage(WebDriver driver, FluentWait<WebDriver> wait) {
    super(driver, wait);
    //TODO here an exception can appear if the flight selector fails.
    isRetryingDisplayed(departureTitle);
  }

  @Override
  public boolean checkSort() {
    boolean stable = true;
    String[] sortTypes = {"Price (Lowest)", "Price (Highest)", "Duration (Shortest)",
        "Duration (Longest)", "Departure (Earliest)", "Departure (Latest)", "Arrival (Earliest)",
        "Arrival (Latest)"};
    for (String sort : sortTypes) {
      try {
        retryingSelect(sortLocator, sort);
      } catch (NoSuchElementException exception) {
        stable = false;
      }
    }
    return stable;
  }

  @Override
  public boolean checkSelectButtonForAll() {
    return listingBodies.size() == selectButtons.size();
  }

  @Override
  public boolean checkFlightDurationForAll() {
    boolean check = listingBodies.size() == flightDurations.size();
    if (check) {
      for (WebElement duration : flightDurations) {
        check &= duration.getText().matches("\\d+h \\d+m");
      }
    }
    return check;
  }

  @Override
  public boolean checkDetailsForAll() {
    return detailLists.size() == listingBodies.size();
  }

  @Override
  public void sortByDistanceShortest() {
    int size = flightDurations.size();
    retryingSelect(sortLocator, "Duration (Shortest)");
    getWait().until(webDriver -> {
      List<WebElement> newList = webDriver.findElements(listings);
      return newList.size() == size;
    });
    WebDriverWait newWait = new WebDriverWait(getDriver(), 120);
    newWait.until(webDriver -> checkSortedByDistance(size));
    PageFactory.initElements(getDriver(), this);
  }

  @Override
  public boolean checkSortedByDistance(int size) {
    By newFlightDurationsSelector = By.cssSelector(
        "#flightModuleList>li.flight-module.segment.offer-listing>div>div.uitk-grid.all-grid-fallback-alt>div.uitk-col.all-col-fill.custom-short-r-margin>div>div>div>div.uitk-col.tablet-col-1-2.desktop-col-1-2.all-col-fill>div.fluid-content.inline-children.custom-primary-padding>span.duration-emphasis");
    boolean check = true;
    Pattern pattern = Pattern.compile("\\d+");
    Matcher matcher;
    int total = 0;
    int attempts = 0;
    while (attempts < 10) {
      List<WebElement> newFlightDurations = getDriver().findElements(newFlightDurationsSelector);
      try {
        for (WebElement duration : newFlightDurations) {
          matcher = pattern.matcher(duration.getText());
          int current;
          matcher.find();
          int hour = Integer.parseInt(matcher.group());
          current = 60 * hour;
          matcher.find();
          int minute = Integer.parseInt(matcher.group());
          current += minute;
          if (current >= total) {
            total = current;
          } else {
            check = false;
            break;
          }
        }
        break;
      } catch (StaleElementReferenceException e) {
        attempts++;
        if (attempts == 10) {
          check = false;
        }
      }
    }
    return check;
  }

  @Override
  public TravelocityReturnFlightPage selectFirstResult() {
    TravelocityReturnFlightPage page;
    List<WebElement> fare = getDriver().findElements(fareLocator);
    boolean empty = fare.isEmpty();
    retryingFindClick(firstButtonSelector);
    if (empty) {
      page = new TravelocityReturnFlightPage(getDriver(), getWait(), firstButtonSelector);
    } else {
      retryingFindClick(selectFare);
      page = new TravelocityReturnFlightPage(getDriver(), getWait(), selectFare);
    }
    PageFactory.initElements(getDriver(), page);
    return page;
  }

  @Override
  public String[] selectedDepartureAndArrivalTimes() {
    WebDriverWait newWait1 = new WebDriverWait(getDriver(), 120);
    newWait1.until(ExpectedConditions.visibilityOf(selectedDepartureTime));
    WebDriverWait newWait2 = new WebDriverWait(getDriver(), 120);
    newWait2.until(ExpectedConditions.visibilityOf(selectedArrivalTime));
    return new String[]{selectedDepartureTime.getText(), selectedArrivalTime.getText()};
  }

  @Override
  public String hotelTextTag() {
    return null;
  }

  @Override
  public String roomTextTag() {
    return null;
  }

  @Override
  public String flightTextTag() {
    return null;
  }

  @Override
  public String titleTextTag() {
    return null;
  }
}
