package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.relevantcodes.extentreports.ExtentTest;
import cucumber.api.Scenario;
import cucumber.runtime.ScenarioImpl;
import gherkin.formatter.model.Result;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.Level;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Reporter;

/*
*This class contains the common utility methods to perform the intermediate functions during end to end testing
* such as DataSheet connection, getScreenshot, settter and getters
* @Author: Janagan jsivapatha@wiley.com
 */

public class Common {

	// String objectMapProfilePath="./resources/Objectsmap.properties";
	private static String configFilePath = "./resources/config/Config.properties";
	private static Logger log = Logger.getLogger(Common.class);
	//private static final Logger log = LogManager.getLogger(Common.class.getName());
	//private static final Logger log = LogManager.getRootLogger();//getLogger(Common.class.getName());

	public static void main(String[] args) throws IOException {
		Common c = new Common();
		// String xyz[]=c.getDataFromDsheet("TC_01") ;
	}

	public WebDriver getDriver(String browserName) throws IOException {
		WebDriver wd;
		browserName = browserName.trim().toUpperCase();
		Properties pro = getConfig();
		if (browserName.equals("CHROME")) {
			System.setProperty("webdriver.chrome.driver", Config.DRIVER_CHROME_PATH);
			wd = new ChromeDriver();
		} else if (browserName.equals("FIREFOX") || browserName.equals("FF")) {
			System.setProperty("webdriver.gecko.driver", Config.DRIVER_FIREFOX_PATH);
			wd = new FirefoxDriver();
		} else if (browserName.equals("IE") || browserName.equals("Internet Explorer")) {
			System.setProperty("webdriver.ie.driver", Config.DRIVER_IE_PATH);
			wd = new InternetExplorerDriver();
		} else if (browserName.equals("EDGE")) {
			System.setProperty("webdriver.edge.driver", Config.DRIVER_EDGE_PATH);
			wd = new EdgeDriver();
		} else {
			System.setProperty("webdriver.gecko.driver", Config.DRIVER_FIREFOX_PATH);
			wd = new FirefoxDriver();
		}
		wd.manage().window().maximize();
		wd.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		System.out.println("Driver name: " + browserName);
		System.out.println(browserName + " >> Browser loaded");
		return wd;
	}

	public static void takeScreenshot(Scenario sc, WebDriver driver, String detail) {
		if (driver == null) {

			sc.write("Driver is not available, NO screenshot: " + detail);

		} else {
			try {
				sc.write(detail);
				byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				LogSysOut(log, "info", "Screenshots taken for step " + sc.getName());
				sc.embed(screenshot, "image/png"); // Stick it in the report
			} catch (WebDriverException PlatformNotSupport) {
				LogSysOut(log, "error", PlatformNotSupport.getMessage());
			} catch (ClassCastException cEx) {
				cEx.printStackTrace();
				LogSysOut(log, "error", cEx.getMessage());
			}
		}

	}

	public static Properties getConfig() throws IOException {

		Properties con = new Properties();
		FileInputStream confile = new FileInputStream(configFilePath);
		con.load(confile);
		confile.close();
		System.out.println("Config loaded: " + configFilePath);
		return con;

	}

	public static Properties getObject() throws IOException {

		Properties con = new Properties();
		FileInputStream confile = new FileInputStream(Config.OBJECTREPO_FILE_PATH);
		con.load(confile);
		confile.close();
		System.out.println("Config loaded: " + Config.OBJECTREPO_FILE_PATH);
		return con;

	}

	public static final String getObject(String filedName) {
		try {
			Properties con = new Properties();
			FileInputStream confile = new FileInputStream(Config.OBJECTREPO_FILE_PATH);
			con.load(confile);
			confile.close();
			System.out.println("Config loaded: " + Config.OBJECTREPO_FILE_PATH);

			return con.getProperty(filedName);
		} catch (Exception e) {
			return "ERROR";
		}

	}

	public void mkdir(String dirName) {
		try {
			System.out.println("Create directory if not exist : " + dirName);
			new File("./" + dirName.trim()).mkdir();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public static void LogSysOut(Logger Log, String Loglevel, String logmsg) {
		try {
			String LL = Loglevel.trim().toUpperCase();
			if (LL.equals("INFO")) {
				//Log.info(logmsg);
				Log.log(Level.INFO,logmsg);
				System.out.println(logmsg);
			} else if (LL.equals("ERROR")) {
				//Log.error(logmsg);
				Log.log(Level.ERROR,logmsg);
				System.out.println(logmsg);
			} else if (LL.equals("DEBUG")) {
				Log.log(Level.DEBUG,logmsg);
				//Log.debug(logmsg);
				System.out.println(logmsg);
			} else if (LL.equals("WARN")) {
				Log.log(Level.WARN,logmsg);
				//Log.warn(logmsg);
				System.out.println(logmsg);
			} else {
				//Log.info(logmsg);
				Log.log(Level.INFO,logmsg);
				System.out.println(logmsg);
			}
		} catch (Exception e) {
			System.out.println("Log write error, SKIP this   :" + e.getMessage());
		}
	}
#/

	 */

	public static void LogSysOut(Logger Log, String Loglevel,String logmsg)  {
		try {
			String LL = Loglevel.trim().toUpperCase();
			if (LL.equals("INFO")) {
				Log.info(logmsg);
				System.out.println(logmsg);
			} else if (LL.equals("ERROR")) {
				Log.error(logmsg);
				System.out.println(logmsg);
			} else if (LL.equals("DEBUG")) {
				Log.debug(logmsg);
				System.out.println(logmsg);
			} else if (LL.equals("WARN")) {
				Log.warn(logmsg);
				System.out.println(logmsg);
			} else {
				Log.info(logmsg);
				System.out.println(logmsg);
			}
		}catch(Exception e){
			System.out.println("Log write error, SKIP this   :"+e.getMessage());
		}
	}


	public static void closeExcel() {
		try {
			LogSysOut(log, "info", "Close all the EXCEL instance if anything opened already");
			Runtime.getRuntime().exec("cmd /c taskkill /f /t /im excel.exe");

			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeOpenBrowsers(String browsername) throws IOException {
		try {
			if (browsername.trim().equalsIgnoreCase("chrome")) {
				LogSysOut(log, "info", "Close all the chromedriver instance if anything opened already");
				Runtime.getRuntime().exec("cmd /c taskkill /f /t /im chrome.exe");
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String scenarioFailReason(Scenario scenario) {
		String errmsg = "";
		Field field = FieldUtils.getField(((ScenarioImpl) scenario).getClass(), "stepResults", true);
		field.setAccessible(true);
		int stepindex = 0;
		try {
			ArrayList<Result> results = (ArrayList<Result>) field.get(scenario);
			for (Result result : results) {
				stepindex = stepindex + 1;
				if (result.getError() != null && !result.getError().equals("")) {
					errmsg = "Reason for fail at step " + stepindex + " : " + result.getErrorMessage() + "." + errmsg;
					// LogSysOut(log, "error", errmsg);
				}

			}
		} catch (Exception e) {
			LogSysOut(log, "error", "Error while logging error:" + e.getMessage());
			errmsg = e.getMessage();
		}
		return errmsg;
	}

	public String getTCIDofScenario(Scenario sc) {
		String tagname = "";
		boolean tcTag = false;
		int matchTag = 0;
		String scDataTag = "";
		LogSysOut(log, "info", "Scenario Data Tag Size : " + sc.getSourceTagNames().size());
		for (int i = 0; i < sc.getSourceTagNames().size(); i++) {

			tagname = sc.getSourceTagNames().toArray()[i].toString();
			tcTag = tagname.trim().toUpperCase().startsWith("@TC");
			if (tcTag) {
				matchTag = i + 1;
				scDataTag = tagname.replace("@", "");
				Common.LogSysOut(log, "info", "Found tc tag @ " + matchTag + " st/nd/th/rd position tag as TCID: "
						+ scDataTag + "  for Scenario: \"" + sc.getName() + "\"");
				break;
			}

		}
		if (!tcTag) {
			Common.LogSysOut(log, "WARN",
					"No TAG FOUND with TCID, please assing the Test Case ID for Scenario : " + sc.getName());
		}
		return scDataTag;
	}

	public List<HashMap<String, String>> getDataFromSheet(Scenario sc) throws IOException {
		List<HashMap<String, String>> scenarioData = null;
		FileInputStream fis = new FileInputStream(new File(Config.DATATABLE_FILE_PATH));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheet(Config.DATATABLE_MAPSHEET_NAME);
		String sheetName = null;

		for (int rowNumber = 1; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
			XSSFRow row = (XSSFRow) sheet.getRow(rowNumber);
			Cell scNameFromMapSheet = row.getCell(1);
			Cell tcSheetNameFromMapSheet = row.getCell(2);
			// LogSysOut(log,"INFO","Scenario name to match in datasheet: "+sc.getName());
			if (scNameFromMapSheet.getStringCellValue().trim().equalsIgnoreCase(sc.getName().trim())) {
				// scenarionNo = (int) sheet.getRow(rowNumber).getCell(1).getNumericCellValue();
				LogSysOut(log, "INFO",
						"TCMap cell value for scenario name: " + scNameFromMapSheet.getStringCellValue());
				LogSysOut(log, "INFO", "Found DataSheet name is: " + stringCellValueTCMAP(tcSheetNameFromMapSheet));
				sheetName = stringCellValueTCMAP(tcSheetNameFromMapSheet);
				LogSysOut(log, "INFO", "DataSheet name is: " + sheetName);
				break;
			}

		}

		if (existSheetName(workbook, sheetName)) {

			XSSFSheet sheet1 = workbook.getSheet(sheetName);
			scenarioData = data(sheet1);
		}
		workbook.close();
		fis.close();
		return scenarioData;
		// return dataMap;
	}

	public List<HashMap<String, String>> getDataFromSheet(String testCaseID) throws IOException {
		List<HashMap<String, String>> tcData = null;
		FileInputStream fis = new FileInputStream(new File(Config.DATATABLE_FILE_PATH));
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheet(Config.DATATABLE_MAPSHEET_NAME);
		String sheetName = null;

		for (int rowNumber = 1; rowNumber <= sheet.getLastRowNum(); rowNumber++) {
			XSSFRow row = (XSSFRow) sheet.getRow(rowNumber);
			Cell tcIDFromMapSheet = row.getCell(0);
			Cell tcSheetNameFromMapSheet = row.getCell(2);
			// LogSysOut(log,"INFO","Scenario name to match in datasheet: "+sc.getName());
			if (tcIDFromMapSheet.getStringCellValue().trim().equalsIgnoreCase(testCaseID.trim())) {
				// scenarionNo = (int) sheet.getRow(rowNumber).getCell(1).getNumericCellValue();
				LogSysOut(log, "INFO", "TCMap cell value for tc id: " + tcIDFromMapSheet.getStringCellValue());
				sheetName = stringCellValueTCMAP(tcSheetNameFromMapSheet);
				LogSysOut(log, "INFO", "DataSheet name is: " + sheetName);
				break;
			}

		}

		if (existSheetName(workbook, sheetName)) {

			XSSFSheet sheet1 = workbook.getSheet(sheetName);
			tcData = data(sheet1);
		}
		workbook.close();
		fis.close();
		System.out.println("getDataFromSheet Hasmapping : " + tcData);
		return tcData;
		// return dataMap;
	}

	public void moveToArchiveExecutionSummary() {
		System.out.println("*****START method moveToArchiveExecutionSummary()******");
		try {
			File summaryReport = new File(Config.REPORT_EXECUTIONSUMMARYREPORT_PATH);
			System.out.println("Summary report excel if already exist : " + summaryReport.getPath());
			if (summaryReport.exists()) {
				File archiveDir = new File(summaryReport.getParent() + "/archives");
				archiveDir.mkdir();
				System.out.println("Archive folder created if not exsit: " + archiveDir.getPath());
				// System.out.println(summaryReport.lastModified());
				String timeStamp = new SimpleDateFormat("MMddyy_HHmmss").format(new Date(summaryReport.lastModified()));
				summaryReport
						.renameTo(new File(archiveDir.getPath() + "/" + timeStamp + "_" + summaryReport.getName()));
				// System.out.println(summaryReport.getPath());
				summaryReport.delete();
			}
		} catch (Exception e) {
			System.out.println(e.getCause().toString());
			e.printStackTrace();
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("*****END method moveToArchiveExecutionSummary()******");
	}

	public void createExecutionSummaryTemplate() throws IOException {
		System.out.println("*****START method createExecutionSummaryTemplate() - Override if exist******");

		// File newSummaryReport = new File(Config.REPORT_EXECUTIONSUMMARYREPORT_PATH);

		// if (!newSummaryReport.exists()) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(Config.REPORT_SHEET_NAME);
		String testDate = Config.REPORT_HEADER_TESTDATE; // getConfig().getProperty("report.header.testDate");
		String osName = Config.REPORT_HEADER_OS;// getConfig().getProperty("report.header.OS");
		String hostName = Config.REPORT_HEADER_HOSTNAME;// getConfig().getProperty("report.header.hostName");
		String application = Config.REPORT_HEADER_APPLICATION;// getConfig().getProperty("report.header.application");
		String featureName = Config.REPORT_HEADER_FEATURENAME;// getConfig().getProperty("report.header.featureName");
		String scenarioName = Config.REPORT_HEADER_SCENARIONAME;// getConfig().getProperty("report.header.scenarioName");
		String scenarioID = Config.REPORT_HEADER_SCENARIOID;// getConfig().getProperty("report.header.scenarioID");
		String scenarioStatus = Config.REPORT_HEADER_SCENATIOSTATUS;// getConfig().getProperty("report.header.scenarioStatus");
		String reason = Config.REPORT_HEADER_REASON;// getConfig().getProperty("report.header.reason");
		String excelReportPath = Config.REPORT_HEADER_EXECUTIONSUMMARY;// getConfig().getProperty("report.header.executionSummary");
		String htmlReportPath = Config.REPORT_HEADER_HTMLREPORT;// getConfig().getProperty("report.header.HTMLReport");
		String scenarioDuration = Config.REPORT_HEADER_SCENARIODURATION;// getConfig().getProperty("report.header.scenarioDuration");

		String[] columns = { testDate, osName, hostName, application, featureName, scenarioName, scenarioID,
				scenarioStatus, reason, excelReportPath, htmlReportPath, scenarioDuration };
		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setWrapText(true);
		headerCellStyle.setAlignment(HorizontalAlignment.LEFT);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		// Row headerRow = sheet.createRow(0);
		// Create a Row

		Row headerRow1 = sheet.createRow(0);

		for (int i = 0; i < columns.length; i++) {

			Cell cell = headerRow1.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
			// sheet.autoSizeColumn(i);
			sheet.setDefaultColumnWidth(20);
		}
		FileOutputStream fos = new FileOutputStream(new File(Config.REPORT_EXECUTIONSUMMARYREPORT_PATH));
		workbook.write(fos);
		fos.close();
		// Closing the workbook
		workbook.close();
		System.out.println("Summary excel is created");
		// }
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void updateResultInExecutionSummaryReport(XSSFSheet sheet, String[] resultData) throws IOException {
		// FileInputStream fis = new FileInputStream(ExcelReportPath);
		// XSSFWorkbook workbook = new XSSFWorkbook(fis);
		// Sheet sheet = workbook.getSheetAt(0);
		// CreationHelper createHelper = workbook.getCreationHelper();
		// Create Cell Style for formatting Date
		// CellStyle dateCellStyle = workbook.createCellStyle();
		// dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/MM/dd
		// HH:mm:ss"));
		int rowCount = sheet.getLastRowNum();
		Row row = sheet.createRow(++rowCount);
		for (int i = 0; i < resultData.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(resultData[i]);
			cell.getCellStyle().setWrapText(true);
			// sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, 20);
		}
	}

	public void updateResultInExecutionSummaryReport(File ExcelReportPath, String[] resultData) throws IOException {
		FileInputStream fis = new FileInputStream(ExcelReportPath);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);

		Sheet sheet = workbook.getSheetAt(0);

		CreationHelper createHelper = workbook.getCreationHelper();
		// Create Cell Style for formatting Date
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/MM/dd HH:mm:ss"));
		int rowCount = sheet.getLastRowNum();

		Row row = sheet.createRow(++rowCount);

		for (int i = 0; i < resultData.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(resultData[i]);
			sheet.autoSizeColumn(i);
		}

		// Write the output to a file
		fis.close();
		FileOutputStream fos = new FileOutputStream(ExcelReportPath);
		workbook.write(fos);
		fos.close();

		// Closing the workbook
		workbook.close();
	}

	public void updateResultInExecutionSummaryReport(File ExcelReportPath, ArrayList<String[]> resultData)
			throws IOException {
		FileInputStream fis = new FileInputStream(ExcelReportPath);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);

		Sheet sheet = workbook.getSheetAt(0);

		CreationHelper createHelper = workbook.getCreationHelper();
		// Create Cell Style for formatting Date
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/MM/dd HH:mm:ss"));
		// int rowCount = sheet.getLastRowNum();
		int rowCount = sheet.getLastRowNum() + 1;
		// Row row = sheet.createRow(++rowCount);
		for (int a = 0; a < resultData.size(); a++) {
			// int rowCount = sheet.getLastRowNum()+1;
			Row row = sheet.createRow(rowCount);
			for (int i = 0; i < resultData.get(a).length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(resultData.get(a)[i]);
				sheet.autoSizeColumn(i);
			}
			rowCount++;
		}

		// Write the output to a file
		fis.close();
		FileOutputStream fos = new FileOutputStream(ExcelReportPath);
		workbook.write(fos);
		fos.close();

		// Closing the workbook
		workbook.close();
	}

	public static List<HashMap<String, String>> data(XSSFSheet sheet) {
		List<HashMap<String, String>> mydata = new ArrayList<>();
		String currentValuInString = "";
		try {

			Row HeaderRow = sheet.getRow(0);
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				Row currentRow = sheet.getRow(i);
				HashMap<String, String> currentHash = new HashMap<String, String>();
				for (int j = 0; j < currentRow.getPhysicalNumberOfCells(); j++) {
					Cell currentCell = currentRow.getCell(j);
					currentValuInString = stringCellValue(currentCell);
					currentHash.put(HeaderRow.getCell(j).getStringCellValue(), currentValuInString);
				}
				mydata.add(currentHash);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Initial Mappings are: " + mydata);
		return mydata;
	}

	public static List<HashMap<String, String>> getDataFromSheetWithFilter(XSSFSheet sheet, int filterColNum,
			String filterValue) {
		List<HashMap<String, String>> mydata = new ArrayList<>();
		String currentValuInString = "";
		try {

			Row HeaderRow = sheet.getRow(0);
			// HashMap<String,String> currentHash = new HashMap<String,String>();
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {

				Row currentRow = sheet.getRow(i);
				// HashMap<String,String> currentHash = new HashMap<String,String>();
				System.out.println("filtervalue: " + currentRow.getCell(filterColNum));

				if (currentRow.getCell(filterColNum).toString().trim().equalsIgnoreCase(filterValue.trim())) {
					HashMap<String, String> currentHash = new HashMap<String, String>();
					for (int j = 0; j < currentRow.getPhysicalNumberOfCells(); j++) {
						Cell currentCell = currentRow.getCell(j);
						currentValuInString = stringCellValue(currentCell);
						System.out.println("hasmap header: " + HeaderRow.getCell(j).getStringCellValue() + "  value: "
								+ currentValuInString);
						currentHash.put(HeaderRow.getCell(j).getStringCellValue(), currentValuInString);
					}
					mydata.add(currentHash);
				}
				// mydata.add(currentHash);
			}
			// mydata.add(currentHash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("getDataFromSheetWithFilter Mappings are: " + mydata);
		return mydata;
	}

	public static List<HashMap<String, String>> getDataFromSheetWithFilter(XSSFSheet sheet, String filterColName,
			String filterValue) {
		List<HashMap<String, String>> mydata = new ArrayList<>();
		String currentValuInString = "";
		try {
			int filterColNum = 0;
			Row HeaderRow = sheet.getRow(0);
			for (int k = 0; k < HeaderRow.getPhysicalNumberOfCells(); k++) {
				if (HeaderRow.getCell(k).toString().trim().equalsIgnoreCase(filterColName.trim())) {
					filterColNum = k;
					break;
				}

			}
			// HashMap<String,String> currentHash = new HashMap<String,String>();
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {

				Row currentRow = sheet.getRow(i);
				// HashMap<String,String> currentHash = new HashMap<String,String>();
				System.out.println("filtervalue: " + currentRow.getCell(filterColNum));

				if (currentRow.getCell(filterColNum).toString().trim().equalsIgnoreCase(filterValue.trim())) {
					HashMap<String, String> currentHash = new HashMap<String, String>();
					for (int j = 0; j < currentRow.getPhysicalNumberOfCells(); j++) {
						Cell currentCell = currentRow.getCell(j);
						currentValuInString = stringCellValue(currentCell);
						System.out.println("hasmap header: " + HeaderRow.getCell(j).getStringCellValue() + "  value: "
								+ currentValuInString);
						currentHash.put(HeaderRow.getCell(j).getStringCellValue(), currentValuInString);
					}
					mydata.add(currentHash);
				}
				// mydata.add(currentHash);
			}
			// mydata.add(currentHash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("getDataFromSheetWithFilter Mappings are: " + mydata);
		return mydata;
	}

	public Object[][] getIterativeRunData() throws IOException, InvalidFormatException {
		ArrayList al;
		Object[][] runData = null;
		String yesRun = "";
		int xRun = 0;
		List<HashMap<String, String>> selectedTCData = new ArrayList<>();
		XSSFWorkbook tPworkbook = new XSSFWorkbook(new File(Config.TESTPLAN_FILE_PATH));
		XSSFSheet tcRunsheet = tPworkbook.getSheet(Config.RUNSHEET_FROM_TESTPLAN);// getSheetAt(0);
		// selectedTCData=getDataFromSheetWithFilter(tcRunsheet,3,"yes");
		selectedTCData = getDataFromSheetWithFilter(tcRunsheet, "SelectToRun", "yes");
		xRun = selectedTCData.size();
		System.out.println("selectedTCData count: " + xRun + " " + selectedTCData.get(0).get("Test/ScenarioID"));
		int arrayLimit = 0;
		for (int x = 0; x < xRun; x++) {
			int a;
			if (getDataFromSheet(selectedTCData.get(x).get("Test/ScenarioID")) == null) {
				a = 1;
			} else {
				a = getDataFromSheet(selectedTCData.get(x).get("Test/ScenarioID")).size();
			}

			arrayLimit = arrayLimit + a;
		}
		System.out.println("Array limit: " + arrayLimit);
		runData = new Object[arrayLimit][5];

		int iteration = 0;
		for (int x = 0; x < xRun; x++) {
			System.out.println("getData from hashmap: " + x + " " + selectedTCData.get(x).get("Test/ScenarioID"));
			List<HashMap<String, String>> testData = getDataFromSheet(selectedTCData.get(x).get("Test/ScenarioID"));
			// System.out.println("getData from hashmap: "+x+"
			// "+selectedTCData.get(x).get("Test/ScenarioID"));
			// List<HashMap<String, String>> testData=getDataFromSheet("JUSATC2");
			if (testData == null) {
				runData[iteration][0] = selectedTCData.get(x).get("Test/ScenarioID");
				runData[iteration][1] = selectedTCData.get(x).get("Test Method Name");
				runData[iteration][2] = selectedTCData.get(x).get("Manual Test Case Ref/Detail");
				runData[iteration][3] = null;
				runData[iteration][4] = 0;
				iteration++;
			} else {
				int a = testData.size();
				System.out.println("testdata loop: " + a);
				for (int y = 0; y < a; y++) {
					runData[iteration][0] = selectedTCData.get(x).get("Test/ScenarioID");
					// System.out.println("iteration data: " + runData[iteration][0]);
					runData[iteration][1] = selectedTCData.get(x).get("Test Method Name");
					runData[iteration][2] = selectedTCData.get(x).get("Manual Test Case Ref/Detail");
					runData[iteration][3] = testData;
					runData[iteration][4] = y;
					iteration++;
				}
			}
		}

		return runData;
	}

	public static List<String> getAlldataFromColumn(String ExcelPath, String sheetName, String filter)
			throws Exception {

		XSSFWorkbook workbook = new XSSFWorkbook(new File(ExcelPath));
		Sheet sheet = workbook.getSheet(sheetName);// getSheetAt(0);
		List<String> columnData = new ArrayList<String>();
		String isSelectToRun = "";
		int filterRowIndex = -1;
		//for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++)
			for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {

                if (stringCellValue(sheet.getRow(0).getCell(i)).trim( ).equalsIgnoreCase(filter)) {
				filterRowIndex = i;
				break;
			 }
			/*else {
				throw new Exception("No matching found in test plan for the column  " + filterRowIndex);
			}*/
		}
		System.out.println("Column of title " + filter + " at the column " + filterRowIndex);
		try {
			int rowsCount = sheet.getPhysicalNumberOfRows();

			int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
			System.out.println("Row count: " + rowsCount + " Column Count :" + columnCount);
			for (int i = 1; i < rowsCount; i++) {
				Row currentRow = sheet.getRow(i);
				isSelectToRun = stringCellValue(currentRow.getCell(filterRowIndex));
				if (isSelectToRun.equalsIgnoreCase("Yes")) {
					columnData.add(stringCellValue(currentRow.getCell(0)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columnData;
	}

	private boolean existSheetName(XSSFWorkbook workbook, String sheetname) {
		boolean sheetExist = false;
		if (workbook.getNumberOfSheets() != 0) {
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				if (workbook.getSheetName(i).equals(sheetname)) {
					sheetExist = true;
					break;
				}
			}
			if (!sheetExist) {
				LogSysOut(log, "WARN", "Data sheet NOT Required OR There is >> NO sheets with name : " + sheetname
						+ " in the workbook : " + workbook);
			}
		} else {
			LogSysOut(log, "WARN", "0 sheets in the workbook : " + workbook);
		}
		return sheetExist;
	}

	private static String stringCellValueTCMAP(Cell cell) throws IOException {
		String strValueofCell = "";
		strValueofCell = stringCellValue(cell);

		if (strValueofCell.contains(".")) {
			strValueofCell = strValueofCell.substring(0, strValueofCell.indexOf("."));
		}

		return strValueofCell;
	}

	private static String stringCellValue(Cell cell) throws IOException {
		// System.out.println("Read the cell data format and convert and return the
		// value with STRING format **** ");
		String strValueofCell = "";
		if (cell == null) {
			strValueofCell = "";
		}
		// LogSysOut(log,"INFO","Cell value before convert: "+cell.toString());
		CellType cellType = cell.getCellTypeEnum();
		// LogSysOut(log,"INFO","Cell Type: "+cellType);

		switch (cellType) {
		case BLANK:
			strValueofCell = "";
			break;
		case BOOLEAN:
			strValueofCell = Boolean.toString(cell.getBooleanCellValue());
			break;
		case ERROR:
			strValueofCell = "*error*";
			break;
		case NUMERIC:
			strValueofCell = String.valueOf(cell.getNumericCellValue());
			break;
		case STRING:
			strValueofCell = cell.getStringCellValue();
			break;
		default:
			strValueofCell = cell.toString();
			break;
		}
		// LogSysOut(log,"INFO","Cell value After convert: "+strValueofCell);
		return strValueofCell;
	}

	public static String getTagsFromTestPlan(String testPlanPath, String sheetName)
			throws Exception {
		List<String> runTagsFromTestPlan;
		String tagString = "";
		runTagsFromTestPlan = getAlldataFromColumn(testPlanPath, sheetName, "SelectToRun");
		String prefix = "--tags ";
		int countTag = runTagsFromTestPlan.size();
		String temp = "";
		if (countTag == 1) {
			tagString = "@" + runTagsFromTestPlan.get(0);
		} else {
			for (int i = 0; i < countTag; i++) {
				if (i != (countTag - 1))
					tagString = tagString + "@" + runTagsFromTestPlan.get(i) + ",";
				else
					tagString = tagString + "@" + runTagsFromTestPlan.get(i);

			}
		}
		tagString = prefix + tagString;
		return tagString;
	}

	public static void takeScreenshot(WebDriver driver) throws Exception {
		String timeStamp;
		File screenShotName;
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//The below method will save the screen shot in d drive with name "screenshot.png"
		timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		screenShotName = new File(Config.SCREENSHOTS_PATH + "/" + timeStamp + ".png");
		FileUtils.copyFile(scrFile, screenShotName);

		String filePath = screenShotName.getAbsolutePath();
		String path = "<img src=\"file://" + filePath + "\" alt=\"\"/>";
		Reporter.log(path);

	}

	public static String takeScreenshot(WebDriver driver, String imageName) {
		try {
			String timeStamp;
			File screenShotName;
			String fileName;
			String basdDir = System.getProperty("user.dir");
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			fileName = basdDir + "/" + Config.SCREENSHOTS_PATH + "/" + imageName + "_" + timeStamp + ".png";
			screenShotName = new File(fileName);
			FileUtils.copyFile(scrFile, screenShotName);
			return fileName;
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	public static String takeScreenshotBase64(WebDriver driver) {
		try {
			String base64Screenshot = "data:image/png;base64,"
					+ ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
			return base64Screenshot;
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	/*
	 * public static String takeScreenshotHTMLReport(ExtentTest htmlLogger,WebDriver
	 * driver){ //DateFormat dateFormatScreenshot = new
	 * SimpleDateFormat("MMddyy_HH_mm_ss"); //return
	 * htmlLogger.addScreenCapture(Common.takeScreenshot(Cases.driver,
	 * TestCaseDriver.testCaseID+"_"+TestCaseDriver.testCaseName+
	 * dateFormatScreenshot.format(new Date()))); return
	 * htmlLogger.addScreenCapture(Common.takeScreenshot(driver,
	 * TestCaseDriver.testCaseID+"_"+TestCaseDriver.testCaseName));
	 * 
	 * }
	 */

	public void cleanSheet(Sheet sheet, int startRowNumberToDelete) {
		int numberOfRows = sheet.getPhysicalNumberOfRows();
		System.out.println("StartRowToDelete " + startRowNumberToDelete + " User Rows count: " + numberOfRows);

		if (numberOfRows >= startRowNumberToDelete) {
			for (int i = startRowNumberToDelete; i <= sheet.getLastRowNum(); i++) {
				if (sheet.getRow(i) != null) {
					sheet.removeRow(sheet.getRow(i));
				} else {
					System.out.println("Info: clean sheet='" + sheet.getSheetName() + "' ... skip line: " + i);
				}
			}
		} else {
			System.out.println("Info: clean sheet='" + sheet.getSheetName() + "' ... is empty");
		}
	}

	public static String statusMapforCI(String status) {
		String tempStatus = status.trim().toUpperCase();
		if (tempStatus.startsWith("PASS")) {
			tempStatus = "PASS";
		} else if (tempStatus.startsWith("SUCCESS")) {
			tempStatus = "PASS";
		} else if (tempStatus.startsWith("TRUE")) {
			tempStatus = "PASS";
		} else if (tempStatus.startsWith("FAIL")) {
			tempStatus = "FAIL";
		} else if (tempStatus.startsWith("FALSE")) {
			tempStatus = "FAIL";
		} else if (tempStatus.startsWith("PENDING")) {
			tempStatus = "FAIL";
		} else if (tempStatus.startsWith("UNDEFINED")) {
			tempStatus = "FAIL";
		} else if (tempStatus.startsWith("SKIP")) {
			tempStatus = "SKIP";
		} else {
			tempStatus = "NOT EXECUTED";
		}
		return tempStatus;
	}
	
	public String randomStringSimple(int length) {
	    byte[] byteArray = new byte[length];
	    Random random = new Random();
	    random.nextBytes(byteArray);
	   // random.s
	 
	    return new String(byteArray, Charset.forName("UTF-8"));
	}
	
	public  String randomAlphanumericString(int length) {
	    String alphanumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuv";
	 
	    StringBuffer randomString = new StringBuffer(length);
	    Random random = new Random();
	 
	    for (int i = 0; i < length; i++) {
	        int randomIndex = random.nextInt(alphanumericCharacters.length());
	        char randomChar = alphanumericCharacters.charAt(randomIndex);
	        randomString.append(randomChar);
	    }
	 
	    return randomString.toString();
	}

	

}
