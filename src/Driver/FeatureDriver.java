package Driver;


import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import org.testng.annotations.*;
import common.*;

import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.TestNGCucumberRunner;

/*@CucumberOptions(
        //features = "feature1",
        //glue = {"StepDefinition"},
        //tags = {"@JTC1"},
        format = {
                "pretty",
                "html:target/cucumber-reports/cucumber-pretty",
                "json:target/cucumber-reports/CucumberTestReport.json"
        })*/

 public class FeatureDriver {
   static Logger log = Logger.getLogger(FeatureDriver.class);

    // private static final Logger log = LogManager.getLogger(FeatureDriver.class.getName());
     


    private String cucumberFeature=Config.CUCUMBER_OPTION_FEATURES;
    private String cucumberGlue=Config.CUCUMBER_OPTION_GLUE;
    private String runFromTP=Config.RUN_FROM_TESTPLAN;
    private TestNGCucumberRunner testNGCucumberRunner;
    public static String browser;
    private String testPlanPath=Config.TESTPLAN_FILE_PATH;
    public static String startExeTimeDate;
    public static String featureName;
    private String tcTags;
    private String excelReportPath=Config.REPORT_EXECUTIONSUMMARYREPORT_PATH;
    private String sheetNameTP=Config.RUNSHEET_FROM_TESTPLAN;
    Common cn=new Common();
    Properties con;
    public static ArrayList<String[]> resultData=new ArrayList<>();
    public static boolean openNewDriver=true;
    public static boolean requiredToLogin=true;


    public FeatureDriver() {
        System.setProperty("log4j.configurationFile",Config.LOG4J_CONFIG_PATH);

        //PropertyConfigurator.configure(Config.LOG4J_CONFIG_PATH);
  /*      Common.LogSysOut(log,"info","Log4j config path: "+Config.LOG4J_CONFIG_PATH);
        Common.closeExcel();
        Common.closeOpenBrowsers();
        cn.moveToArchiveExecutionSummary();
        cn.createExecutionSummaryTemplate();
        if(runFromTP.equalsIgnoreCase("yes") || runFromTP.equalsIgnoreCase("Y")) {
            tcTags=cn.getTagsFromTestPlan(testPlanPath,sheetNameTP); //--tags @JUSATC1
        }else{
            tcTags="--tags "+Config.CUCUMBER_OPTION_TAGS;
        }
        System.setProperty("cucumber.options", cucumberFeature+" "+"--glue "+cucumberGlue+" "+tcTags+" "+"--plugin pretty --plugin html:"+Config.REPORT_CUCUMBERHTML_PATH+"/pretty --plugin json:"+Config.REPORT_CUCUMBERHTML_PATH+"/Cucumber.json");
        */

        //System.setProperty("cucumber.options", cucumberFeature+" "+"--glue "+cucumberGlue+" "+tcTags+" "+"--plugin json:target/cucumber/Cucumber.json");
    }

    @BeforeSuite
    public void parentSetUp() throws Exception {
        PropertyConfigurator.configure(Config.LOG4J_CONFIG_PATH);
        Common.LogSysOut(log,"info","Log4j config path: "+Config.LOG4J_CONFIG_PATH);
        Common.closeExcel();
        cn.mkdir(Config.REPORT_DIR);
        cn.moveToArchiveExecutionSummary();
        System.out.println("Excel path: "+Config.REPORT_EXECUTIONSUMMARYREPORT_PATH);
        cn.createExecutionSummaryTemplate();
        if(runFromTP.equalsIgnoreCase("yes") || runFromTP.equalsIgnoreCase("Y")) {
            tcTags=cn.getTagsFromTestPlan(testPlanPath,sheetNameTP); //--tags @ETMTC01
        }else{
        	
            tcTags="--tags "+Config.CUCUMBER_OPTION_TAGS;
            System.out.println("Tags from coonfirg file" +tcTags);
        }
        System.setProperty("cucumber.options", cucumberFeature+" "+"--glue "+cucumberGlue+" "+tcTags+" "+"--plugin pretty --plugin html:"+Config.REPORT_CUCUMBERHTML_PATH+"/pretty --plugin json:"+Config.REPORT_CUCUMBERHTML_PATH+"/Cucumber.json");

    }
//Parameter browsernmae is taken from TESTNG.XML
    @Parameters({"browserName"})
    @BeforeClass(alwaysRun = true)
    public void setUpClass(String browserName) throws Exception {
        Common.LogSysOut(log,"info","This is @Before class Test NG start");
        browser=browserName.trim().toUpperCase();
        System.out.println(browser);
        Common.closeOpenBrowsers(browser);
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date Date = new Date();
        startExeTimeDate = sdf.format(Date);
    }


    @Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "features")
    public void feature(CucumberFeatureWrapper cucumberFeature) {
        featureName =cucumberFeature.getCucumberFeature().getGherkinFeature().getName();
        //cucumberFeature.getCucumberFeature().getGherkinFeature().getTags().add(new Tag("hello",0));
        Common.LogSysOut(log,"info","This is @Test start for Feature: "+featureName);
        testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
    }

    @DataProvider
    public Object[][] features() {
        //System.out.println("This is TestNG Data provider reading Feature file to get looped based on number of feature file");
        Common.LogSysOut(log,"info","This is @DataProvider start, execute each feature files as iterations");
        return testNGCucumberRunner.provideFeatures();

    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() throws Exception {
        Common.closeOpenBrowsers(browser);
        Common.LogSysOut(log,"info","This is @AfterClass TestNG start");
        testNGCucumberRunner.finish();
        Common.LogSysOut(log,"info",">>>>> Updating result in SUMMARY REPORT EXCEL");
        cn.updateResultInExecutionSummaryReport(new File(excelReportPath),resultData);
     }
}