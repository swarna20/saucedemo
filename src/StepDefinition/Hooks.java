package StepDefinition;

import Driver.FeatureDriver;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import common.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Hooks {

    private static Logger log = Logger.getLogger(Hooks.class);
    public static WebDriver driver;
    public static String scDataTag;
    Common cn = new Common();
    Properties con;
    public static String tagTCID = "";
    public static Scenario sc;
    public Date beforeScExeTimeDate;
    public Date afterScExeTimeDate;
    double scenarioExeDuration;
    public static  List<HashMap<String,String>>  dataMap ;
	private String scenarioTagID="";
	private String scenarioID="";
	public static int iterationID;
	private String status;
	private String scenarioName;
	int countGetIDSplit=0;
	int iteration;


    @Before
    public void initializeTest(Scenario scenario) throws IOException {
		Common.LogSysOut(log, "info", "****** START - Before Cucumber Scenario :" + scenario.getName());
		if(FeatureDriver.openNewDriver) {
			driver = cn.getDriver(FeatureDriver.browser);
			Steps.driver = driver;
			driver.get(Config.URL_CTM);
			System.out.println(">> Loaded URL");
			driver.manage().window().maximize();
		}
		//FeatureDriver.openNewDriver=false;
		sc = scenario;
		iterationID=-1;
        dataMap= cn.getDataFromSheet(scenario);
		scenarioName=scenario.getName();
		scenarioID=scenario.getId();
		countGetIDSplit=scenarioID.split(";").length;
		System.out.println(">>> Sceanrio iD: "+scenarioID);
		beforeScExeTimeDate = new Date();
    }

	@After
	public void postTestProcess(Scenario scenario) throws IOException {
		status=Common.statusMapforCI(scenario.getStatus());
		System.out.println("This is @After test Cucumber");
		System.out.println("<<< SCENARIO ID -- "+	scenario.getId());
		Common.LogSysOut(log, "info", "******START - Cucumber Afer Scenario :" + scenario.getStatus());
		afterScExeTimeDate = new Date();
		scenarioExeDuration=Double.valueOf((new DecimalFormat("#.##")).format((afterScExeTimeDate.getTime() - beforeScExeTimeDate.getTime())/ (double)1000));
		String failReason = "";

		if (scenario.isFailed()) {
			/*FeatureDriver.openNewDriver=true;
			FeatureDriver.requiredToLogin=true; */
			failReason = Common.scenarioFailReason(scenario);

			try {
				Common.takeScreenshot(scenario, Steps.driver,
						"Test Scenario : " + scenarioName + " Status : " + scenario.getStatus());
			} catch (Exception e) {
				Common.LogSysOut(log, "WARN", e.getMessage());

			}

		}else{
			/*FeatureDriver.openNewDriver=false;
			FeatureDriver.requiredToLogin=false;*/
		}
		//System.out.println("Status>>> openNewDriver: "+FeatureDriver.openNewDriver);
		//System.out.println("Status>>> openNewDriver: "+FeatureDriver.requiredToLogin);

		//if(FeatureDriver.openNewDriver) {

			try {
				driver.quit();
				System.out.println("openNewDriver: "+FeatureDriver.openNewDriver+"  Hence: Driver Closed");
				//Common.closeOpenBrowsers(FeatureDriver.browser);
				Common.LogSysOut(log, "info", "Browser closed");
			} catch (Exception e) {
				Common.LogSysOut(log, "error", "Driver Quit: " + e.getMessage());
			}

	//	}

		if(countGetIDSplit>2){

			if(dataMap!=null){
				scenarioTagID = scenario.getSourceTagNames().toArray()[0].toString().split("@")[1] + "_" + iterationID;
				scenario.write("Outline: Data from datasheet, Iteration# "+scenarioTagID);
			}else{
				iteration=Integer.parseInt(scenarioID.substring(scenarioID.lastIndexOf(";") + 1).trim())-1;
				scenarioTagID = scenario.getSourceTagNames().toArray()[0].toString().split("@")[1] + "_" + iteration;
				scenario.write("Outline: Data from feature file and not from Data Sheet, Iteration# "+scenarioTagID);
			}

		}else{
			scenarioTagID = scenario.getSourceTagNames().toArray()[0].toString().split("@")[1];
			if(dataMap!=null){
				scenario.write("No Iteration, Data from datasheet");
			}else{
				scenario.write("No Iteration, No data or data from feature file");
			}
		}

		System.out.println("<<<< size of split array"+scenarioID.split(";").length);
		System.out.println("<<<< last string "+scenarioID.substring(scenarioID.lastIndexOf(';') + 1).trim());
		FeatureDriver.resultData.add(new String[]{ FeatureDriver.startExeTimeDate, Config.OS_NAME, Config.HOST_NAME, Config.SUITE_APPNAME, FeatureDriver.featureName, scenarioName, scenarioTagID,status,failReason, Config.REPORT_EXECUTIONSUMMARYREPORT_PATH, Config.REPORT_CUCUMBERHTML_PATH, String.valueOf(scenarioExeDuration) });
		Common.LogSysOut(log, "info", "***** END - Cucumber After Scenario :" + scenario.getStatus());
	}
       
  }






