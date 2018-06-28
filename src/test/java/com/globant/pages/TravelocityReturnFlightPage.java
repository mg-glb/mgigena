package com.globant.pages;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TravelocityReturnFlightPage extends BasePage {

  //Stable locators
  @FindBy(css = "#flightModuleList>li>div>div.uitk-grid.all-grid-fallback-alt>div.uitk-col.all-col-shrink>div>div.uitk-col.standard-col-l-margin.all-col-shrink.display-larger-screens-only>button")
  private List<WebElement> selectButtons;
  @FindBy(css = "#flightModuleList>li:first-child>div.grid-container.standard-padding>div.uitk-grid.all-grid-fallback-alt>div.uitk-col.all-col-fill.custom-short-r-margin>div>div>div>div.uitk-col.tablet-col-1-3.desktop-col-1-3.custom-col-r-margin.min-width-large-screens-only>div.primary-content.no-wrap.custom-primary-padding>span>span:first-child")
  private WebElement selectedDepartureTime;
  @FindBy(css = "#flightModuleList>li:first-child>div.grid-container.standard-padding>div.uitk-grid.all-grid-fallback-alt>div.uitk-col.all-col-fill.custom-short-r-margin>div>div>div>div.uitk-col.tablet-col-1-3.desktop-col-1-3.custom-col-r-margin.min-width-large-screens-only>div.primary-content.no-wrap.custom-primary-padding>span>span:nth-child(2)")
  private WebElement selectedArrivalTime;

  //Stale locators
  private final By returnLocator = By.id("outboundFlightModule");
  private final By fareSelector = By
      .cssSelector("#flightModuleList>li:first-child>div.basic-economy-tray.uitk-grid>button");
  private final By firstButtonSelector = By.cssSelector(
      "#flightModuleList>li:first-child>div>div.uitk-grid.all-grid-fallback-alt>div.uitk-col.all-col-shrink>div>div.uitk-col.standard-col-l-margin.all-col-shrink.display-larger-screens-only>button");
  private final By selectFare = By.cssSelector(
      "#flightModuleList>li:first-child>div.basic-economy-tray.uitk-grid>div>div>div.basic-economy-footer.uitk-grid.all-grid-nowrap>button");

  public TravelocityReturnFlightPage(WebDriver driver, FluentWait<WebDriver> wait,
      By startLocator) {
    super(driver, wait);
    retryingOpenPageWithLocator(startLocator, returnLocator);
  }

  public TravelocityCheckoutPage selectFirstResult() {
    Set<String> currentHandles = getDriver().getWindowHandles();
    TravelocityCheckoutPage page;
    List<WebElement> fare = getDriver().findElements(fareSelector);
    boolean empty = fare.isEmpty();
    retryingFindClick(firstButtonSelector);
    if (empty) {
      retryingOpenWindows(firstButtonSelector, currentHandles.size());
    } else {
      retryingFindClick(selectFare);
      retryingOpenWindows(selectFare, currentHandles.size());
    }
    //Go to the new window.
    Set<String> newHandles = getDriver().getWindowHandles();
    for (String handle : newHandles) {
      if (!currentHandles.contains(handle)) {
        getDriver().switchTo().window(handle);
        break;
      }
    }
    page = new TravelocityCheckoutPage(getDriver(), getWait());
    PageFactory.initElements(getDriver(), page);
    return page;
  }

  public String[] selectedDepartureAndArrivalTimes() {
    WebDriverWait newWait1 = new WebDriverWait(getDriver(), 120);
    newWait1.until(ExpectedConditions.visibilityOf(selectedDepartureTime));
    WebDriverWait newWait2 = new WebDriverWait(getDriver(), 120);
    newWait2.until(ExpectedConditions.visibilityOf(selectedArrivalTime));
    return new String[]{selectedDepartureTime.getText(), selectedArrivalTime.getText()};
  }
}
