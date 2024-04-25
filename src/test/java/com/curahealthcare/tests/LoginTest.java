package com.curahealthcare.tests;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.nio.file.Paths;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginTest {
	WebDriver driver;

//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}

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
	}
	
	

	@After
	public void tearDown() throws Exception {
		// if driver exists then close it post test cases execution
		if (driver!=null) driver.quit();
	}
	

	@Test
	public void validateLoginCredentialsVariations() throws IOException, InterruptedException {
		
		// getting absolute path of file from relative path
		String relativeFilePath = "src/test/resources/testdata/testdata.xlsx";
		String absoluteFilePath = Paths.get(relativeFilePath).toAbsolutePath().toString();
		
		// Opening the file in input stream
		FileInputStream fileRead = new FileInputStream(absoluteFilePath);
		XSSFWorkbook workbook = new XSSFWorkbook(fileRead);
		XSSFSheet sheet = workbook.getSheet("Login");
		

		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		
		for(int i=1; i<=rowCount; i++) {
			
			// Fetching username and password from 'testdata' excel sheet
			String username = sheet.getRow(i).getCell(1).getStringCellValue();
			String password = sheet.getRow(i).getCell(2).getStringCellValue();
			
			// Entering username and password to input fields and clicking Login button
			driver.findElement(By.id("txt-username")).sendKeys(username);
			driver.findElement(By.id("txt-password")).sendKeys(password);
			driver.findElement(By.id("btn-login")).click();
			
			
			// Checking if Error text appears for invalid data or not
			List<WebElement> errorTextList = driver.findElements(By.className("text-danger"));
			
			if (errorTextList.size() > 0) {
				// Fetching value in Expected Result cell in testdata.xlsx sheet and error text from website
				String expectedResult = sheet.getRow(i).getCell(3).getStringCellValue();
				String errorText = driver.findElement(By.className("text-danger")).getText();
				
				String status = "Fail";
				
				// Checking if Expected Result text from excel file is same as error text
				if (expectedResult.equals(errorText)) {
					status = "Pass";
				} 
				
				// Setting values in Actual Result and Status column in testdata.xlsx sheet
				sheet.getRow(i).createCell(4).setCellValue(errorText);
				sheet.getRow(i).createCell(5).setCellValue(status);
				
				driver.findElement(By.id("txt-username")).clear();
				driver.findElement(By.id("txt-password")).clear();
			}
			
			
			// Checking if user successfully logged in for valid credentials
			List<WebElement> btnList = driver.findElements(By.id("btn-book-appointment"));
			
			if (btnList.size() > 0) {
				// Fetching value in Expected Result cell in testdata.xlsx sheet
				String expectedResult = sheet.getRow(i).getCell(3).getStringCellValue();
				String status = "Fail";
				
				if (expectedResult.equals("Successful login")) {
					status = "Pass";
				}
				
				// Setting values in Actual Result and Status column in testdata.xlsx sheet
				sheet.getRow(i).createCell(4).setCellValue("Successful login");
				sheet.getRow(i).createCell(5).setCellValue(status);
			}
			
			
			Thread.sleep(1000);
				
		}
			
		// Permanent writing the set values into the workbook and closing the workbook
		FileOutputStream fileWrite = new FileOutputStream(absoluteFilePath);
		workbook.write(fileWrite);
			
		workbook.close();
		
	}
		
		
}


