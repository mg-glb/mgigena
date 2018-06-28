package com.globant.tests;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.globant.pages.TravelocityCheckoutPage;
import com.globant.pages.landing.TravelocityFlightsPage;
import com.globant.pages.landing.TravelocityLandingPage;
import com.globant.pages.landing.TravelocityMainPage;
import com.globant.pages.TravelocityReturnFlightPage;
import com.globant.pages.TravelocityWhoIsTravellingPage;
import com.globant.pages.results.TravelocityResultsPage;
import io.github.bonigarcia.wdm.ChromeDriverManager;

/**
 * This class implements the tests outlined in the first exercise of the test. Every assertion will
 * have a corresponding test.
 *
 * @author Maximiliano Gigena
 */
public class ExerciseOneTest {

  //We encapsulate the driver and the wait inside a thread local instance, to impede driver resource
  //sharing between different tests.
  private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
  private final ThreadLocal<FluentWait<WebDriver>> wait = new ThreadLocal<>();

  @BeforeMethod
  public void setup() {
    ChromeDriverManager.getInstance().setup();
    driver.set(new ChromeDriver());
    driver.get().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    wait.set(new FluentWait<>(driver.get())
        .withTimeout(120, TimeUnit.SECONDS)
        .pollingEvery(1, TimeUnit.SECONDS)
        .ignoring(NoSuchElementException.class)
    );
  }

  private TravelocityLandingPage goToTravelocity() {
    TravelocityLandingPage page = null;
    try {
      page = new TravelocityMainPage(driver.get(), wait.get());
    } catch (StaleElementReferenceException e) {
      page = new TravelocityFlightsPage(driver.get(),wait.get());
    }
    PageFactory.initElements(driver.get(), page);
    return page;
  }

  /**
   * This method will implement the assertion defined at this statement: "There is a box that allow
   * you to order by Price, Departure, Arrival and Duration."
   *
   * @author Maximiliano Gigena
   */
  @Test
  public void testSortResults() {
    TravelocityLandingPage mainPage = goToTravelocity();
    mainPage.flights();
    mainPage.flyFrom("LAS");
    mainPage.flyTo("LAX");
    mainPage.pickDepartureDate();
    TravelocityResultsPage results = mainPage.search();

    Assert.assertTrue(results.checkSort(), "Sort function in results is broken!");
  }

  /**
   * This method will implement the assertion defined at this statement: "The select button is
   * present on every result."
   *
   * @author Maximiliano Gigena
   */
  @Test
  public void testSelectButtonPresent() {
    TravelocityLandingPage mainPage = goToTravelocity();
    mainPage.flights();
    mainPage.flyFrom("LAS");
    mainPage.flyTo("LAX");
    mainPage.pickDepartureDate();
    TravelocityResultsPage results = mainPage.search();

    Assert.assertTrue(results.checkSelectButtonForAll(), "The sort button is not for all results!");
  }

  /**
   * This method will implement the assertion defined at this statement: "Flight duration is present
   * on every result."
   *
   * @author Maximiliano Gigena
   */
  @Test
  public void testFlightDurationPresent() {
    TravelocityLandingPage mainPage = goToTravelocity();
    mainPage.flights();
    mainPage.flyFrom("LAS");
    mainPage.flyTo("LAX");
    mainPage.pickDepartureDate();
    TravelocityResultsPage results = mainPage.search();

    Assert.assertTrue(results.checkFlightDurationForAll(), "Flight duration not for all results!");
  }

  /**
   * This method will implement the assertion defined at this statement: "The flight detail and
   * baggage fees is present on every result"
   *
   * @author Maximiliano Gigena
   */
  @Test
  public void testDetailsLinkPresent() {
    TravelocityLandingPage mainPage = goToTravelocity();
    mainPage.flights();
    mainPage.flyFrom("LAS");
    mainPage.flyTo("LAX");
    mainPage.pickDepartureDate();
    TravelocityResultsPage results = mainPage.search();

    Assert.assertTrue(results.checkDetailsForAll(), "Flight details not for all results!");
  }

  /**
   * This method will implement the assertion defined at this statement: "Sort by duration >
   * shorter. Verify the list was correctly sorted."
   *
   * @author Maximiliano Gigena
   */
  @Test
  public void testSortByDurationShorter() {
    TravelocityLandingPage mainPage = goToTravelocity();
    mainPage.flights();
    mainPage.flyFrom("LAS");
    mainPage.flyTo("LAX");
    mainPage.pickDepartureDate();
    TravelocityResultsPage results = mainPage.search();
    results.sortByDistanceShortest();

    Assert.assertTrue(results.checkSortedByDistance(26), "Results not sorted correctly!");
  }

  /**
   * This method will implement the assertion defined at this statement: "Trip total price is
   * present"
   *
   * @author Maximiliano Gigena
   */
  @Test
  public void testTripTotalIsPresent() {
    TravelocityLandingPage mainPage = goToTravelocity();
    mainPage.flights();
    mainPage.flyFrom("LAS");
    mainPage.flyTo("LAX");
    mainPage.pickDepartureDate();
    TravelocityResultsPage results = mainPage.search();
    results.sortByDistanceShortest();
    TravelocityReturnFlightPage returns = results.selectFirstResult();
    TravelocityCheckoutPage checkout = returns.selectFirstResult();

    Assert.assertTrue(checkout.isTotalPricePresent(), "Total price not present!");
  }

  /**
   * This method will implement the assertion defined at this statement: "Departure and return
   * information is present"
   *
   * @author Maximiliano Gigena
   */
  @Test
  public void testDepartureAndReturn() {
    TravelocityLandingPage mainPage = goToTravelocity();
    mainPage.flights();
    mainPage.flyFrom("LAS");
    mainPage.flyTo("LAX");
    mainPage.pickDepartureDate();
    TravelocityResultsPage results = mainPage.search();
    results.sortByDistanceShortest();
    String[] departureInfo = results.selectedDepartureAndArrivalTimes();
    TravelocityReturnFlightPage returns = results.selectFirstResult();
    String[] returnInfo = returns.selectedDepartureAndArrivalTimes();
    TravelocityCheckoutPage checkout = returns.selectFirstResult();

    Assert.assertTrue(checkout.areDepartureAndReturnInfoDisplayed(departureInfo, returnInfo),
        "Departure and return info have errors!");
  }

  /**
   * This method will implement the assertion defined at this statement: "Price guarantee text is
   * present"
   *
   * @author Maximiliano Gigena
   */
  @Test
  public void testPriceGuaranteePresent() {
    TravelocityLandingPage mainPage = goToTravelocity();
    mainPage.flights();
    mainPage.flyFrom("LAS");
    mainPage.flyTo("LAX");
    mainPage.pickDepartureDate();
    TravelocityResultsPage results = mainPage.search();
    results.sortByDistanceShortest();
    TravelocityReturnFlightPage returns = results.selectFirstResult();
    TravelocityCheckoutPage checkout = returns.selectFirstResult();

    Assert.assertEquals(checkout.priceGuaranteeText(), "Price Guarantee",
        "Price guarantee not present!");
  }

  @Test
  public void testWhoIsTravelling() {
    TravelocityLandingPage mainPage = goToTravelocity();
    mainPage.flights();
    mainPage.flyFrom("LAS");
    mainPage.flyTo("LAX");
    mainPage.pickDepartureDate();
    TravelocityResultsPage results = mainPage.search();
    results.sortByDistanceShortest();
    TravelocityReturnFlightPage returns = results.selectFirstResult();
    TravelocityCheckoutPage checkout = returns.selectFirstResult();
    TravelocityWhoIsTravellingPage whoIs = checkout.clickContinueBooking();

    Assert.assertEquals(whoIs.whoIsText(), "Who's traveling?", "Who is text is not present");
    //Since this text changes randomly, I had to change this assert.
    try {
      Assert.assertEquals(whoIs.paybackText(),
          "Get money back if you find a lower price on Travelocity", "Payback text not present!");
    } catch (AssertionError e) {
      boolean alternative = "Want money back if you find a lower price?".equals(whoIs.paybackText());
      alternative |= "Get money back if you find a price drop for your flight on Travelocity".equals(whoIs.paybackText());
      if(!alternative) {
        throw e;
      }
    }
    Assert.assertEquals(whoIs.paymentText(), "How would you like to pay?",
        "Payment text not present!");
    Assert.assertEquals(whoIs.completeText(), "Review and book your trip",
        "Review Text is not present!");
    Assert.assertTrue(whoIs.isCompleteDisplayed(), "Complete checkout button not present!");
  }

  @AfterMethod
  public void tearDown() {
    if (driver.get() != null) {
      driver.get().quit();
    }
  }
}
