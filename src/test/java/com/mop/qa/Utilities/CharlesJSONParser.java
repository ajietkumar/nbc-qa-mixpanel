/**
 * 
 */
package com.mop.qa.Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Pattern;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * @author 259517
 *
 */
public class CharlesJSONParser {
	public static String pageUrl = "Ad Sales Automation Execution_";
	public static String temp;
	public static String executionReportName, summaryReport;
	public static String strFilePath;
	public static BufferedWriter writer;
	public static String reportName;
	// public static ExtentReports logger;
	// public static ExtentTest testLogger;
	public static String time, date;
	public static String assetId, deviceDetails, videoDuration, googleAdID, deviceType, optOut;
	public static File summaryFile;
	public static PrintStream out;

	public static String pathCreator(String appBrand) {
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal1 = Calendar.getInstance();
		date = dateFormat1.format(cal1.getTime()).toString().replace("/", "_");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		time = dateFormat.format(cal.getTime()).toString().replace("/", "_").replace(":", "_");
		setReportPath(appBrand);
		try {
			// temp = strFilePath+new URL(pageUrl.toString()).getHost()+new
			// URL(pageUrl.toString()).getPath().replace("/", "_")+time;
			executionReportName = strFilePath + pageUrl.replace(" ", "_") + time;
			summaryReport = strFilePath + pageUrl.replace(" ", "_") + "_Summary_" + time;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return executionReportName;
	}

	private static void setReportPath(String appBrand) {
		try {
			String sep = Pattern.quote(File.separator);
			String path = System.getProperty("user.dir");
			String[] strs = path.split("Users");
			String userName = strs[1].trim().split(sep)[1];
			String os = System.getProperty("os.name");
			if (os.contains("Windows"))
				strFilePath = "C:/Users/" + userName + "/Dropbox (NBCUniversal)/Cognizant (New)/Automation/ExecutionResults/" + appBrand + "/" + date + "/";
			else
				strFilePath = "/Users/" + userName + "/Dropbox (NBCUniversal)/Cognizant (New)/Automation/ExecutionResults/" + appBrand + "/" + date + "/";
		} catch (Exception e) {
			strFilePath = System.getProperty("user.dir") + "/reports/" + date + "/";
		}
		new File(strFilePath).mkdirs();
	}

	public static void renameReports(String app_Type) {
		File source = new File(reportName);
		File sourceSummary = new File(summaryReport);
		String destFileName = strFilePath + CharlesIntegration.brand.replaceAll(" ", "") + "_" + CharlesIntegration.platform + "_" + CharlesIntegration.deviceName.replaceAll(" ", "") + "_" + CharlesIntegration.episodeType + "_" + app_Type + "_" + time + ".html";
		String destFileSummary = strFilePath + CharlesIntegration.brand.replaceAll(" ", "") + "_" + CharlesIntegration.platform + "_" + CharlesIntegration.deviceName.replaceAll(" ", "") + "_Summary_" + app_Type + time + ".html";
		File destination = new File(destFileName);
		File destSummary = new File(destFileSummary);
		source.renameTo(destination);
		sourceSummary.renameTo(destSummary);
		reportName = destFileName;
	}

	public static void initTestReporter(String appBrand) {
		reportName = pathCreator(appBrand) + ".html";
		summaryReport = summaryReport + ".html";
		summaryFile = new File(summaryReport);
		try {
			out = new PrintStream(summaryFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// logger = new ExtentReports(reportName);
	}

	public static void testName(String TestName) {
		// testLogger = logger.startTest(TestName);
	}

	public static void updateSummaryReport(String testCaseId, String testDesc, String priority, String status) {
		if (status.equalsIgnoreCase("pass"))
			out.println("<tr><td>" + testCaseId + "</td><td>" + testDesc + "</td><td>" + priority + "</td><td><b><font color='green'>" + status + "</font></b></td></tr>");
		else
			out.println("<tr><td>" + testCaseId + "</td><td>" + testDesc + "</td><td>" + priority + "</td><td><b><font color='red'>" + status + "</font></b></td></tr>");
	}

	public static void closeReportSummary() {
		out.println("</table></body></html>");
		out.close();
	}

	public static File getTheNewestFile(String filePath, String ext) {
		File theNewestFile = null;
		File dir = new File(filePath);
		FileFilter fileFilter = new WildcardFileFilter("*." + ext);
		File[] files = dir.listFiles(fileFilter);
		if (files.length > 0) {
			/** The newest file comes first **/
			Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
			theNewestFile = files[0];
		}
		return theNewestFile;
	}

	public static String getTheCharlesSessionFile(String filePath, String ext) {
		File theNewestFile = null;
		File dir = new File(filePath);
		FileFilter fileFilter = new WildcardFileFilter("*." + ext);
		File[] files = dir.listFiles(fileFilter);
		String charlesSessionPath = "";
		if (files.length > 0) {
			/** The newest file comes first **/
			Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
			theNewestFile = files[0];
			charlesSessionPath = theNewestFile.getPath();
		}
		return charlesSessionPath;
	}
}
