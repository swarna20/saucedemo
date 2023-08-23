package StepDefinition;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import Page.SaucedemoAppMethods;
import common.Common;
import cucumber.api.Scenario;
import org.apache.log4j.Logger;

//import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Steps {

	static Logger log = Logger.getLogger(Steps.class);
	// static LogManager log= (LogManager)
	// LogManager.getLogger(Steps.class.getName());

	// private static final org.apache.logging.log4j.Logger log =
	// LogManager.getLogger(Steps.class.getName());
	public static WebDriver driver = null;
	private final Common cn = new Common();
	private SaucedemoAppMethods commonMethods = PageFactory.initElements(driver, SaucedemoAppMethods.class);;
	private Properties obj;
	public static List<HashMap<String, String>> dataMap;
	Properties con;

	public String userFirstName,userLastName, zipcode, item1Name, item2Name, item3Name;

	Scenario sc;

	public Steps() throws IOException {
		sc = Hooks.sc;
		dataMap = Hooks.dataMap;

	}

	public void testData() {
		userFirstName = dataMap.get(0).get("UserFirstName");
		userLastName = dataMap.get(0).get("UserLastName");
		zipcode = dataMap.get(0).get("Zipcode");
		item1Name = dataMap.get(0).get("Item1Name");
		item2Name = dataMap.get(0).get("Item2Name");
		item3Name = dataMap.get(0).get("Item3Name");		
		
	}

	/// get currency conversion values

	@Given("^Login as Admin to the CTM application$")
	public void AdminLogin() throws Throwable {
		testData();
		//loginpage.login(adminUsername, adminPassword);

	}

	
		
	@Then("^Verify the Page Title$")
	public void checkThePageTitle() throws InterruptedException {
		commonMethods.checkThePageTitleOfTheURL();
	}

	@Given("Enter username as ([^\"]*) and password as ([^\\\"]*)$")
	public void enterUserNameAndPassword(String arg1, String arg2) throws InterruptedException {
		//commonMethods.OpenURLInBrowser();
		commonMethods.enterUserNameAndPasswordFromPropo(arg1, arg2);
		
	}

	@Then("^Click on Login button$")
	public void clickLoginButton() throws InterruptedException {
		commonMethods.clickOnButtonByid("login-button");
	}

	@Then("^Verify that user logged In successfully$")
	public void checkTheUserLoginIsSuccess() throws InterruptedException {
		commonMethods.checkTextOfElementByXpath("//div[@class='header_secondary_container']/span", "Products");
	}

	@When("^Add Multiple Items to cart$")
	public void AddMultipleItemsToCart() throws InterruptedException {
		testData();
		System.out.println("item1Name..."+	item1Name);
		commonMethods.addItemToCart(item1Name);
		commonMethods.addItemToCart(item2Name);
		commonMethods.addItemToCart(item3Name);
	}

	@Then("^Verify that Items are added to cart$")
	public void verifyItemsAddedToCart() throws InterruptedException {
		commonMethods.checkTextOfElementByXpath("//span[@class='shopping_cart_badge']", "3");
	}

	@When("^Click on cart icon$")
	public void clickCartIcon() throws InterruptedException {
		commonMethods.clickByXpath("//a[@class='shopping_cart_link']");
		System.out.println(">>>> Cart Icon is clicked...");
	}

	@And("^Click on Checkout button$")
	public void clicCheckOutButton() throws InterruptedException {
		commonMethods.clickOnButtonByid("checkout");
	}

	@And("^Provide UsSer Information$")
	public void inputUserInformation() throws InterruptedException {
		testData();
		commonMethods.inputDataToTextFieldById("first-name", userFirstName);
		commonMethods.inputDataToTextFieldById("last-name", userLastName);
		commonMethods.inputDataToTextFieldById("postal-code", zipcode);
	}

	@And("^Click on Continue button$")
	public void clickContinueButton() throws InterruptedException {
	commonMethods.clickOnButtonByid("continue");
	}

	@When("^Click on Finish button$")
	public void clickFinishButton() throws InterruptedException {
		commonMethods.clickOnButtonByid("finish");
	}

	@Then("^Validate the confirmation Message of order placement$")
	public void validateConfirmatioMessage() throws InterruptedException {
		commonMethods.checkTextOfElementByXpath("//h2[@class='complete-header']", "Thank you for your order!");
	}


}