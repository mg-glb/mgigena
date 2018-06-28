package com.globant.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.FluentWait;

public class TravelocityWhoIsTravellingPage extends BasePage {

  //Stable locators
  @FindBy(css = "#assurance>div>h2")
  private WebElement payBack;
  @FindBy(css = "#payments>fieldset>h2")
  private WebElement paymentMethodTitle;
  @FindBy(css = "#complete>h2")
  private WebElement completeTitle;
  @FindBy(id = "complete-booking")
  private WebElement completeButton;

  //Stale locators
  private final By whoIsTravelling = By.cssSelector("#preferences>form>fieldset>h2");

  public TravelocityWhoIsTravellingPage(WebDriver driver, FluentWait<WebDriver> wait) {
    super(driver, wait);
    wait.until(webDriver -> isRetryingDisplayed(whoIsTravelling));
  }

  public String whoIsText() {
    return retryingGetText(whoIsTravelling);
  }

  public String paybackText() {
    return payBack.getText();
  }

  public String paymentText() {
    return paymentMethodTitle.getText();
  }

  public String completeText() {
    return completeTitle.getText();
  }

  public boolean isCompleteDisplayed() {
    getWait().until(webDriver -> completeButton.isDisplayed());
    return completeButton.isEnabled();
  }
}
