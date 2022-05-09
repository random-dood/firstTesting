package com.herokuapp.theinternet;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
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

public class LoginTests {
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

	@Test (priority = 1, groups = { "positiveTests", "smokeTests" })
	public void positiveLoginTest() {
		System.out.println("Starting loginTest");
		


//		open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);
		System.out.println("Page is opened.");
		
//		enter username
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("tomsmith");
		
//		enter password
		WebElement password = driver.findElement(By.name("password"));
		password.sendKeys("SuperSecretPassword!");
		
		new WebDriverWait(driver, Duration.ofSeconds(10));
		
//		click login button
		WebElement logInButton = driver.findElement(By.tagName("button"));
		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(logInButton));
//		Don't ask me how I got the one above LOL ^ this code wasn't in the video
		logInButton.click();
		
		sleep(3000);

//		verifications:
//		 new url
		String expectedUrl = "http://the-internet.herokuapp.com/secure";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "This is not actually the web page that it was asserted it be");
		
//		 logout button is visible
		WebElement logOutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));
		Assert.assertTrue(logOutButton.isDisplayed(), "Logout button is not visible");
		
//		 successful login message
		WebElement successMessage = driver.findElement(By.xpath("//div[@id='flash']"));
		System.out.println("***" + successMessage.getAriaRole());
		
		String expectedMessage = "You logged into a secure area!";
		String actualMessage = successMessage.getText(); 
//		Assert.assertEquals(actualMessage, expectedMessage, "This is not actually the message that it was asserted it be");
		Assert.assertTrue(actualMessage.contains(expectedMessage), 
				"Weird, the actual message is not the same as expected.\nActual Message: " 
		+ actualMessage + "\nExpected Message: " + expectedMessage);
//		WebElement successMessage = driver.findElement(By.cssSelector("#flash"));
//		WebElement successMessage = driver.findElement(By.className("success")); 
//		There can be multiple elements so the xpath one is preferred (the one above)
		
		
		
		
		
	}

	
	
	@Parameters({ "username", "password", "expectedMessage" })

	@Test (priority = 2, groups = { "negativeTests", "smokeTests" })
	public void negativeLoginTest(String username, String password, String expectedErrorMessage) {
		System.out.println("Starting negativeLoginTest with " + username + " and " + password );
		
    		System.out.println("Starting the incorrect Username Test");
    		

//    		open test page
    		String url = "http://the-internet.herokuapp.com/login";
    		driver.get(url);
    		System.out.println("Page is opened.");
    		
//    		enter username
    		WebElement usernameElement = driver.findElement(By.id("username"));
    		usernameElement.sendKeys(username);
    		
//    		enter password
    		WebElement passwordElement = driver.findElement(By.name("password"));
    		passwordElement.sendKeys(password);
    		
//    		click login button
    		WebElement logInButton = driver.findElement(By.tagName("button"));
    		logInButton.click();
    		
    		sleep(3000);
    		
    		//Verifications
    		WebElement errorMessage = driver.findElement(By.id("flash"));
    		
    		String actualErrorMessage = errorMessage.getText();
    		
    		Assert.assertTrue(actualErrorMessage.contains(expectedErrorMessage), 
    				"How unfortunate, the actual message does not match the expected message \nActual Message: " + actualErrorMessage 
    				+ "\nExpected Message: " + expectedErrorMessage);
    		
    		
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