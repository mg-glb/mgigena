package com.globant.pages;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;

public class TravelocityCheckoutPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(TravelocityCheckoutPage.class);

  @FindBy(css = "body>main>section.tripSummaryContainer.uitk-col>div>div.totalContainer>div>div.tripTotals>span.packagePriceTotal")
  private WebElement price;
  @FindBy(css = "body>main>section.flightSummaryContainer.uitk-col>div.uitk-grid.all-x-gutter-12.flex-listing.flightSummary.desktop-x-gutter-0.tablet-x-gutter-0.smalltablet-x-gutter-0.mobile-x-gutter-0>div.flex-card.flex-tile.details.OD0>div>div>div.flightSummary-timeDuration.cf>div.departure>span")
  private WebElement departureFlightDepartureTime;
  @FindBy(css = "body>main>section.flightSummaryContainer.uitk-col>div.uitk-grid.all-x-gutter-12.flex-listing.flightSummary.desktop-x-gutter-0.tablet-x-gutter-0.smalltablet-x-gutter-0.mobile-x-gutter-0>div.flex-card.flex-tile.details.OD0>div>div>div.flightSummary-timeDuration.cf>div.arrival>span")
  private WebElement departureFlightArrivalTime;
  @FindBy(css = "body>main>section.flightSummaryContainer.uitk-col>div.uitk-grid.all-x-gutter-12.flex-listing.flightSummary.desktop-x-gutter-0.tablet-x-gutter-0.smalltablet-x-gutter-0.mobile-x-gutter-0>div.flex-card.flex-tile.details.OD1>div>div>div.flightSummary-timeDuration.cf>div.departure>span")
  private WebElement returnFlightDepartureTime;
  @FindBy(css = "body>main>section.flightSummaryContainer.uitk-col>div.uitk-grid.all-x-gutter-12.flex-listing.flightSummary.desktop-x-gutter-0.tablet-x-gutter-0.smalltablet-x-gutter-0.mobile-x-gutter-0>div.flex-card.flex-tile.details.OD1>div>div>div.flightSummary-timeDuration.cf>div.arrival>span")
  private WebElement returnFlightArrivalTime;

  //Stale locators
  private final By reviewYourTrip = By.cssSelector("#fisHeader>h1");
  private final By priceGuarantee = By.cssSelector(
      "body>main>section.tripSummaryContainer.uitk-col>div>div.totalContainer>div>div.priceGuarantee");
  private final By bookButton = By.id("bookButton");
  private final By iFramesLocator = By.tagName("iframe");
  private final By supLocator = By.tagName("sup");


  public TravelocityCheckoutPage(WebDriver driver, FluentWait<WebDriver> wait) {
    super(driver, wait);
    isRetryingDisplayed(reviewYourTrip);
  }

  /**
   * This method checks that the final price is correctly formatted.
   *
   * @author Maximiliano Gigena
   */
  public boolean isTotalPricePresent() {
    boolean check = true;
    check &= price.isDisplayed();
    WebElement cents = price.findElement(supLocator);
    check &= cents.isDisplayed();
    String priceText = price.getText();
    check &= priceText.matches("\\$\\d+\\.\\d+");
    String centsText = cents.getText();
    check &= centsText.matches("\\.\\d+");
    return check;
  }

  /**
   * With data brought from the flight selection pages, this method checks that the data contained
   * in the checkout page times coincide with what is brought in the input data.
   *
   * @author Maximiliano Gigena
   */
  public boolean areDepartureAndReturnInfoDisplayed(String[] departureInfo,
      String[] returnInfo) {
    boolean check = true;
    check &= departureFlightDepartureTime.getText().equals(departureInfo[0]);
    check &= departureFlightArrivalTime.getText().equals(departureInfo[1]);
    check &= returnFlightDepartureTime.getText().equals(returnInfo[0]);
    check &= returnFlightArrivalTime.getText().equals(returnInfo[1]);
    return check;
  }

  /**
   * This method returns the text of the priceGuaranteeLocator.
   *
   * @author Maximiliano Gigena
   */
  public String priceGuaranteeText() {
    if (!isRetryingDisplayed(priceGuarantee)) {
      throw new ElementNotVisibleException("The price guaranteee could not be found!");
    }
    return retryingGetText(priceGuarantee);
  }

  /**
   * This method will click the book flight button, and will take the test to the who is travelling
   * page.
   *
   * @author Maximiliano Gigena
   */
  public TravelocityWhoIsTravellingPage clickContinueBooking() {
    try {
      retryingFindClick(bookButton);
    } catch (WebDriverException e) {
      closeIframeAndClickBookAgain();
    }
    TravelocityWhoIsTravellingPage page = new TravelocityWhoIsTravellingPage(getDriver(),
        getWait());
    PageFactory.initElements(getDriver(), page);
    return page;
  }

  /**
   * This method is called when the driver fails at clicking the book flight button. This method
   * assumes that the failure is due to the presence of an iframe on top of the button. To close the
   * iframe, the driver needs to click one button. This button, has a locator of the
   * type="#yie-close-button-{string}". The target iframe has a locator of the
   * type="yie-iframe-{string}". So, the method extracts {string} and parses the locator for the
   * button.
   *
   * @author Maximiliano Gigena
   */
  private void closeIframeAndClickBookAgain() {
    int attempts = 0;
    while (attempts < 10) {
      try {
        List<WebElement> iframes = getDriver().findElements(iFramesLocator);
        for (WebElement iframe : iframes) {
          String iframeName = iframe.getAttribute("name");
          if (iframeName.contains("yie-iframe-")) {
            String name = iframeName.replace("yie-iframe-", "");
            try {
              WebElement closeButton = getDriver()
                  .findElement(By.cssSelector("#yie-close-button-" + name));
              closeButton.click();
              retryingFindClick(bookButton);
              break;
            } catch (NoSuchElementException e1) {
              LOGGER.debug("Element not found. Going for next try.");
            }
          }
        }
        break;
      } catch (StaleElementReferenceException e1) {
        attempts++;
        if (attempts == 10) {
          throw e1;
        }
      }
    }
  }
}
