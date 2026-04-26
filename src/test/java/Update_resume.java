
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

	 // Save cookies to file
    public void saveCookies(WebDriver driver) throws IOException {
        String path = System.getProperty("user.dir")
                    + File.separator + "src"
                    + File.separator + "test"
                    + File.separator + "resources"
                    + File.separator + "cookies.data";

        System.out.println("Saving cookies to: " + path);

        File cookieFile = new File(path);
        FileWriter fw = new FileWriter(cookieFile);

        for(Cookie cookie : driver.manage().getCookies()) {
            fw.write(cookie.getName() + ";" + cookie.getValue() + ";"
                    + cookie.getDomain() + ";" + cookie.getPath() + ";"
                    + cookie.getExpiry() + ";" + cookie.isSecure() + "\n");
        }
        fw.close();
        System.out.println("Cookies saved successfully!");
    }

    // Load cookies from file
    public void loadCookies(WebDriver driver) throws IOException {
        String cookiePath = System.getProperty("user.dir")
                          + File.separator + "src"
                          + File.separator + "test"
                          + File.separator + "resources"
                          + File.separator + "cookies.data";

        System.out.println("Loading cookies from: " + cookiePath);

        BufferedReader br = new BufferedReader(new FileReader(cookiePath));
        String line;
        while((line = br.readLine()) != null) {
            String[] token = line.split(";");
            if(token.length >= 2) {
                Cookie cookie = new Cookie(token[0], token[1]);
                driver.manage().addCookie(cookie);
            }
        }
        br.close();
        System.out.println("Cookies loaded successfully!");
    }
	
	@Test
	public void updateResume() throws InterruptedException, IOException {
		
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new ChromeDriver(options);
		
		Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(
            System.getProperty("user.dir") + File.separator + "src"
            + File.separator + "test"
            + File.separator + "resources"
            + File.separator + "Config.properties"
        );
        properties.load(fis);
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		WebDriverWait Wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		
		driver.get(properties.getProperty("url"));
		Thread.sleep(5000);

		File screenshot1 = ((TakesScreenshot)driver)
                           .getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot1, new File("page_screenshot.png"));
        System.out.println("Initial screenshot taken!");

		// Check if cookies file exists
        String cookiePath = System.getProperty("user.dir")
                          + File.separator + "src"
                          + File.separator + "test"
                          + File.separator + "resources"
                          + File.separator + "cookies.data";

        File cookieFile = new File(cookiePath);
        System.out.println("Looking for cookies at: " + cookiePath);
        System.out.println("Cookies file exists: " + cookieFile.exists());

		if(cookieFile.exists()) {
            // Load cookies and skip login
            System.out.println("Loading cookies - skipping login!");
            loadCookies(driver);
            driver.navigate().refresh();
            Thread.sleep(5000);
            System.out.println("Logged in via cookies!");
        } else {
            // No cookies - do normal login
            System.out.println("No cookies found - doing normal login...");

            // Handle cookie popup
            try {
                WebElement cookieBtn = driver.findElement(
                    By.xpath("//button[text()='Got it']"));
                cookieBtn.click();
                Thread.sleep(1000);
                System.out.println("Cookie popup closed!");
            } catch(Exception e) {
                System.out.println("No cookie popup found");
            }

            // Click Login button
            driver.findElement(Locators.Login_Button).click();
            Thread.sleep(2000);

            // Get credentials
            String username = System.getenv("NAUKRI_USERNAME") != null
                            ? System.getenv("NAUKRI_USERNAME")
                            : properties.getProperty("Username");

            String password = System.getenv("NAUKRI_PASSWORD") != null
                            ? System.getenv("NAUKRI_PASSWORD")
                            : properties.getProperty("Password");

            driver.findElement(Locators.Username).sendKeys(username);
            driver.findElement(Locators.Password).sendKeys(password);
            driver.findElement(Locators.Login_continue_Button).click();
            Thread.sleep(5000);

            // Save cookies after successful login
            saveCookies(driver);
        }
		
		// Take screenshot after login
        File screenshot2 = ((TakesScreenshot)driver)
                           .getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot2, new File("after_login.png"));
        System.out.println("After login screenshot taken!");

		Wait.until(ExpectedConditions.elementToBeClickable(Locators.View_Profile)).click();
		Thread.sleep(2000);
		
		String resumePath = System.getProperty("user.dir") + File.separator + "src" 
                  + File.separator + "test" 
                  + File.separator + "resources" 
                  + File.separator + "Nithish_QA.pdf";
		
		System.out.println("Uploading resume from: " + resumePath);
		driver.findElement(Locators.File_Input).sendKeys(resumePath);
		Thread.sleep(5000);  
		// Take screenshot after upload
        File screenshot3 = ((TakesScreenshot)driver)
                           .getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot3, new File("after_upload.png"));
        System.out.println("After upload screenshot taken!");
		System.out.println("File upload attempted");
		
		String LastUpdated = driver.findElement(Locators.Last_Updated).getText();
		Assert.assertEquals(LastUpdated, "Today");
		System.out.println("Resume updated successfully");
		driver.quit();
	}

}
