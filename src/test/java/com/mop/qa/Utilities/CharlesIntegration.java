/**
 * 
 */
package com.mop.qa.Utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mop.qa.testbase.TestBase;

import io.appium.java_client.AppiumDriver;

/**
 * @author 259517
 *
 */
public class CharlesIntegration extends CharlesJSONParser {

	protected static String showDetails;
	static String webBrowser;
	protected static AppiumDriver driver;
	public static int count = 0;
	protected static WebDriver wd;// =
									// initializeDriver(WebBrowsers.getBrowser(webBrowser));
	static String testCaseID;
	public static String testCasePriority;
	static File processName = new File("/Applications/Charles.app");
	static File f = new File(
			"C:\\Users\\259517\\Downloads\\charlesProcessKill.bat");
	public static Process process;
	public static String brand,
			deviceName,
			platform,
			episodeType,
			appActivity = "com.nbcu.tve.client.mobile.home.SplashScreenActivity";
	public static int slotImpressionCount = 0;

	// static WebDriverWait wait = new WebDriverWait(driver, 30);

	@SuppressWarnings("deprecation")
	public static void harCreator() throws IOException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String time = dateFormat.format(cal.getTime()).toString()
				.replace("/", "_").replace(":", "_");
		temp = strFilePath + new URL(pageUrl.toString()).getHost()
				+ new URL(pageUrl.toString()).getPath().replace("/", "_")
				+ time;
		System.out.println("reached end");
	}

	public static void updateSummaryHeader() {
		if (count == 0) {
			SimpleDateFormat sdf = new SimpleDateFormat(
					"EEE, MMM d,yyyy HH:mm:ss z");
			Date date = new Date(System.currentTimeMillis());
			out.println("<html><body><font color='blue'><h3>Device Name - "
					+ CharlesIntegration.deviceName
					+ "<br>App Name - "
					+ CharlesIntegration.brand
					+ "<br>Device OS Details - "
					+ CharlesIntegration.deviceDetails.split(";")[0].trim()
					+ "<br>Executed on "
					+ sdf.format(date)
					+ "</h3></font><table border=\"1\"><tr><th><b>TestCase ID</b></th><th><b>TestCase Description</b></th><th><b>Priority</b></th><th><b>Status</b></th></tr>");
			count++;
		}
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

	public static void triggerCharles() {

		CharlesController
				.startRecording("http://control.charles/recording/start");
		forcedWait(2000);
		CharlesController
				.startRecording("http://control.charles/session/clear");
		forcedWait(2000);
	}

	public static void closeCharles(String filename) {

		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String time = dateFormat.format(cal.getTime()).toString()
					.replace("/", "_").replace(":", "_");
			CharlesController.IEDownloader(
					"http://control.charles/session/export-xml", time + "_"
							+ filename + ".xml");
			forcedWait(5000);
			CharlesController.IEDownloader(
					"http://control.charles/session/download", time + "_"
							+ filename + ".chls");

			forcedWait(5000);
			CharlesController
					.startRecording("http://control.charles/session/clear");
			forcedWait(2000);

			CharlesController
					.startRecording("http://control.charles/recording/stop");
			forcedWait(2000);

		} catch (Exception e) {
		}
	}

	public static void closeCharlesCSV(String filename) {

		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String time = dateFormat.format(cal.getTime()).toString()
					.replace("/", "_").replace(":", "_");
			CharlesController.IEDownloader(
					"http://control.charles/session/export-xml", time + "_"
							+ filename + ".xml");
			forcedWait(5000);
			CharlesController.IEDownloader(
					"http://control.charles/session/export-csv", time + "_"
							+ filename + ".csv");
			forcedWait(5000);
			TestBase.downloadedHarFile += "CharlesSesions/";
			CharlesController.IEDownloader(
					"http://control.charles/session/download", time + "_"
							+ filename + ".chls");

			forcedWait(5000);
			CharlesController
					.startRecording("http://control.charles/session/clear");
			forcedWait(2000);

			CharlesController
					.startRecording("http://control.charles/recording/stop");
			forcedWait(2000);

		} catch (Exception e) {
		}
	}

	@SuppressWarnings("unused")
	protected static WebElement getWebElement(WebDriver driver, By by) {

		WebElement element;
		(new WebDriverWait(driver, 60)).until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(by));
		return driver.findElement(by);
	}

	public static WebElement randomVideoSelect(String selector) {

		WebElement textAtRangeIndex = null;

		try {

			WebElement divTag = getWebElement(wd, By.cssSelector(selector));
			List<WebElement> allAnchorTags = divTag.findElements(By
					.tagName("a"));

			Random rn = new Random();
			int range = rn.nextInt(allAnchorTags.size());
			// = rn.nextInt(allAnchorTags.size());

			WebElement textAtRange = allAnchorTags.get(range);
			System.out.println("Selected Video: "
					+ textAtRange.getText().toString());
			textAtRangeIndex = textAtRange;
		} catch (ElementNotVisibleException e) {

			wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			e.printStackTrace();
		}

		return textAtRangeIndex;
	}

	public static List<WebElement> randomVideoSelectForShortVideos(
			String selector) {

		WebElement textAtRangeIndex = null;
		List<WebElement> allWebElements = new ArrayList<WebElement>();

		try {

			WebElement divTag = getWebElement(wd, By.cssSelector(selector));
			WebElement innerDivTag = divTag.findElement(By.tagName("div"));
			List<WebElement> allAnchorTags = innerDivTag.findElements(By
					.tagName("a"));

			Random rn = new Random();
			int range = rn.nextInt(allAnchorTags.size());

			WebElement textAtRange = allAnchorTags.get(range);
			List<WebElement> nameOfShowDivTags = textAtRange.findElements(By
					.tagName("div"));
			WebElement nameOfShow = nameOfShowDivTags.get(2);
			WebElement subTitleOfShow = nameOfShowDivTags.get(3);
			System.out.println("Selected Video: "
					+ nameOfShow.getText().toString());
			System.out.println("Title: " + subTitleOfShow.getText().toString());
			textAtRangeIndex = textAtRange;

			allWebElements.add(textAtRangeIndex);
			allWebElements.add(nameOfShow);
			allWebElements.add(subTitleOfShow);
		} catch (ElementNotVisibleException e) {

			wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			e.printStackTrace();
		}

		return allWebElements;
	}

	public static void startCharlesProcess() {

		try {
			Desktop.getDesktop().open(processName);
			Thread.sleep(10000);

		} catch (Exception e) {

			new RuntimeException();
		}
	}

	public static String setPackageName() {
		String packageName = null;
		return packageName;
	}

	public static void stopCharlesProcess() {

		try {
			Runtime.getRuntime().exec("cmd /c start" + f);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public static void killDriverServer() {

		if (webBrowser.equalsIgnoreCase("IE")) {
			try {
				Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (webBrowser.equalsIgnoreCase("Chrome")) {

			try {
				Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static void forcedWait(int wait) {

		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	public static boolean isElementExists(By by) {
		boolean isExists = true;
		try {
			Thread.sleep(5000);
			wd.findElement(by);
		} catch (NoSuchElementException e) {
			isExists = false;
		} catch (ElementNotVisibleException e) {
			isExists = false;

		}

		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isExists;
	}

	public static boolean isElementVisible(By by) {
		boolean isExists = true;
		try {
			Thread.sleep(5000);
			wd.findElement(by).click();
		} catch (NoSuchElementException e) {
			isExists = false;
		} catch (ElementNotVisibleException e) {
			isExists = false;

		}

		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isExists;
	}

	public static int randomNumber(List<WebElement> listOfWebElements) {

		Random randomNumber = new Random();
		int index = randomNumber.nextInt(listOfWebElements.size());

		if (index == 0) {

			index = 2;
		}

		return index;

	}

	public static void enterOptimumUserCredentials(String testId) {

		WebElement optimumId = getWebElement(wd, By.cssSelector("#IDToken1"));
		// optimumId.sendKeys(DataReader.get("User ID", testId));
		WebElement password = getWebElement(wd, By.cssSelector("#IDToken2"));
		// password.sendKeys(DataReader.get("Password", testId));
		WebElement signIn = getWebElement(wd, By.cssSelector("#signin_button"));
		signIn.click();
	}
}
