package common;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;

import static java.net.InetAddress.getLocalHost;

public class Config {

    static Properties pro;

    static {
        try {
            pro = Common.getConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static final String URL_CTM=pro.getProperty("url.CTM");
 
    public static final String RUN_FROM_TESTPLAN=pro.getProperty("runFromTestPlan");
    public static final String CLASS_PER_TESTCASE=pro.getProperty("isClass.PerTestCase");
    public static final String RUNSHEET_FROM_TESTPLAN=pro.getProperty("runSheetFromTestPlan");
    public static final String SUITE_APPNAME=pro.getProperty("suite.appName");

    public static final String DATATABLE_FILE_PATH=pro.getProperty("dataTablePath");
    public static final String DATATABLE_MAPSHEET_NAME=pro.getProperty("dataTable.mapSheet.name");
    public static final String OBJECTREPO_FILE_PATH=pro.getProperty("objectRepoPath");
    public static final String TESTPLAN_FILE_PATH=pro.getProperty("testPlanPath");
    public static final String DRIVER_CHROME_PATH=pro.getProperty("driver.chromePath");
    public static final String DRIVER_FIREFOX_PATH=pro.getProperty("driver.firefoxPath");

    public static final String DRIVER_IE_PATH=pro.getProperty("driver.iePath");
    public static final String DRIVER_EDGE_PATH=pro.getProperty("driver.edgePath");
    public static final String REPORT_DIR=pro.getProperty("report.dir");
    public static String REPORT_EXECUTIONSUMMARYREPORT_PATH=pro.getProperty("report.executionSummaryReportPath");
    public static final String REPORT_CUCUMBERHTML_PATH=pro.getProperty("report.cucumberHTMLPath");
    public static final String REPORT_SHEET_NAME=pro.getProperty("report.sheet.name");
    public static final String REPORT_HEADER_TESTDATE=pro.getProperty("report.header.testDate");
    public static final String REPORT_HEADER_OS=pro.getProperty("report.header.OS");
    public static final String REPORT_HEADER_HOSTNAME=pro.getProperty("report.header.hostName");
    public static final String REPORT_HEADER_APPLICATION=pro.getProperty("report.header.application");
    public static final String REPORT_HEADER_FEATURENAME=pro.getProperty("report.header.featureName");
    public static final String REPORT_HEADER_SCENARIONAME=pro.getProperty("report.header.scenarioName");
    public static final String REPORT_HEADER_SCENARIOID=pro.getProperty("report.header.scenarioID");
    public static final String REPORT_HEADER_SCENATIOSTATUS=pro.getProperty("report.header.scenarioStatus");
    public static final String REPORT_HEADER_SCENARIODURATION=pro.getProperty("report.header.scenarioDuration");
    public static final String REPORT_HEADER_REASON=pro.getProperty("report.header.reason");
    public static final String REPORT_HEADER_EXECUTIONSUMMARY=pro.getProperty("report.header.executionSummary");
    public static final String REPORT_HEADER_HTMLREPORT=pro.getProperty("report.header.HTMLReport");
    public static final String LOG4J_CONFIG_PATH=pro.getProperty("log4jConfigPath");
    public static final String CUCUMBER_OPTION_FEATURES=pro.getProperty("cucumber.option.features");
    public static final String CUCUMBER_OPTION_GLUE=pro.getProperty("cucumber.option.glue");
    public static final String CUCUMBER_OPTION_TAGS=pro.getProperty("cucumber.option.tags");
    public static final String OS_NAME=System.getProperty("os.name");
    public static final String SCREENSHOTS_PATH=pro.getProperty("report.html.screenshotsPath");
    public static final String REPORT_EXTENDED_CONFIG_FILE_PATH=pro.getProperty("report.html.configFilePath");
    public static final String REPORT_EXTENDED_PATH=pro.getProperty("report.html.path");
    public static String HOST_NAME;

    static {
        try {
            HOST_NAME = getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] s) throws IllegalAccessException {
    Config cf=new Config();
    System.out.println(RUN_FROM_TESTPLAN);
    Field[] fields = cf.getClass().getDeclaredFields();
    for ( Field field : fields  ) {
        System.out.println(field.getName()+"="+field.get(cf));
    }
    Enumeration<?> e = pro.propertyNames();
    while (e.hasMoreElements()) {
        String key = (String) e.nextElement();
        String value = pro.getProperty(key);
        System.out.println(key + "=" + value);
    }

}

}
