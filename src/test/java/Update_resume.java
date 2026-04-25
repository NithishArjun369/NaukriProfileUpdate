
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;


public class Update_resume  {

	@Test
	public void updateResume() throws AWTException, InterruptedException, IOException {
		
		WebDriverManager.chromedriver().setup();
		/*ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-gpu");
		WebDriver driver = new ChromeDriver(options);
		WebDriver driver = new ChromeDriver();*/
		
		//Disable Notifications
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-first-run");
		options.addArguments("--no-default-browser-check");
		options.addArguments("--disable-default-apps");
		options.addArguments("--disable-notifications");
		options.addArguments("--disable-extensions");
		options.addArguments("--start-maximized");
		options.addArguments("--incognito");
		WebDriver driver = new ChromeDriver(options);
		
		Properties properties = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/Config.properties");
		properties.load(fis);
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		WebDriverWait Wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		
		driver.get(properties.getProperty("url"));
		Thread.sleep(3000);
		driver.manage().deleteAllCookies();
		driver.navigate().refresh();
		Thread.sleep(3000);
		driver.findElement(Locators.Login_Button).click();
		driver.findElement(Locators.Username).sendKeys(properties.getProperty("Username"));
		driver.findElement(Locators.Password).sendKeys(properties.getProperty("Password"));
		driver.findElement(Locators.Login_continue_Button).click();
		Wait.until(ExpectedConditions.elementToBeClickable(Locators.View_Profile)).click();
		driver.findElement(Locators.Update_resume_Button).click();
		Thread.sleep(2000);
		
		String resumePath = System.getProperty("user.dir") + "\\src\\test\\resources\\Nithish_QA.pdf";
		String autoITPath = System.getProperty("user.dir") + "\\src\\test\\resources\\fileupload.exe";
		ProcessBuilder pb = new ProcessBuilder(autoITPath, resumePath);
		pb.start();
		Thread.sleep(5000);  
		System.out.println("File upload attempted");
		
		String LastUpdated = driver.findElement(Locators.Last_Updated).getText();
		Assert.assertEquals(LastUpdated, "Today");
		System.out.println("Resume updated successfully");
		driver.quit();
	}

}
