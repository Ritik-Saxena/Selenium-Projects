package com.curahealthcare.tests;

import org.junit.Assert; 

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MakeAppointmentTest {
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
		
		driver.findElement(By.id("menu-toggle")).click();
		driver.findElement(By.linkText("Login")).click();
		
		// Login the website
		driver.findElement(By.id("txt-username")).sendKeys("John Doe");
		driver.findElement(By.id("txt-password")).sendKeys("ThisIsNotAPassword");
		driver.findElement(By.id("btn-login")).click();
	}

	@After
	public void tearDown() throws Exception {
		// if driver exists then close it post test cases execution
		if (driver!=null) driver.quit();
	}
	

	@Test
	public void validAppointmentForToday() throws InterruptedException {
		// Getting today's date in dd/MM/yyyy format
		String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		// selecting facility
		WebElement facilityDropdown = driver.findElement(By.id("combo_facility"));
		Select select = new Select(facilityDropdown);
		select.selectByValue("Tokyo CURA Healthcare Center");
		
		
		// selecting hospital readmission 
		driver.findElement(By.id("chk_hospotal_readmission")).click();
		
		// selecting health program as Medicare
		driver.findElement(By.id("radio_program_medicare")).click();
		
		// Entering today's date in Visit Date
		driver.findElement(By.id("txt_visit_date")).sendKeys(todayDate);
		
		// Giving comment
		driver.findElement(By.id("txt_comment")).sendKeys("Appointment for an emergency");
		
		// Clicking Book Appointment button
		driver.findElement(By.id("btn-book-appointment")).click();	
		
		Thread.sleep(500);

		// Condition to check if test case status is Pass or Fail
		List<WebElement> aptConfirmTxt = driver.findElements(By.xpath("//h2[text()='Appointment Confirmation']"));	
		Assert.assertTrue(aptConfirmTxt.size()>0);
	}
	
	@Test
	public void validAppointmentForNearFuture() throws InterruptedException {
		// Getting today's date and adding 10 days in it in dd/MM/yyyy format
		LocalDate todayDate = LocalDate.now();
		String futureDate = todayDate.plusWeeks(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		
		// making an appointment
		WebElement facilityDropdown = driver.findElement(By.id("combo_facility"));
		Select select = new Select(facilityDropdown);
		select.selectByValue("Hongkong CURA Healthcare Center");
		
		driver.findElement(By.id("radio_program_medicaid")).click();
		
		driver.findElement(By.id("txt_visit_date")).sendKeys(futureDate);
		
		driver.findElement(By.id("txt_comment")).sendKeys("Appointment for chronic");
		
		driver.findElement(By.id("btn-book-appointment")).click();
		
		
		Thread.sleep(500);
		
		// Condition to check if test case status is Pass or Fail
		List<WebElement> aptConfirmTxt = driver.findElements(By.xpath("//h2[text()='Appointment Confirmation']"));		
		Assert.assertTrue(aptConfirmTxt.size()>0);

	}
	
	@Test
	public void validAppointmentForDistantFuture() throws InterruptedException {
		// Getting today's date and adding 10 days in it in dd/MM/yyyy format
		LocalDate todayDate = LocalDate.now();
		String futureDate = todayDate.plusMonths(4).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		
		// making an appointment
		WebElement facilityDropdown = driver.findElement(By.id("combo_facility"));
		Select select = new Select(facilityDropdown);
		select.selectByValue("Hongkong CURA Healthcare Center");

		driver.findElement(By.id("radio_program_medicaid")).click();
		
		driver.findElement(By.id("txt_visit_date")).sendKeys(futureDate);
		
		driver.findElement(By.id("txt_comment")).sendKeys("Appointment for chronic");
		
		driver.findElement(By.id("btn-book-appointment")).click();
		
		
		Thread.sleep(500);
		
		// Condition to check if test case status is Pass or Fail
		List<WebElement> aptConfirmTxt = driver.findElements(By.xpath("//h2[text()='Appointment Confirmation']"));
		Assert.assertFalse(aptConfirmTxt.size()>0);
	}
	
	@Test
	public void invalidAppointmentForPast() throws InterruptedException {
		// Getting today's date in dd/MM/yyyy format
		LocalDate todayDate = LocalDate.now();
		String pastDate = todayDate.minusDays(10).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		// making an appointment
		WebElement facilityDropdown = driver.findElement(By.id("combo_facility"));
		Select select = new Select(facilityDropdown);
		select.selectByValue("Seoul CURA Healthcare Center");
		
		driver.findElement(By.id("chk_hospotal_readmission")).click();
		
		driver.findElement(By.id("radio_program_none")).click();
		
		driver.findElement(By.id("txt_visit_date")).sendKeys(pastDate);
		
		driver.findElement(By.id("txt_comment")).sendKeys("Appointment for an nothing");
		
		driver.findElement(By.id("btn-book-appointment")).click();	
		
		Thread.sleep(500);
		
		// Condition to check if test case status is Pass or Fail by checking if Appointment Confirmation text appears or not
		List<WebElement> aptConfirmTxt = driver.findElements(By.xpath("//h2[text()='Appointment Confirmation']"));
		Assert.assertFalse(aptConfirmTxt.size()>0);
	}

}
