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

import com.globant.pages.landing.TravelocityFlightsPage;
import com.globant.pages.landing.TravelocityLandingPage;
import com.globant.pages.landing.TravelocityMainPage;
import com.globant.pages.results.TravelocityResultsPage;
import io.github.bonigarcia.wdm.ChromeDriverManager;

/**
 * This class implements the tests outlined in the second exercise of the test. Every assertion will
 * have a corresponding test.
 *
 * @author Maximiliano Gigena
 */
public class ExerciseTwoTest {

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
    TravelocityLandingPage page;
    try {
      page = new TravelocityMainPage(driver.get(), wait.get());
    } catch (StaleElementReferenceException e) {
      page = new TravelocityFlightsPage(driver.get(), wait.get());
    }
    PageFactory.initElements(driver.get(), page);
    return page;
  }

  /**
   * This method will implement the assertion defined at this statement: "Verify results page by
   * choosing at least 5 validations to be performed".
   *
   * @author Maximiliano Gigena
   */
  @Test
  public void testResultsValidations() {
    TravelocityLandingPage mainPage = goToTravelocity();
    TravelocityLandingPage flightsAndHotelsPage = mainPage.flightsAndHotels();
    flightsAndHotelsPage.flyFrom("LAS");
    flightsAndHotelsPage.flyTo("LAX");
    flightsAndHotelsPage.pickDepartureDate();
    flightsAndHotelsPage.pickReturnDate(13);
    TravelocityResultsPage results = flightsAndHotelsPage.search();

    Assert.assertEquals(results.hotelTextTag(), "Hotel", "Hotel text tag not present!");
    Assert.assertEquals(results.roomTextTag(), "Room", "Room text tag not present!");
    Assert.assertEquals(results.flightTextTag(), "Flights", "Flights text tag not present!");
    Assert.assertEquals(results.titleTextTag(), "Start by choosing your hotel",
        "Title text tag not present!");

  }

  @AfterMethod
  public void tearDown() {
    if (driver.get() != null) {
      driver.get().quit();
    }
  }
}
