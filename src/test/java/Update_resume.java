
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.io.File;

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

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.apache.commons.io.FileUtils;

import io.github.bonigarcia.wdm.WebDriverManager;


public class Update_resume  {

	@Test
	public void updateResume() throws AWTException, InterruptedException, IOException {
		
		WebDriverManager.chromedriver().setup();

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
		Thread.sleep(8000);
		try {
  driver.findElement(Locators.GotIt_PopUp_Button).click();
    System.out.println("Cookie popup closed!");
    Thread.sleep(2000);
} catch(Exception e) {
    System.out.println("No cookie popup found, continuing...");
}
		File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
FileUtils.copyFile(screenshot, new File("page_screenshot.png"));
System.out.println("Screenshot taken!");
		driver.findElement(Locators.Login_Button).click();
		String username = System.getenv("NAUKRI_USERNAME") != null? System.getenv("NAUKRI_USERNAME") : properties.getProperty("Username");
		String password = System.getenv("NAUKRI_PASSWORD") != null ? System.getenv("NAUKRI_PASSWORD") : properties.getProperty("Password");
		driver.findElement(Locators.Username).sendKeys(username);
		driver.findElement(Locators.Password).sendKeys(password);
		driver.findElement(Locators.Login_continue_Button).click();
		Wait.until(ExpectedConditions.elementToBeClickable(Locators.View_Profile)).click();
		Thread.sleep(2000);
		
		String resumePath = System.getProperty("user.dir") + File.separator + "src" 
                  + File.separator + "test" 
                  + File.separator + "resources" 
                  + File.separator + "Nithish_QA.pdf";
		driver.findElement(Locators.File_Input).sendKeys(resumePath);
		
		Thread.sleep(3000);  
		System.out.println("File upload attempted");
		
		String LastUpdated = driver.findElement(Locators.Last_Updated).getText();
		Assert.assertEquals(LastUpdated, "Today");
		System.out.println("Resume updated successfully");
		driver.quit();
	}

}
