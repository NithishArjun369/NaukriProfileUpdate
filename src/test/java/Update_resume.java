
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Update_resume  {

	 
	@Test
	public void updateResume() throws InterruptedException, IOException {
		
		// Get credentials
        String username = System.getenv("NAUKRI_USERNAME");
        String password = System.getenv("NAUKRI_PASSWORD");
		
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-first-run");
		options.addArguments("--no-default-browser-check");
		options.addArguments("--disable-default-apps");
		options.addArguments("--disable-notifications");
		options.addArguments("--disable-extensions");
		options.addArguments("--start-maximized");
		options.addArguments("--incognito");
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new ChromeDriver(options);
		
		Properties properties = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/Config.properties");
		properties.load(fis);
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		WebDriverWait Wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		
		driver.get(properties.getProperty("url"));
		Thread.sleep(3000);
		driver.findElement(Locators.Login_Button).click();
		driver.findElement(Locators.Username).sendKeys(username);
		driver.findElement(Locators.Password).sendKeys(password);
		driver.findElement(Locators.Login_continue_Button).click();
		Wait.until(ExpectedConditions.elementToBeClickable(Locators.View_Profile)).click();
		Thread.sleep(2000);
		
		String resumePath = System.getProperty("user.dir") + "\\src\\test\\resources\\Nithish_QA.pdf";
		driver.findElement(Locators.File_Input).sendKeys(resumePath);
		Thread.sleep(3000);  
		System.out.println("File upload attempted");
		System.out.println("Resume updated successfully");
		
		String LastUpdated = driver.findElement(Locators.Last_Updated).getText();
		Assert.assertEquals(LastUpdated, "Today");
		driver.quit();
	}

}
