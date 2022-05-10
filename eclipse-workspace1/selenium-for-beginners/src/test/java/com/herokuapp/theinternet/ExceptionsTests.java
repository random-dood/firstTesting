package com.herokuapp.theinternet;

import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ExceptionsTests {
	private WebDriver driver;
	
	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(@Optional("chrome") String browser) {
		
		
//		Create driver
		switch (browser) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			 driver = new ChromeDriver();
			break;

		case "firefox":
			System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
			 driver = new FirefoxDriver();
			break;
			
			
		default:
			System.out.println("What are you doing? This is clearly not Firefox or Chrome. This is " + browser + 
					". This is not in the test cases. I'll just show you a chrome one instead by default.");
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			 driver = new ChromeDriver();
			break;
		}
		
		

		// sleep for 3 seconds
		sleep(3000);

		// maximize browser window
		driver.manage().window().maximize();
		
		//implicit wait
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Test (priority = 1)
	public void notVisibleTest() {
		
		//First open the page https://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button"));
		startButton.click();
		
		//Get finish element text
		WebElement finishElement = driver.findElement(By.id("finish"));
		
		
		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(finishElement));
		//After hitting the start button, wait 10 seconds for the Hello World string to be revealed
		//Until the getText function gets actual text from the find element by the id finish as designated by the expected conditions
		//Otherwise the getText will grab no text and thus the assertTrue will fail since it has nothing to compare it with
		
		String finishText = finishElement.getText(); //This gets the VISIBLE text, not the hidden
		
		
		//Compare actual finish element with expected text "Hello World!" using Test NG Assert class
		Assert.assertTrue(finishText.contains("Hello World!"), "Finish text: " + finishText);
		
		//Note that the start button becomes hidden after Hello World text is revealed
		
	}
	
	@Test (priority = 2)
	public void timeOutTest() {
		
		//First open the page https://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button"));
		startButton.click();
		
		//Get finish element text
		WebElement finishElement = driver.findElement(By.id("finish"));
		
		
		try {
			new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.visibilityOf(finishElement));
		} catch (TimeoutException exception) {
			System.out.println("Exception caught: " + exception.getMessage());
			sleep(3000);
		}
		//After hitting the start button, wait 10 seconds for the Hello World string to be revealed
		//Until the getText function gets actual text from the find element by the id finish as designated by the expected conditions
		//Otherwise the getText will grab no text and thus the assertTrue will fail since it has nothing to compare it with
		
		String finishText = finishElement.getText(); //This gets the VISIBLE text, not the hidden
		
		
		//Compare actual finish element with expected text "Hello World!" using Test NG Assert class
		Assert.assertTrue(finishText.contains("Hello World!"), "Finish text: " + finishText);
		
		//Note that the start button becomes hidden after Hello World text is revealed
		
	}
	
	@Test (priority = 3)
	public void noSuchElementTest() {
		
		//First open the page https://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("https://the-internet.herokuapp.com/dynamic_loading/2");
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button"));
		startButton.click();
		
//		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("finish")));
		//Alternative for getting the element it needs to wait 10 seconds for.
		//Note that it has to be put before the WebElement finishElement
		
		//Get finish element text
		WebElement finishElement = driver.findElement(By.id("finish"));
		
		
		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(finishElement));
		//After hitting the start button, wait 10 seconds for the Hello World string to be revealed
		//Until the getText function gets actual text from the find element by the id finish as designated by the expected conditions
		//Otherwise the getText will grab no text and thus the assertTrue will fail since it has nothing to compare it with
		
		String finishText = finishElement.getText(); //This gets the VISIBLE text, not the hidden
		
		
		//Compare actual finish element with expected text "Hello World!" using Test NG Assert class
//		Assert.assertTrue(finishText.contains("Hello World!"), "Finish text: " + finishText);
		//Another version of assertTrue with expected conditions
		Assert.assertTrue(new WebDriverWait(driver, Duration.ofSeconds(0)).until(ExpectedConditions.textToBePresentInElementLocated(By.id("finish"), "Hello World!")), 
				"Could not verify expected text Hello World!");
		// Tested it by changing the Hello World! string to something else and it indeed failed. So good working test code
		
		//Note that the start button becomes hidden after Hello World text is revealed
		
	}

	@Test
	public void staleElementTest() {
		
		driver.get("http://the-internet.herokuapp.com/dynamic_controls");
		
		WebElement checkbox = driver.findElement(By.id("checkbox"));
		WebElement removeButton = driver.findElement(By.xpath("//button[contains(text(),'Remove')]"));
		
		removeButton.click();
		
//		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.invisibilityOf(checkbox));
		
//		Assert.assertFalse(checkbox.isDisplayed());
		
//		Assert.assertTrue(new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.invisibilityOf(checkbox)), 
//				"Checkbox still visible but it should not be");
     // Alternative
		Assert.assertTrue(new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.stalenessOf(checkbox)), 
				"Checkbox still visible but it should not be");
		
		WebElement addButton = driver.findElement(By.xpath("//button[contains(text(),'Add')]"));
		addButton.click();
		
//	    new WebDriverWait(driver, Duration.ofSeconds(0)).until(ExpectedConditions.visibilityOf(checkbox));
	   //Couldn't figure out how to add assertTrue to above, kept giving me a synthax error for the dot operator of visibility combined with assertTrue
		
		checkbox = new WebDriverWait(driver, Duration.ofSeconds(0)).until(ExpectedConditions.visibilityOfElementLocated(By.id("checkbox")));
		Assert.assertTrue(checkbox.isDisplayed(), "Checkbox is not visible it should not be");
		
		//Finally done with section 6!
	    
	}
	
	@Test
	public void disabledElementTest() {
		driver.get("http://the-internet.herokuapp.com/dynamic_controls");

		WebElement enableButton = driver.findElement(By.xpath("//button[contains(text(),'Enable')]"));
		WebElement textField = driver.findElement(By.xpath("(//input)[2]"));
		
		enableButton.click();

		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(textField));
		
		textField.sendKeys("My name is Rolando");
		Assert.assertEquals(textField.getAttribute("value"), "My name is Rolando");
	}

	private void sleep(long m) {
		try {
			Thread.sleep(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		// Close browser
		driver.quit();
	}

}