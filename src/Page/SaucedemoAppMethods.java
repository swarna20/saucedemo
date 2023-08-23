package Page;

import common.Config;
import cucumber.api.Scenario;
import cucumber.runtime.ScenarioImpl;
import gherkin.formatter.model.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.velocity.texen.util.FileUtil;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;

import Driver.FeatureDriver;

public class SaucedemoAppMethods {
	public WebDriver driver;
	private WebDriverWait wait;
	// private static Logger log = Logger.getLogger(LoginPage.class);

	// final String s1= Config.CUCUMBER_OPTION_FEATURES;

	public SaucedemoAppMethods(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, 100);
		PageFactory.initElements(driver, this);
	}

	public void loadURL() throws InterruptedException {

		driver.get(Config.URL_CTM);
		System.out.println(">> Loaded URL");
		driver.manage().window().maximize();
		
	}
	public void checkThePageTitleOfTheURL() throws InterruptedException {
		String titleOfPage = driver.getTitle();
		System.out.println(">>>> titleOfPage is..." + titleOfPage);
		Assert.assertEquals(titleOfPage, "Swag Labs");
	}

	public void OpenURLInBrowser() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver_win32/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
	//	driver.get(properties.getProperty("browser.baseURL"));
		Thread.sleep(3000);

	}

	public void enterUserNameAndPasswordFromPropo(String arg1, String arg2) throws InterruptedException {
	//	loadURL();
		System.out.println(">>>> Entered username is..." + arg1);

		driver.findElement(By.id("user-name")).sendKeys(arg1);
		System.out.println(">>>> Entered username is..." + arg1);
		driver.findElement(By.id("password")).sendKeys(arg2);
		System.out.println(">>>> Entered password is..." + arg2);

	}

	public void clickOnButtonByid(String id) throws InterruptedException {
		driver.findElement(By.id(id)).click();
		System.out.println(">>>> " + id + "  Button is clicked...");
	}

	public void addItemToCart(String itemName) {
		System.out.println(">>>>required itemName is " + itemName);

		int j = 0;
		List<WebElement> items = driver.findElements(By.xpath("//div[@class='inventory_item_label']/a/div"));
		for (int i = 0; i < items.size(); i++) {
			System.out.println(">>>> items.get(i).getText()..." + items.get(i).getText());
			if (items.get(i).getText().equalsIgnoreCase(itemName)) {
				j = i + 1;
				break;
			}
		}
		String addToCartButtonXpath = "(//div[@class='pricebar']/button)[" + j + "]";
		driver.findElement(By.xpath(addToCartButtonXpath)).click();
		System.out.println(">>>>Add to Cart button of Item is clicked...");

	}

	public void checkTextOfElementByXpath(String xpath, String expectedText) throws InterruptedException {
		String titleOfPage = driver.findElement(By.xpath(xpath)).getText();
		System.out.println(">>>> titleOfPage is..." + titleOfPage);
		Assert.assertEquals(titleOfPage, expectedText);

	}

	public void clickByXpath(String xpath) throws InterruptedException {
		driver.findElement(By.xpath(xpath)).click();
	}

	public void inputDataToTextFieldById(String id, String textToInput) throws InterruptedException {

		driver.findElement(By.id(id)).sendKeys(textToInput);
		System.out.println(">>>> Given text..." + textToInput + "...is Entered in " + id);

	}

	public void closeAllBroswerWindows() throws InterruptedException {
		// driver.quit();

		try {
			Runtime.getRuntime().exec("cmd /c taskkill /f /t /im chrome.exe");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
