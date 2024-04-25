package com.curahealthcare.tests;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AppointmentHistoryTest {
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
		
		// Login the website
		driver.findElement(By.id("menu-toggle")).click();
		driver.findElement(By.linkText("Login")).click();
		
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
	public void validateAppointmentHistoryPage() throws IOException {
		String facilityName = "Tokyo CURA Healthcare Center";
		String hospitalReadmission = "Yes";
		String healthcareProgram = "Medicare";
		String visitDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); // Getting today's date in dd/MM/yyyy format
		String comment = "Appointment for an emergency";
		
		
		// making an appointment 
		WebElement facilityDropdown = driver.findElement(By.id("combo_facility"));
		Select select = new Select(facilityDropdown);
		select.selectByValue(facilityName);
		
		driver.findElement(By.id("chk_hospotal_readmission")).click();
		
		driver.findElement(By.id("radio_program_medicare")).click();
		
		driver.findElement(By.id("txt_visit_date")).sendKeys(visitDate);
		
		driver.findElement(By.id("txt_comment")).sendKeys(comment);
		
		driver.findElement(By.id("btn-book-appointment")).click();
		
		
		// Navigating to History page
		driver.findElement(By.id("menu-toggle")).click();
		driver.findElement(By.linkText("History")).click();
		
		
		String relativeFilePath = "src/test/resources/screenshots";
		String absoluteFilePath = Paths.get(relativeFilePath).toAbsolutePath().toString();
		File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File(absoluteFilePath + "/aptHistory.png"));
		
		
		// Checking if appointment history has same informations as we have given 
		String facilityNameHistory = driver.findElement(By.id("facility")).getText();
		String hospitalReadmissionHistory = driver.findElement(By.id("hospital_readmission")).getText();
		String healthcareProgamHistory = driver.findElement(By.id("program")).getText();
		String visitDateHistory = driver.findElement(By.className("panel-heading")).getText();
		String commentHistory = driver.findElement(By.id("comment")).getText();
		
		Assert.assertEquals("Facility Name is not same", facilityName, facilityNameHistory);
		Assert.assertEquals("Hospital readmission is not same", hospitalReadmission, hospitalReadmissionHistory);
		Assert.assertEquals("Healthcare Program is not same", healthcareProgram, healthcareProgamHistory);
		Assert.assertEquals("Visit date is not same", visitDate, visitDateHistory);
		Assert.assertEquals("Comment is not same", comment, commentHistory);		
	}

}
