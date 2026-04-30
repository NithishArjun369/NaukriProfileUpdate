
import org.openqa.selenium.By;

public class Locators {
	
	public static final By Login_Button = By.xpath("//a[text()='Login']");
	public static final By Username = By.xpath("//label[contains(text(),'Username')]/following::input[1]");
	public static final By Password = By.xpath("//label[contains(text(),'Password')]/following::input[1]");
	public static final By Login_continue_Button = By.xpath("//button[text()='Login']");
	public static final By View_Profile = By.xpath("(//a[contains(text(),'View')])[1]");
	public static final By File_Input = By.id("attachCV");
	public static final By Last_Updated = By.cssSelector("span.mod-date-val");

}
