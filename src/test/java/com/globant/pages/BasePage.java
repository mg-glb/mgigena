package com.globant.pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

  private static final Logger LOGGER = LogManager.getLogger(BasePage.class);
  private final WebDriver driver;
  private final FluentWait<WebDriver> wait;

  public BasePage(WebDriver driver, FluentWait<WebDriver> wait) {
    this.driver = driver;
    this.wait = wait;
    WebDriverWait newWait = new WebDriverWait(driver, 120);
    newWait.until(webDriver -> "complete".equals(((JavascriptExecutor) webDriver)
        .executeScript("return document.readyState")));
  }

  protected WebDriver getDriver() {
    return driver;
  }

  protected FluentWait<WebDriver> getWait() {
    return wait;
  }

  protected WebElement retryingGetWebElement(By by) {
    int attempts = 0;
    WebElement element = null;
    while (attempts < 100) {
      try {
        element = driver.findElement(by);
        break;
      } catch (StaleElementReferenceException | NoSuchElementException | ElementNotVisibleException | TimeoutException e) {
        String errorText = String.format("Attempt %d to get locator %s", attempts, by.toString());
        LOGGER.debug(errorText);
      }
      attempts++;
      if (attempts == 10) {
        throw new StaleElementReferenceException("Locator could not be found: " + by);
      }
    }
    return element;
  }

  /**
   * This method is a modified version of the method defined at: http://darrellgrainger.blogspot.com/2012/06/staleelementexception.html
   *
   * @author Maximiliano Gigena
   */
  protected void retryingFindClick(By by) {
    int attempts = 0;
    while (attempts < 10) {
      try {
        isRetryingDisplayed(by);
        driver.findElement(by).click();
        break;
      } catch (StaleElementReferenceException | NoSuchElementException | ElementNotVisibleException | TimeoutException e) {
        String errorText = String.format("Attempt %d to click locator %s", attempts, by.toString());
        LOGGER.debug(errorText);
      }
      attempts++;
      if (attempts == 10) {
        throw new StaleElementReferenceException("Locator could not be found: " + by);
      }
    }
  }

  protected final boolean isRetryingDisplayed(By by) {
    int attempts = 0;
    boolean check = false;
    while (attempts < 10) {
      try {
        check = driver.findElement(by).isDisplayed();
        break;
      } catch (StaleElementReferenceException | NoSuchElementException | ElementNotVisibleException e) {
        String errorText = String.format("Attempt %d to find locator %s", attempts, by.toString());
        LOGGER.debug(errorText);
      }
      attempts++;
      if (attempts == 10) {
        throw new StaleElementReferenceException("Locator could not be found: " + by);
      }
    }
    return check;
  }

  protected String retryingGetText(By by) {
    int attempt = 0;
    String text = "";
    while (attempt < 10) {
      try {
        text = driver.findElement(by).getText();
        break;
      } catch (StaleElementReferenceException | NoSuchElementException | ElementNotVisibleException e) {
        String er = String.format("Attempt %d to get text from locator %s", attempt, by.toString());
        LOGGER.debug(er);
      }
      attempt++;
    }
    return text;
  }

  protected void retryingOpenWindows(By attemptedSelector, int size) {
    int attempts = 0;
    while (attempts < 10) {
      try {
        wait.withTimeout(Duration.ofSeconds(10));
        wait.until(webDriver -> webDriver.getWindowHandles().size() == size + 1);
        break;
      } catch (TimeoutException e) {
        String errorText = String.format("Attempt %d to make window appear", attempts);
        LOGGER.debug(errorText);
        retryingFindClick(attemptedSelector);
      }
      attempts++;
      if (attempts == 10) {
        throw new StaleElementReferenceException("Window could not be opened");
      }
    }
    wait.withTimeout(Duration.ofSeconds(120));
  }

  protected final void retryingOpenPageWithLocator(By startLocator, By returnLocator) {
    int attempts = 0;
    while (attempts < 10) {
      try {
        wait.withTimeout(Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(returnLocator)));
        break;
      } catch (TimeoutException | NoSuchElementException e) {
        String errorText = String.format("Attempt %d to make window appear", attempts);
        LOGGER.debug(errorText);
        retryingFindClick(startLocator);
      }
      attempts++;
      if (attempts == 10) {
        throw new StaleElementReferenceException("Locator could not be found");
      }
    }
    wait.withTimeout(Duration.ofSeconds(120));
  }

  protected void retryingSelect(By selectLocator, String option) {
    int attempts = 0;
    while (attempts < 10) {
      try {
        new Select(driver.findElement(selectLocator)).selectByVisibleText(option);
        break;
      } catch (NoSuchElementException e) {
        attempts++;
        if (attempts == 10) {
          throw e;
        }
      }
    }
  }

  protected void retryingSendKeys(By locator, String string) {
    int attempts = 0;
    while (attempts < 10) {
      try {
        driver.findElement(locator).sendKeys(string);
        break;
      } catch (NoSuchElementException | StaleElementReferenceException e) {
        attempts++;
        if (attempts == 10) {
          throw e;
        }
      }
    }
  }
}
