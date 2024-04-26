package com.curahealthcare.tests;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LogoutTest {
	WebDriver driver;

	@Before
	public void setUp() throws Exception {
		// Driver setup and loading the website
//		System.setProperty("webdriver.chrome.driver", "E:\\Automation Projects\\Chromedriver 123\\chromedriver-win64\\chromedriver.exe");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://katalon-demo-cura.herokuapp.com/");
		driver.manage().window().maximize();

		// Explicit wait for website loading
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement make_appointment_btn = driver.findElement(By.id("btn-make-appointment"));
		wait.until(ExpectedConditions.visibilityOf(make_appointment_btn));
		
		// Navigating to Login screen
		driver.findElement(By.id("menu-toggle")).click();
		driver.findElement(By.linkText("Login")).click();
		
		// Login to the site
		driver.findElement(By.id("txt-username")).sendKeys("John Doe");
		driver.findElement(By.id("txt-password")).sendKeys("ThisIsNotAPassword");
		driver.findElement(By.id("btn-login")).click();
	}

	@After
	public void tearDown() throws Exception {
		if (driver!=null) {
			driver.quit();
		}
	}

	@Test
	public void validateRedirectionToLoginPage() {
		driver.findElement(By.id("menu-toggle")).click();
		driver.findElement(By.linkText("Logout")).click();
		
		driver.findElement(By.id("menu-toggle")).click();
		driver.findElement(By.linkText("Login")).click();
		
		List<WebElement> loginBtn = driver.findElements(By.id("btn-login"));
		
		Assert.assertTrue(loginBtn.size()>0);
	}
	
	@Test()
	public void validateRestrictedPageAccessAfterLogout() {
		String aptHistoryPageUrl = "https://katalon-demo-cura.herokuapp.com/history.php#history";
		driver.navigate().to(aptHistoryPageUrl);
		
		driver.findElement(By.id("menu-toggle")).click();
		driver.findElement(By.linkText("Logout")).click();
		
		driver.navigate().back();
		
		String currentUrl = driver.getCurrentUrl();
		Assert.assertNotEquals(aptHistoryPageUrl, currentUrl);
	}
	
	@Test
	public void validateSessionTerminationAfterLogout() {
		Set<Cookie> cookieAfterLogin = driver.manage().getCookies();
		
		driver.findElement(By.id("menu-toggle")).click();
		driver.findElement(By.linkText("Logout")).click();
		
		Set<Cookie> cookieAfterLogout = driver.manage().getCookies();
		
//		Assert.assertTrue("Cookies got deleted after logout", cookieAfterLogin.size() > cookieAfterLogout.size());
		
		Assert.assertFalse("Cookies are same after Login and Logout", cookieAfterLogout.containsAll(cookieAfterLogin));
	}

}
