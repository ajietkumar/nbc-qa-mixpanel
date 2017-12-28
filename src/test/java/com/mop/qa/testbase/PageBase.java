package com.mop.qa.testbase;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.NetworkConnectionSetting;
import io.appium.java_client.ScrollsTo;
import io.appium.java_client.TouchAction;
import io.appium.java_client.TouchShortcuts;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.ios.IOSDriver;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.bcel.generic.Select;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.mop.qa.Utilities.CharlesIntegration;
import com.mop.qa.Utilities.ReportGenerator;
import com.mop.qa.testrunner.TestRunnerold;
import com.relevantcodes.extentreports.LogStatus;

public class PageBase extends TestBase {
	protected static RemoteWebDriver remoteDriver;
	protected static AppiumDriver appiumDriver;
	protected static String toolName;
	public TouchAction touch;
	protected final String XPATH = "xpath=";
	protected final String ID = "id=";
	protected final String NAME = "name=";
	protected final String LINKTEXT = "link=";
	protected final String PARTLINKTEXT = "partlink=";
	protected final String COORDINATES = "coordinates=";
	public static WebDriver driver;

	public PageBase(AppiumDriver driver) {
		this.appiumDriver = driver;
		PageFactory.initElements(appiumDriver, this);
		this.touch = new TouchAction((MobileDriver) driver);
		toolName = "Appium";
	}

	public PageBase(RemoteWebDriver driver) {
		this.remoteDriver = driver;
		PageFactory.initElements(remoteDriver, this);
		toolName = "Selenium";
	}

	public PageBase() {
		try {
			if (getAppProperties("tool").equalsIgnoreCase("selenium")) {
				this.remoteDriver = TestBase.remoteDriver;
				PageFactory.initElements(remoteDriver, this);
				toolName = "Selenium";
			} else if (getAppProperties("tool").equalsIgnoreCase("appium")) {
				this.appiumDriver = TestBase.appiumDriver;
				PageFactory.initElements(appiumDriver, this);
				toolName = "Appium";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int count3 = 1, imagewidth1, imageheight1, imagewidth2, imageheight2;
	public static int n = 0;
	public String text1 = null;
	public ReportGenerator rg = new ReportGenerator();
	public String tool = null, appType = null, locality=null,webBrowser = null, chromeDriverPath = null, fireFoxDriverPath = null, IEDriverPath = null, deviceName = null, appName = null, appiumPort = null, deviceVersion = null, appPackage = null, appActivity = null, Android_Appium_Server_Path = null, appiumPort_Ios = null, devicePlatformName_Ios = null, deviceVersion_Ios = null, device_UDID = null, platformName = null, applicationPath = null, appiumURL = null, ParentWinhadleMob = null, ParentWinhadle = null, mobileCloud = null;
	private String charlesRequiered;

	public AppiumDriver launchApp(String udid, String appium_port, String platFormName, Proxy proxy, int port) throws Exception {
		tool = getAppProperties("tool");
		appType = getAppProperties("appType");
		// platformName = getAppProperties("platformName");
		// platformName = TestRunner.platFormName();
		// System.out.println(platformName);
		mobileCloud = getAppProperties("mobileCloud");
		charlesRequiered = getAppProperties("charlesRequiered");
		System.out.println("UDID:" + udid);
		System.out.println("platFormName:" + platFormName);
		if (tool.equalsIgnoreCase("Appium")) {
			if ((platFormName.equalsIgnoreCase("iOS")) && (appType.equalsIgnoreCase("Native"))) {
				deviceVersion_Ios = getAppProperties("deviceVersion_Ios");
				applicationPath = getAppProperties("applicationPath");
				appiumURL = "http://127.0.0.1:" + appium_port + "/wd/hub";
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability(CapabilityType.PROXY, proxy);
				System.out.println("Port started:" + port);
				capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				capabilities.setCapability("appium-version", "1.0");
				capabilities.setCapability("platformName", platFormName);
				capabilities.setCapability("platformVersion", deviceVersion_Ios);
				capabilities.setCapability("deviceName", udid);
				capabilities.setCapability("app", applicationPath);
				capabilities.setCapability("newCommandTimeout", 100000);
				if (appiumDriver == null)
					appiumDriver = null;
				if (charlesRequiered.equalsIgnoreCase("Y")) {
					CharlesIntegration.startCharlesProcess();
					CharlesIntegration.triggerCharles();
				}
				appiumDriver = new IOSDriver(new URL(appiumURL), capabilities);
			} else if ((platFormName.equalsIgnoreCase("Android")) && (appType.equalsIgnoreCase("Native"))) {
				appName = getAppProperties("appName");
				deviceVersion = getAppProperties("deviceVersion");
				appPackage = getAppProperties("appPackage");
				appActivity = getAppProperties("appActivity");
				appiumURL = "http://127.0.0.1:" + appium_port + "/wd/hub";
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability(CapabilityType.PROXY, proxy);
				System.out.println("Port started:" + port);
				capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				capabilities.setCapability("appium-version", "1.0");
				capabilities.setCapability("app", appName);
				capabilities.setCapability("platformName", platformName);
				capabilities.setCapability("platformVersion", deviceVersion);
				capabilities.setCapability("deviceName", udid);
				capabilities.setCapability("appPackage", appPackage);
				capabilities.setCapability("appActivity", appActivity);
				capabilities.setCapability("newCommandTimeout", 100000);
				appiumDriver = new AndroidDriver(new URL(appiumURL), capabilities);
				// }
				appiumDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			} else if ((platFormName.equalsIgnoreCase("Android")) && (appType.equalsIgnoreCase("Web"))) {
				deviceVersion = getAppProperties("deviceVersion");
				deviceName = getAppProperties("deviceName");
				appiumPort = getAppProperties("appiumPort");
				appiumURL = "http://127.0.0.1:" + appiumPort + "/wd/hub";
				// mobile remote
				String host = "mobiletestlab.cognizant.com";
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
				capabilities.setCapability("newCommandTimeout", "300");
				capabilities.setCapability("appium-version", "1.0");
				capabilities.setCapability("platformName", platformName);
				capabilities.setCapability("deviceName", deviceName);
				capabilities.setCapability("platformVersion", deviceVersion);
				// if (appiumDriver == null) {
				appiumDriver = new AndroidDriver(new URL(appiumURL), capabilities);
				// }
				appiumDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			} else if ((platFormName.equalsIgnoreCase("iOS")) && (appType.equalsIgnoreCase("Web"))) {
				;
				deviceVersion = getAppProperties("deviceVersion_Ios");
				deviceName = getAppProperties("device_UDID");
				appiumPort = getAppProperties("appiumPort");
				appiumURL = "http://127.0.0.1:" + appiumPort + "/wd/hub";
				DesiredCapabilities cap = new DesiredCapabilities();
				cap.setCapability("deviceName", "iPhone");
				cap.setCapability("browserName", "Safari");
				cap.setCapability("platformVersion", deviceVersion);
				cap.setCapability("platformName", platformName);
				cap.setCapability("udid", deviceName);
				if (appiumDriver == null) {
					appiumDriver = new IOSDriver(new URL(appiumURL), cap);
				}
				appiumDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			}
		
		}
		return appiumDriver;
	}

	public RemoteWebDriver launchSite(String browser, String locality) throws Exception {
		//browser="chrome";
		if (locality.equalsIgnoreCase("local")) {
			if (browser.equalsIgnoreCase("chrome")) {
				chromeDriverPath = getAppProperties("chromeDriverPath");
				System.setProperty("webdriver.chrome.driver", chromeDriverPath);
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--disable-extensions");
				options.addArguments("--test-type");
				options.addArguments("start-maximized");
				//options.addArguments("--disable-web-security");
				//options.addArguments("--no-proxy-server");
				
				
				Map<String, Object> prefs = new HashMap<String, Object>();
				prefs.put("credentials_enable_service", false);
				prefs.put("profile.password_manager_enabled", false);
				options.setExperimentalOption("prefs", prefs);

				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				
				
				
				remoteDriver = new ChromeDriver(capabilities);
				remoteDriver.manage().window().maximize();
				System.out.println(getAppProperties("URL"));
				remoteDriver.get(getAppProperties("URL"));
				
				
			} else if (browser.equalsIgnoreCase("firefox")) {
				fireFoxDriverPath = getAppProperties("fireFoxDriverPath");
				FirefoxBinary binary = new FirefoxBinary(new File(fireFoxDriverPath));
				remoteDriver = new FirefoxDriver(binary, null);
			} else if (browser.equalsIgnoreCase("IE")) {
				IEDriverPath = getAppProperties("IEDriverPath");
				System.setProperty("webdriver.ie.driver", IEDriverPath);
				remoteDriver = new InternetExplorerDriver();
				remoteDriver.manage().window().maximize();
			}
		} else {
			String mobileCloud = getAppProperties("mobileCloud");
			// Desktop Cloud in QPass Cloud
			if (mobileCloud.equalsIgnoreCase("No")) {
				DesiredCapabilities capabilities = DesiredCapabilities.firefox();
				capabilities.setPlatform(Platform.MAC);
				capabilities.setVersion("41");
				capabilities.setCapability("username", "Vignesh.Parameswari@cognizant.com");
				capabilities.setCapability("password", "VmlnbmVzaC5QYXJhbWVzd2FyaUBjb2duaXphbnQuY29tOg==");
				capabilities.setCapability("packagename", "HMHDemo");
				capabilities.setCapability("servicerequestid", "SR-01302-160624-0000664");
				remoteDriver = new RemoteWebDriver(new URL("http://fastpaasinchnp1.cognizant.com/wd/hub"), capabilities);
			}
			// Mobile Cloud in Perfecto Cloud
			else {
				String mobileHost = getAppProperties("mobileCloudHost");
				DesiredCapabilities capabilities = new DesiredCapabilities("mobileChrome", "", Platform.ANY);
				capabilities.setCapability("user", "Ramya.Santhanam@cognizant.com");
				capabilities.setCapability("password", "Password-1");
				capabilities.setCapability("deviceName", "0728FA70");
				// setExecutionIdCapability(capabilities,
				// "https://mobiletestlab.cognizant.com");
				remoteDriver = new RemoteWebDriver(new URL("https://" + mobileHost + "/nexperience/perfectomobile/wd/hub"), capabilities);
				remoteDriver.get("https://my-review-cert.hrw.com/dashboard/home");
			}
		}
		return remoteDriver;
	}

	public void checkVideoPlaying(String videoPlayerPath) throws Exception {
		Thread.sleep(1000);
		takeScreenshotVideo("Image1", videoPlayerPath);
		Thread.sleep(3000);
		takeScreenshotVideo("Image2", videoPlayerPath);
		String file1 = "VideoComparison\\Image1.png";
		String file2 = "VideoComparison\\Image2.png";
		processImage(file1, file2);
		// remoteDriver.quit();
	}

	public void takeScreenshotVideo(String screenshotName, String videoPlayerPath) {
		try {
			WebElement ele = remoteDriver.findElement(By.xpath(videoPlayerPath));
			if (ele.isDisplayed()) {
				File screen = (File) ((TakesScreenshot) remoteDriver).getScreenshotAs(OutputType.FILE);
				int ImageWidth = ele.getSize().getWidth();
				int ImageHeight = ele.getSize().getHeight();
				Point point = ele.getLocation();
				int xcord = point.getX();
				int ycord = point.getY();
				BufferedImage img = ImageIO.read(screen);
				BufferedImage dest = img.getSubimage(xcord, ycord, ImageWidth, ImageHeight);
				ImageIO.write(dest, "png", screen);
				FileUtils.copyFile(screen, new File("VideoComparison\\" + screenshotName + ".png"));
			}
		} catch (Exception e) {
			try {
				;
				rg.logException("Taking Screenshots Fails", e);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public void switchToDefaultFrame() throws Exception {
        try {
               switch (toolName) {
               case "Appium":
                     Thread.sleep(5000);
                     appiumDriver.switchTo().defaultContent();
                     break;
               case "Selenium":
                     remoteDriver.switchTo().defaultContent();
                     break;
               }
        } catch (org.openqa.selenium.NoSuchWindowException exc) {
               exc.printStackTrace();
        }
 }
 public void switchToFrameXpath(WebElement frameXpath) throws Exception {
        try {
               switch (toolName) {
               case "Appium":
                     Thread.sleep(5000);
                     appiumDriver.switchTo().frame(frameXpath);
                     break;
               case "Selenium":
                     remoteDriver.switchTo().frame(frameXpath);
                     break;
               }
        } catch (org.openqa.selenium.NoSuchWindowException exc) {
               exc.printStackTrace();
        }
        
 }

	public ArrayList<Integer> getResolution(String videoPlayerPath) {
		ArrayList<Integer> size = new ArrayList<Integer>();
		try {
			WebElement ele = remoteDriver.findElement(By.xpath(videoPlayerPath));
			if (ele.isDisplayed()) {
				int ImageWidth = ele.getSize().getWidth();
				int ImageHeight = ele.getSize().getHeight();
				size.add(ImageWidth);
				size.add(ImageHeight);
			}
		} catch (Exception e) {
			try {
				rg.logException("Taking Screenshots Fails", e);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return size;
	}

	public static void waitForAjax(int timeoutInSeconds) {
		try {
			if (remoteDriver instanceof JavascriptExecutor) {
				JavascriptExecutor jsDriver = (JavascriptExecutor) remoteDriver;
				Boolean ajaxCondtn = false;
				for (int i = 0; i < timeoutInSeconds; i++) {
					for (int j = 0; j < 20; j++) {
						try {
							ajaxCondtn = (Boolean) jsDriver.executeScript("return window.jQuery != undefined");
							if (ajaxCondtn)
								break;
							else
								Thread.sleep(1000);
						} catch (Exception e) {
						}
					}
					if (!ajaxCondtn)
						continue;
					Object numberOfAjaxConnections = jsDriver.executeScript("return jQuery.active");
					if (numberOfAjaxConnections instanceof Long) {
						Long n = (Long) numberOfAjaxConnections;
						if (n.longValue() == 0L)
							break;
					}
					Thread.sleep(1000);
				}
			} else {
				System.out.println("Web driver: " + remoteDriver + " cannot execute javascript");
			}
		} catch (InterruptedException e) {
			;
		}
	}

	public void takeScreenshot(String screenshotName, String videoPlayerPath) {
		try {
			WebElement ele = remoteDriver.findElement(By.xpath(videoPlayerPath));
			if (ele.isDisplayed()) {
				File screen = (File) ((TakesScreenshot) remoteDriver).getScreenshotAs(OutputType.FILE);
				int ImageWidth = ele.getSize().getWidth();
				int ImageHeight = ele.getSize().getHeight();
				Point point = ele.getLocation();
				int xcord = point.getX();
				int ycord = point.getY();
				BufferedImage img = ImageIO.read(screen);
				BufferedImage dest = img.getSubimage(xcord, ycord, ImageWidth, ImageHeight);
				ImageIO.write(dest, "png", screen);
				FileUtils.copyFile(screen, new File("PhotoPassScreenshots\\" + screenshotName + ".png"));
				rg.passTestCase(screenshotName + " Image is Captured Successfully");
			}
		} catch (Exception e) {
			try {
				rg.logException("Taking Screenshots Fails", e);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public void saveImage(String screenshotName, String videoPlayerPath) throws Exception {
		String s1 = remoteDriver.findElement(By.xpath(videoPlayerPath)).getAttribute("src");
		URL url1 = new URL(s1);
		RenderedImage image1 = ImageIO.read(url1);
		ImageIO.write(image1, "png", new File("PhotoPassScreenshots\\" + screenshotName + ".png"));
		rg.passTestCase(screenshotName + " Captured Successfully");
	}

	public void processImage(String file1, String file2) throws Exception {
		try {
			Image image1 = Toolkit.getDefaultToolkit().getImage(file1);
			Image image2 = Toolkit.getDefaultToolkit().getImage(file2);
			PixelGrabber grab1 = new PixelGrabber(image1, 0, 0, -1, -1, false);
			PixelGrabber grab2 = new PixelGrabber(image2, 0, 0, -1, -1, false);
			int[] data1 = null;
			if (grab1.grabPixels()) {
				int width = grab1.getWidth();
				int height = grab1.getHeight();
				data1 = new int[width * height];
				data1 = (int[]) grab1.getPixels();
			}
			int[] data2 = null;
			if (grab2.grabPixels()) {
				int width = grab2.getWidth();
				int height = grab2.getHeight();
				data2 = new int[width * height];
				data2 = (int[]) grab2.getPixels();
			}
			boolean result = java.util.Arrays.equals(data1, data2);
			if (result == false) {
				;
				rg.passTestCase("Result = Video is playing - PASS ");
			} else {
				;
				rg.logException("Result = Video is not Playing - FALSE" + null, null);
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void validateImage(String imageName1, String imageName2) throws Exception {
		try {
			String file1 = "PhotoPassScreenshots/" + imageName1 + "" + ".png";
			String file2 = "PhotoPassScreenshots/" + imageName2 + "" + ".png";
			Image image1 = Toolkit.getDefaultToolkit().getImage(file1);
			Image image2 = Toolkit.getDefaultToolkit().getImage(file2);
			PixelGrabber grab1 = new PixelGrabber(image1, 0, 0, -1, -1, false);
			PixelGrabber grab2 = new PixelGrabber(image2, 0, 0, -1, -1, false);
			int[] data1 = null;
			if (grab1.grabPixels()) {
				int width = grab1.getWidth();
				int height = grab1.getHeight();
				data1 = new int[width * height];
				data1 = (int[]) grab1.getPixels();
			}
			int[] data2 = null;
			if (grab2.grabPixels()) {
				int width = grab2.getWidth();
				int height = grab2.getHeight();
				data2 = new int[width * height];
				data2 = (int[]) grab2.getPixels();
			}
			boolean result = java.util.Arrays.equals(data1, data2);
			if (result) {
				;
				rg.passTestCase("Result = Image validation - Pass ");
			} else {
				;
				rg.passTestCase("Result = Image validation - Fail ");
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void validateSlideImage(String imageName1, String imageName2) throws Exception {
		try {
			String file1 = "PhotoPassScreenshots\\" + imageName1 + "" + ".png";
			String file2 = "PhotoPassScreenshots\\" + imageName2 + "" + ".png";
			Image image1 = Toolkit.getDefaultToolkit().getImage(file1);
			Image image2 = Toolkit.getDefaultToolkit().getImage(file2);
			PixelGrabber grab1 = new PixelGrabber(image1, 0, 0, -1, -1, false);
			PixelGrabber grab2 = new PixelGrabber(image2, 0, 0, -1, -1, false);
			int[] data1 = null;
			if (grab1.grabPixels()) {
				int width = grab1.getWidth();
				int height = grab1.getHeight();
				data1 = new int[width * height];
				data1 = (int[]) grab1.getPixels();
			}
			int[] data2 = null;
			if (grab2.grabPixels()) {
				int width = grab2.getWidth();
				int height = grab2.getHeight();
				data2 = new int[width * height];
				data2 = (int[]) grab2.getPixels();
			}
			boolean result = java.util.Arrays.equals(data1, data2);
			if (result) {
				;
				rg.passTestCase("Result = Slide Show Image validation - Fail ");
			} else {
				;
				rg.passTestCase("Result = Slide Show Image validation - Pass ");
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public String getAppProperties(String key) throws IOException {
		String value = "";
		try {
			FileInputStream fileInputStream = new FileInputStream("data.properties");
			Properties property = new Properties();
			property.load(fileInputStream);
			value = property.getProperty(key);
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public void hideKeyboard() throws Exception {
		appiumDriver.hideKeyboard();
		// appiumDriver.navigate().back();
		;
	}

	public void enterUrl(String url) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				appiumDriver.get(url);
				break;
			case "Selenium":
				remoteDriver.get(url);
				assertTrue("Application URL " + url + " has been launched", true);
				break;
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	/* **********Added For POC****************** */
	public void tap(int x, int y, String elementName) throws InterruptedException, IOException {
		appiumDriver.tap(1, x, y, 1);
		test.log(LogStatus.PASS, "Tapped on  " + elementName + " successful", test.addScreenCapture(takeScreenShot()));
		// Reporter.log(message + "\n");
		Thread.sleep(2000);
	}

	public void tap(String property, String message) throws InterruptedException {
		touch.tap(getX(property), getY(property)).perform();
		Reporter.log(message + "\n");
		Thread.sleep(2000);
	}

	public void moveTo(int x, int y, int x1, int y1) throws InterruptedException {
		touch.press(x, y).moveTo(x1, y1).perform();
		wait(2000);
	}

	public void moveTo(WebElement source, WebElement target) throws InterruptedException {
		touch.press(source).moveTo(target).perform();
		wait(3000);
	}

	public int getX(String property) {
		int x = findElement(property).getLocation().x;
		return x;
	}

	public int getY(String property) {
		int y = findElement(property).getLocation().y;
		return y;
	}

	public int getX(ArrayList<String> coordinates) {
		int x = Integer.parseInt(coordinates.get(0));
		return x;
	}

	public int getY(ArrayList<String> coordinates) {
		int y = Integer.parseInt(coordinates.get(1));
		return y;
	}

	public List<WebElement> findElements(String property) {
		List<WebElement> elements = null;
		if (property.startsWith(ID)) {
			elements = driver.findElements(By.id(property.replaceFirst(ID, "")));
		} else if (property.startsWith(NAME)) {
			elements = driver.findElements(By.name(property.replaceFirst(NAME, "")));
		} else if (property.startsWith(LINKTEXT)) {
			elements = driver.findElements(By.linkText(property.replaceFirst(LINKTEXT, "")));
		} else if (property.startsWith(PARTLINKTEXT)) {
			elements = driver.findElements(By.partialLinkText(property.replaceFirst(PARTLINKTEXT, "")));
		} else if (property.startsWith(XPATH)) {
			elements = driver.findElements(By.xpath(property.replaceFirst(XPATH, "")));
		}
		return elements;
	}

	public WebElement findElement(String property) {
		WebElement element = null;
		if (property.startsWith(ID)) {
			element = driver.findElement(By.id(property.replaceFirst(ID, "")));
		} else if (property.startsWith(NAME)) {
			element = driver.findElement(By.name(property.replaceFirst(NAME, "")));
		} else if (property.startsWith(LINKTEXT)) {
			element = driver.findElement(By.linkText(property.replaceFirst(LINKTEXT, "")));
		} else if (property.startsWith(PARTLINKTEXT)) {
			element = driver.findElement(By.partialLinkText(property.replaceFirst(PARTLINKTEXT, "")));
		} else if (property.startsWith(XPATH)) {
			element = driver.findElement(By.xpath(property.replaceFirst(XPATH, "")));
		}
		return element;
	}

	/* ************Completed ************ */
	public void clickPoint(WebElement e, String elementName) throws Exception {
		int xx = e.getLocation().x;
		int yy = e.getLocation().y;
		clickCoordinates(xx, yy);
	}

	/*
	 * public void click(String property, String elementName) throws Exception {
	 * try { WebElement e=findElement(property); switch (toolName) {
	 * 
	 * case "Appium": WebDriverWait wait = new WebDriverWait(appiumDriver, 200,
	 * 500); wait.until(ExpectedConditions.visibilityOf(e)); break; case
	 * "Selenium": WebDriverWait waitSelenium = new WebDriverWait(remoteDriver,
	 * 60, 250); // waitSelenium.until(ExpectedConditions.visibilityOf(e));
	 * waitSelenium.until(ExpectedConditions.elementToBeClickable(e)); break; }
	 * e.click(); // waitForAjax(20); test.log(LogStatus.PASS, "Clicked on  " +
	 * elementName + " successful", test.addScreenCapture(takeScreenShot())); }
	 * catch (Exception exc) { exc.printStackTrace();
	 * rg.logException("Exception on clicking webelement" + elementName, exc);
	 * test.log(LogStatus.FAIL, "Clicked on  " + elementName + " Failure",
	 * test.addScreenCapture(takeScreenShot())); } }
	 */
	public void click(WebElement e, String elementName) throws Exception {
		try {
			// WebElement e=findElement(property);
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 200, 500);
				wait.until(ExpectedConditions.visibilityOf(e));
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 250);
				// waitSelenium.until(ExpectedConditions.visibilityOf(e));
				waitSelenium.until(ExpectedConditions.elementToBeClickable(e));
				break;
			}
			e.click();
			test.log(LogStatus.PASS, "Clicked on  " + elementName + " successful");
			test.log(LogStatus.PASS, "Clicked on  " + elementName + " successful", test.addScreenCapture(takeScreenShot()));
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception on clicking webelement" + elementName, exc);
			test.log(LogStatus.FAIL, "Clicked on  " + elementName + " Failure", test.addScreenCapture(takeScreenShot()));
		}
	}

	public static String RemoteDrivertakeScreenShot() throws IOException {
		Calendar cal = Calendar.getInstance();
		long s = cal.getTimeInMillis();
		try {
			File screen1 = (File) ((TakesScreenshot) remoteDriver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screen1, new File("ReportGenerator/" + reportFolder + "/Screenshots/image" + s + ".png"));
		} catch (Exception e) {
			;
		}
		return (new File("ReportGenerator//" + reportFolder + "//Screenshots//image" + s + ".png").getAbsolutePath());
	}
	
	public static String takeScreenShot() throws IOException {
		Calendar cal = Calendar.getInstance();
		long s = cal.getTimeInMillis();
		try {
			File screen1 = (File) ((TakesScreenshot) appiumDriver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screen1, new File("ReportGenerator/" + reportFolder + "/Screenshots/image" + s + ".png"));
		} catch (Exception e) {
			;
		}
		return (new File("ReportGenerator//" + reportFolder + "//Screenshots//image" + s + ".png").getAbsolutePath());
	}

	public void waitForElementEnabled(String property, int msec) {
		try {
			msec = msec * 10;
			while (msec > 0 && (!findElement(property).isEnabled())) {
				Thread.sleep(1000);
				msec = msec - 1000;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void swipe() {
		JavascriptExecutor js = (JavascriptExecutor) appiumDriver;
		HashMap<String, Double> swipeObject = new HashMap<String, Double>();
		swipeObject.put("startX", 0.95);
		swipeObject.put("startY", 0.5);
		swipeObject.put("endX", 0.05);
		swipeObject.put("endY", 0.5);
		swipeObject.put("duration", 1.8);
		js.executeScript("mobile: swipe", swipeObject);
	}

	public void clickByJse(WebElement e, String elementName) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
				wait.until(ExpectedConditions.visibilityOf(e));
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 250);
				// waitSelenium.until(ExpectedConditions.visibilityOf(e));
				// waitSelenium.until(ExpectedConditions.elementToBeClickable(e));
				break;
			}
			JavascriptExecutor jse = (JavascriptExecutor) remoteDriver;
			jse.executeScript("arguments[0].click();", e);
			rg.passTestCase("click on element " + elementName + "" + " successful");
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception on clicking webelement" + elementName, exc);
		}
	}

	public void clickWithoutSS(WebElement e) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
				wait.until(ExpectedConditions.visibilityOf(e));
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 250);
				waitSelenium.until(ExpectedConditions.elementToBeClickable(e));
				break;
			}
			e.click();
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception on clicking webelement" + e, exc);
		}
	}

	public void clickHiddentElement(WebElement e, String elementName) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				JavascriptExecutor executor = (JavascriptExecutor) appiumDriver;
				executor.executeScript("arguments[0].click();", e);
				break;
			case "Selenium":
				JavascriptExecutor executor1 = (JavascriptExecutor) remoteDriver;
				executor1.executeScript("arguments[0].click();", e);
				break;
			}
			// e.click();
			rg.passTestCase("click on element " + elementName + "" + " successful");
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception on clicking webelement" + elementName, exc);
		}
	}

	public void navigateToToC(String strToCLayer, String strToCItem) {
		try {
			switch (toolName) {
			case "Selenium":
				WebElement testElement = remoteDriver.findElement(By.cssSelector("div[id='tocItemContainer" + strToCLayer + "'] > div > div > div[title='" + strToCItem + "']"));
				if (!testElement.isDisplayed()) {
					scrollTo(remoteDriver, testElement);
				}
				testElement.click();
				break;
			case "Appium":
				WebElement testElement1 = appiumDriver.findElement(By.cssSelector("div[id='tocItemContainer" + strToCLayer + "'] > div > div > div[title='" + strToCItem + "']"));
				if (!testElement1.isDisplayed()) {
					scrollTo(appiumDriver, testElement1);
				}
				testElement1.click();
				break;
			}
		} catch (Exception e) {
			;
		}
	}

	public void scrollTo(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("document.getElementById('container id').scrollTop += 250;", "");
	}

	public String getCurrentUrl() throws Exception {
		String url = null;
		try {
			switch (toolName) {
			case "Appium":
				url = appiumDriver.getCurrentUrl();
				break;
			case "Selenium":
				url = remoteDriver.getCurrentUrl();
				break;
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception on getting Current Url", exc);
		}
		return url;
	}

	public String fetchContentFromWebUI(String strCss) throws Exception {
		String strText = "";
		switch (toolName) {
		case "Selenium":
			;
			WebElement element = remoteDriver.findElement(By.cssSelector(strCss));
			WebElement testElement = remoteDriver.findElement(By.cssSelector("p[id^='p26']>span[id='word1']"));
			System.out.println("Font face/family in eBook for the text - The Language of Reaching Out: " + testElement.getCssValue("font-family"));
			String UIfontface = testElement.getCssValue("font-family");
			assertTrue("Font face/family in eBook for the text - The Language of Reaching Out: " + testElement.getCssValue("font-family"), true);
			System.out.println("Font size in eBook for the text - The Language of Reaching Out: " + testElement.getCssValue("font-size"));
			String UIfontSize = testElement.getCssValue("font-size");
			assertTrue("Font size in eBook for the text - The Language of Reaching Out: " + testElement.getCssValue("font-size"), true);
			strText = element.getText();
			break;
		case "Appium":
			;
			WebElement element1 = appiumDriver.findElement(By.cssSelector(strCss));
			WebElement testElement1 = appiumDriver.findElement(By.cssSelector("p[id^='p26']>span[id='word1']"));
			System.out.println("Font face/family in eBook for the text - The Language of Reaching Out: " + testElement1.getCssValue("font-family"));
			UIfontface = testElement1.getCssValue("font-family");
			assertTrue("Font face/family in eBook for the text - The Language of Reaching Out: " + testElement1.getCssValue("font-family"), true);
			System.out.println("Font size in eBook for the text - The Language of Reaching Out: " + testElement1.getCssValue("font-size"));
			UIfontSize = testElement1.getCssValue("font-size");
			assertTrue("Font size in eBook for the text - The Language of Reaching Out: " + testElement1.getCssValue("font-size"), true);
			strText = element1.getText();
			break;
		}
		return strText;
	}

	public void clickByCSS(String e, String text) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				// appiumDriver.findElementByCssSelector(e).click();
				List<WebElement> li = appiumDriver.findElementsByCssSelector(e);
				break;
			case "Selenium":
				remoteDriver.findElementByCssSelector(e).click();
				break;
			}
			rg.passTestCase("click on element " + e + "" + " successful");
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception on clicking webelement" + e, exc);
		}
	}

	public void clickWithoutWait(WebElement e, String elementName) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
				wait.until(ExpectedConditions.visibilityOf(e));
				break;
			case "Selenium":
				break;
			}
			e.click();
			rg.passTestCase("click on element " + elementName + "" + " successful");
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception on clicking webelement" + elementName, exc);
		}
	}

	public void clickWithoutWait(String xpath, String elementName) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
				wait.until(ExpectedConditions.visibilityOf(appiumDriver.findElementByXPath(xpath)));
				appiumDriver.findElementByXPath(xpath).click();
				break;
			case "Selenium":
				Thread.sleep(3000);
				remoteDriver.findElementByXPath(xpath).click();
				break;
			}
			rg.passTestCase("click on element " + elementName + "" + " successful");
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception on clicking webelement" + elementName, exc);
		}
	}

	/*
	 * public void click(String xpath, String elementName) throws Exception {
	 * try { switch (toolName) { case "Appium": WebDriverWait wait = new
	 * WebDriverWait(appiumDriver, 60, 500);
	 * wait.until(ExpectedConditions.visibilityOfElementLocated
	 * (By.xpath(xpath))); appiumDriver.findElementByXPath(xpath).click();
	 * waitForAjax(20); break; case "Selenium": WebDriverWait waitSelenium = new
	 * WebDriverWait(remoteDriver, 60, 500);
	 * waitSelenium.until(ExpectedConditions
	 * .visibilityOfElementLocated(By.xpath(xpath)));
	 * remoteDriver.findElementByXPath(xpath).click(); waitForAjax(20); break; }
	 * rg.passTestCase("Click on Element " + elementName + " successful"); }
	 * catch (Exception exc) { rg.logException("Exception in clicking " +
	 * elementName, exc); } }
	 */
	public void clickbyid(String id, String elementName) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
				// wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
				appiumDriver.findElement(By.id(id)).click();
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 100, 1000);
				/*
				 * waitSelenium.until(ExpectedConditions
				 * .visibilityOfElementLocated(By.id(id)));
				 */
				remoteDriver.findElementById(id).click();
				break;
			}
			rg.passTestCase("Click on Element " + elementName + " successful");
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception in clicking " + elementName, exc);
		}
	}

	public void clickbyClassName(String className, String elementName) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(className)));
				appiumDriver.findElementByClassName(className).click();
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 100, 1000);
				remoteDriver.findElementByClassName(className).click();
				break;
			}
			rg.passTestCase("Click on Element " + elementName + " successful");
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception in clicking " + elementName, exc);
		}
	}

	public void clickByElementName(String name, String elementName) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
				wait.until(ExpectedConditions.elementToBeClickable(By.name(name)));
				appiumDriver.findElementByName(name).click();
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
				waitSelenium.until(ExpectedConditions.elementToBeClickable(By.name(name)));
				remoteDriver.findElementByName(name).click();
				break;
			}
			rg.passTestCase("Click on Element " + elementName + " successful");
		} catch (Exception exc) {
			rg.logException("Exception in clicking " + elementName, exc);
		}
	}

	public void clickAlert() throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
				Alert a2 = remoteDriver.switchTo().alert();
				a2.accept();
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
				Alert a1 = remoteDriver.switchTo().alert();
				a1.accept();
				break;
			}
			// rg.passTestCase("Click on Alert Successful");
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception in clicking ", exc);
		}
	}

	public void dragAndDrop(WebElement e1, WebElement e2) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				Actions action = new Actions(appiumDriver);
				action.dragAndDrop(e1, e2).perform();
				break;
			case "Selenium":
				Actions action1 = new Actions(remoteDriver);
				action1.dragAndDrop(e1, e2).perform();
				break;
			}
			// rg.passTestCase("Click on Alert Successful");
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception in clicking ", exc);
		}
	}

	public String getText(WebElement e, String elementName) throws Exception {
		switch (toolName) {
		case "Appium":
			WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
			wait.until(ExpectedConditions.visibilityOf(e));
			break;
		case "Selenium":
			WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
			waitSelenium.until(ExpectedConditions.visibilityOf(e));
			break;
		}
		String text = e.getText();
		rg.passTestCase("get text from " + elementName + " successful");
		return text;
	}

	public String getValue(WebElement e, String elementName) throws Exception {
		switch (toolName) {
		case "Appium":
			WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
			wait.until(ExpectedConditions.visibilityOf(e));
			break;
		case "Selenium":
			WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
			waitSelenium.until(ExpectedConditions.visibilityOf(e));
			break;
		}
		String text = e.getAttribute("value");
		rg.passTestCase("get text from " + elementName + " successful");
		return text;
	}

	public String getText(WebElement e) throws Exception {
		switch (toolName) {
		case "Appium":
			WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
			wait.until(ExpectedConditions.visibilityOf(e));
			break;
		case "Selenium":
			WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
			waitSelenium.until(ExpectedConditions.visibilityOf(e));
			break;
		}
		String text = e.getText().trim();
		return text;
	}

	public String getText(String xpath) throws Exception {
		String text = null;
		;
		switch (toolName) {
		case "Appium":
			WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
			wait.until(ExpectedConditions.visibilityOf(appiumDriver.findElementByXPath(xpath)));
			text = appiumDriver.findElementByXPath(xpath).getText();
			break;
		case "Selenium":
			WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
			waitSelenium.until(ExpectedConditions.visibilityOf(remoteDriver.findElementByXPath(xpath)));
			text = remoteDriver.findElementByXPath(xpath).getText();
			break;
		}
		return text;
	}

	public String getAttributeValue(WebElement e, String attribute) throws Exception {
		switch (toolName) {
		case "Appium":
			WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
			wait.until(ExpectedConditions.visibilityOf(e));
			break;
		case "Selenium":
			WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
			waitSelenium.until(ExpectedConditions.visibilityOf(e));
			break;
		}
		String text = e.getAttribute(attribute);
		return text;
	}

	public void clickMultipleButtons(WebElement tab, WebElement pause, String elementName) throws Exception {
		try {
			Thread.sleep(20000);
			if (elementIsDisplayed(pause, "pausebutton")) {
				pause.click();
			} else {
				tab.click();
				pause.click();
			}
			Thread.sleep(10000);
			rg.passTestCase("click on element " + elementName + "" + " successful");
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception on clicking webelement" + elementName, exc);
		}
	}

	public void switchToCurrentWindowTitle() throws InterruptedException {
		try {
			switch (toolName) {
			case "Appium":
				Thread.sleep(10000);
				int size = appiumDriver.getWindowHandles().size();
				for (String winHandle : appiumDriver.getWindowHandles()) {
					appiumDriver.switchTo().window(winHandle);
				}
				break;
			case "Selenium":
				for (String winHandle : remoteDriver.getWindowHandles()) {
					remoteDriver.switchTo().window(winHandle);
					Thread.sleep(5000);
				}
				break;
			}
		} catch (org.openqa.selenium.NoSuchWindowException exc) {
			exc.printStackTrace();
		}
	}

	public void switchToWindowTitle() throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				Thread.sleep(10000);
				int size = appiumDriver.getWindowHandles().size();
				ParentWinhadleMob = appiumDriver.getWindowHandle();
				for (String winHandle : appiumDriver.getWindowHandles()) {
					appiumDriver.switchTo().window(winHandle);
				}
				break;
			case "Selenium":
				ParentWinhadle = remoteDriver.getWindowHandle();
				;
				for (String winHandle : remoteDriver.getWindowHandles()) {
					remoteDriver.switchTo().window(winHandle);
					Thread.sleep(5000);
					;
				}
				break;
			}
		} catch (org.openqa.selenium.NoSuchWindowException exc) {
			exc.printStackTrace();
		}
	}

	public void switchToParentWindowTitle() throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				Thread.sleep(10000);
				appiumDriver.close();
				appiumDriver.switchTo().window(ParentWinhadleMob);
				break;
			case "Selenium":
				remoteDriver.close();
				remoteDriver.switchTo().window(ParentWinhadle);
				break;
			}
		} catch (org.openqa.selenium.NoSuchWindowException exc) {
			exc.printStackTrace();
		}
	}

	public void selectOPtion(WebElement ele, String text) throws InterruptedException {
		//WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
		//waitSelenium.until(ExpectedConditions.visibilityOf(e));
		//Select sel=new Select();
		org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(ele);
		select.selectByValue(text);
		//new org.openqa.selenium.support.ui.Select(remoteDriver.findElement(By.xpath(xpath))).selectByVisibleText(text);
	}

	public void switchToFrame(String frameId) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				Thread.sleep(5000);
				appiumDriver.switchTo().frame(frameId);
				break;
			case "Selenium":
				remoteDriver.switchTo().frame(frameId);
				break;
			}
		} catch (org.openqa.selenium.NoSuchWindowException exc) {
			exc.printStackTrace();
		}
	}

	public void switchToFrame(int frameId) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				Thread.sleep(5000);
				appiumDriver.switchTo().frame(frameId);
				break;
			case "Selenium":
				remoteDriver.switchTo().frame(frameId);
				break;
			}
		} catch (org.openqa.selenium.NoSuchWindowException exc) {
			exc.printStackTrace();
		}
	}

	public void switchToParentFrame() throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				Thread.sleep(5000);
				appiumDriver.switchTo().parentFrame();
				break;
			case "Selenium":
				remoteDriver.switchTo().parentFrame();
				break;
			}
		} catch (org.openqa.selenium.NoSuchWindowException exc) {
			exc.printStackTrace();
		}
	}

	public String getParentWindow() throws Exception {
		String parentWindow = null;
		if (toolName.equalsIgnoreCase("Appium")) {
			parentWindow = appiumDriver.getWindowHandle();
		} else if (toolName.equalsIgnoreCase("Selenium")) {
			parentWindow = remoteDriver.getWindowHandle();
		}
		return parentWindow;
	}

	public void switchToParentWindow(String parentWindow) throws Exception {
		if (toolName.equalsIgnoreCase("Appium")) {
			appiumDriver.close();
			appiumDriver.switchTo().window(parentWindow);
		} else if (toolName.equalsIgnoreCase("Selenium")) {
			remoteDriver.close();
			remoteDriver.switchTo().window(parentWindow);
		}
	}

	public WebElement getElement(String xpath) throws Exception {
		switch (toolName) {
		case "Appium":
			WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
			wait.until(ExpectedConditions.visibilityOf(appiumDriver.findElementByXPath(xpath)));
			WebElement we = appiumDriver.findElementByXPath(xpath);
			return we;
		case "Selenium":
			WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
			waitSelenium.until(ExpectedConditions.visibilityOf(remoteDriver.findElementByXPath(xpath)));
			WebElement weSelenium = remoteDriver.findElementByXPath(xpath);
			return weSelenium;
		}
		rg.passTestCase("get text on webelement successful");
		return null;
	}

	public boolean verifyText(WebElement e, String value) throws Exception {
		switch (toolName) {
		case "Appium":
			WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
			wait.until(ExpectedConditions.visibilityOf(e));
			break;
		case "Selenium":
			WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
			waitSelenium.until(ExpectedConditions.visibilityOf(e));
			break;
		}
		if (e.getText().contains(value)) {
			rg.passTestCase(" Verified Element successful");
			return true;
		} else {
			rg.failTestCase("Exception on Verified webelement");
			return false;
		}
	}

	public void enterText(WebElement element, String cred, String elementName) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
				wait.until(ExpectedConditions.visibilityOf(element));
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
				waitSelenium.until(ExpectedConditions.visibilityOf(element));
				break;
			}
			element.clear();
			element.sendKeys(cred);
			test.log(LogStatus.PASS, "entering text in " + elementName + " successful", "ScreenShot as :" + test.addScreenCapture(takeScreenShot()));
		} catch (Exception exc) {
			rg.logException("Enter text failed", exc);
		}
	}

	public boolean navToSubMenu(String subMenu) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				;
				appiumDriver.findElement(By.cssSelector("div[title='" + subMenu + "']")).click();
				// waitFor(3);
				;
				break;
			case "Selenium":
				;
				remoteDriver.findElement(By.cssSelector("div[title='" + subMenu + "']")).click();
				// waitFor(3);
				;
				break;
			}
		} catch (Exception exc) {
			;
			exc.printStackTrace();
			return false;
		}
		return true;
	}

	public void clearSystemCache() throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				appiumDriver.manage().deleteAllCookies();
				break;
			case "Selenium":
				remoteDriver.manage().deleteAllCookies();
				break;
			}
		} catch (Exception exc) {
			rg.logException("Enter text failed", exc);
		}
	}

	public boolean elementIsDisplayed(WebElement e, String ElementName) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
				wait.until(ExpectedConditions.visibilityOf(e));
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 15, 250);
				waitSelenium.until(ExpectedConditions.visibilityOf(e));
				break;
			}
			rg.passTestCase(ElementName + " is displayed");
			return true;
		} catch (Exception exc) {
			return false;
		}
	}

	public void isDisplayed(WebElement e, String ElementName) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 30, 500);
				wait.until(ExpectedConditions.visibilityOf(e));
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 30, 500);
				waitSelenium.until(ExpectedConditions.visibilityOf(e));
				break;
			}
		} catch (Exception exc) {
		}
		try {
			if (e.isDisplayed()) {
				rg.passTestCase(ElementName + " is displayed");
			} else
				rg.failTestCase("");
		} catch (Exception exc) {
			rg.logException("exception", exc);
			// rg.failTestCase("element not displayed");
		}
	}

	public boolean elementIsDisplayed(WebElement e) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 30, 500);
				wait.until(ExpectedConditions.visibilityOf(e));
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 30, 500);
				waitSelenium.until(ExpectedConditions.visibilityOf(e));
				break;
			}
		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	public boolean elementIsEnabled(WebElement e) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 30, 500);
				wait.until(ExpectedConditions.visibilityOf(e));
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
				waitSelenium.until(ExpectedConditions.visibilityOf(e));
			}
		} catch (Exception exc) {
			rg.logException("Get element visibilty failed", exc);
		}
		try {
			if (e.isEnabled()) {
				return true;
			} else
				return false;
		} catch (Exception exc) {
			rg.logException("Get element visibilty failed", exc);
			return false;
		}
	}

	public boolean elementIsDisplayed(String xpath, String ElementName) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 30, 500);
				wait.until(ExpectedConditions.visibilityOf(appiumDriver.findElementByXPath(xpath)));
				if (appiumDriver.findElementByXPath(xpath).isDisplayed()) {
					rg.passTestCase(ElementName + " is displayed");
				}
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 30, 500);
				waitSelenium.until(ExpectedConditions.visibilityOf(remoteDriver.findElementByXPath(xpath)));
				if (remoteDriver.findElementByXPath(xpath).isDisplayed()) {
					rg.passTestCase(ElementName + " is displayed");
				}
				break;
			}
		} catch (Exception exc) {
			rg.logException("Element visibilty failed", exc);
			return false;
		}
		return true;
	}

	public boolean elementIsDisplayed(String xpath) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 30, 500);
				wait.until(ExpectedConditions.visibilityOf(appiumDriver.findElementByXPath(xpath)));
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 30, 500);
				waitSelenium.until(ExpectedConditions.visibilityOf(remoteDriver.findElementByXPath(xpath)));
				break;
			}
		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	public boolean elementIsDisplayedByName(String name) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 30, 500);
				wait.until(ExpectedConditions.visibilityOf(appiumDriver.findElementByName(name)));
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
				waitSelenium.until(ExpectedConditions.visibilityOf(remoteDriver.findElementByName(name)));
				break;
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			return false;
		}
		return true;
	}

	public void scroll(String key) {
		switch (toolName) {
		case "Appium":
			for (int i = 0;; i++) {
				boolean shouldBreak = false;
				List<WebElement> listObject = appiumDriver.findElements(By.name("dropdownViewCell_" + i + ""));
				while ((listObject.size()) == 0)
					break;
				for (WebElement wb : listObject) {
					if ((wb.getText().equalsIgnoreCase(key))) {
						wb.click();
						shouldBreak = true;
						break;
					}
				}
				if (shouldBreak)
					break;
			}
		case "Selenium":
			for (int i = 0;; i++) {
				boolean shouldBreak = false;
				List<WebElement> listObject = remoteDriver.findElements(By.name("dropdownViewCell_" + i + ""));
				while ((listObject.size()) == 0)
					break;
				for (WebElement wb : listObject) {
					if ((wb.getText().equalsIgnoreCase(key))) {
						wb.click();
						shouldBreak = true;
						break;
					}
				}
				if (shouldBreak)
					break;
			}
		}
	}

	public void clickCoordinates(final int x, final int y) {
		switch (toolName) {
		case "Appium":
			appiumDriver.executeScript("mobile: tap", new HashMap<String, Integer>() {
				{
					put("tapCount", (int) 1);
					put("touchCount", (int) 1);
					put("duration", (int) 0.5);
					put("x", x);
					put("y", y);
				}
			});
			break;
		case "Selenium":
			remoteDriver.executeScript("mobile: tap", new HashMap<String, Integer>() {
				{
					put("tapCount", (int) 1);
					put("touchCount", (int) 1);
					put("duration", (int) 0.5);
					put("x", x);
					put("y", y);
				}
			});
			break;
		}
	}

	public void keyBoardActions(String text) {
		switch (toolName) {
		case "Appium":
			if (text.equalsIgnoreCase("return"))
				appiumDriver.findElementByName(text).click();
			else {
				for (int i = 0; i < text.length(); i++) {
					String alp = text.substring(i, i + 1);
					appiumDriver.findElementByName(alp).click();
				}
			}
		case "Selenium":
			if (text.equalsIgnoreCase("return"))
				remoteDriver.findElementByName(text).click();
			else {
				for (int i = 0; i < text.length(); i++) {
					String alp = text.substring(i, i + 1);
					remoteDriver.findElementByName(alp).click();
				}
			}
		}
	}

	public void scrollToExact(String key) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				((ScrollsTo) appiumDriver).scrollToExact(key);
				break;
			case "Selenium":
				((ScrollsTo) remoteDriver).scrollToExact(key);
				break;
			}
			rg.passTestCase("scroll to element " + "" + key + "" + " successful");
		} catch (Exception exc) {
			rg.logException("Exception on scroll to element" + key, exc);
		}
	}

	public void scrollPage(String side, int key) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				break;
			case "Selenium":
				JavascriptExecutor jse = (JavascriptExecutor) remoteDriver;
				if (side.equalsIgnoreCase("Up"))
					jse.executeScript("scroll(0, " + key + ");");
				else {
					int key1 = (-key);
					jse.executeScript("scroll(0, " + key1 + ");");
				}
				break;
			}
			//rg.passTestCase("scroll to element  successful");
		} catch (Exception exc) {
			rg.logException("Exception on scroll to element", exc);
		}
	}

	public static String screenshot() throws IOException, InterruptedException {
		String imgPath = null;
		Thread.sleep(2000);
		n = n + 1;
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_MM_SS");
		Date date = new Date();
		String timeStamp = dateFormat.format(date);
		switch (toolName) {
		case "Appium":
			File scrFile = ((TakesScreenshot) appiumDriver).getScreenshotAs(OutputType.FILE);
			imgPath = ".//ReportGenerator//" + reportFolder + "//screenshots" + "/" + "/" + timeStamp + ".png";
			FileUtils.copyFile(scrFile, new File(imgPath));
			break;
		case "Selenium":
			File scrFileSelenium = ((TakesScreenshot) remoteDriver).getScreenshotAs(OutputType.FILE);
			imgPath = ".//ReportGenerator//" + reportFolder + "//screenshots" + "/" + "/" + timeStamp + ".png";
			FileUtils.copyFile(scrFileSelenium, new File(imgPath));
			break;
		}
		return imgPath;
	}

	// nfr scenarios
	public void accessNotification() {
		AndroidDriver android = (AndroidDriver) this.appiumDriver;
		android.openNotifications();
	}

	public void setDataConnection(boolean enable) {
		// TODO Auto-generated method stub
		if (this.appiumDriver instanceof AndroidDriver) {
			AndroidDriver android = (AndroidDriver) this.appiumDriver;
			NetworkConnectionSetting setting = android.getNetworkConnection();
			setting.dataEnabled();
			android.setNetworkConnection(setting);
			String mode = enable ? "ON" : "OFF";
			;
		}
	}

	public void setAirplaneConnection(boolean enable) {
		// TODO Auto-generated method stub
		if (this.appiumDriver instanceof AndroidDriver) {
			AndroidDriver android = (AndroidDriver) this.appiumDriver;
			NetworkConnectionSetting setting = android.getNetworkConnection();
			setting.setAirplaneMode(true);
			android.setNetworkConnection(setting);
			String mode = enable ? "ON" : "OFF";
		}
	}

	public void setWifiConnection(boolean enable) {
		// TODO Auto-generated method stub
		;
		if (this.appiumDriver instanceof AndroidDriver) {
			;
			AndroidDriver android = (AndroidDriver) this.appiumDriver;
			NetworkConnectionSetting setting = android.getNetworkConnection();
			setting.setWifi(enable);
			android.setNetworkConnection(setting);
			;
			String mode = enable ? "ON" : "OFF";
		}
	}

	public void keyboardActions(WebElement e, Keys key) {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
				wait.until(ExpectedConditions.visibilityOf(e));
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
				waitSelenium.until(ExpectedConditions.visibilityOf(e));
				waitSelenium.until(ExpectedConditions.elementToBeClickable(e));
				break;
			}
			e.sendKeys(key);
		} catch (Exception exc) {
		}
	}

	public void dragAndDropElement(String dragFromXpath, String dragToXpath, int xOffset, int yOffset) throws Exception {
		WebElement dragFrom = remoteDriver.findElementByXPath(dragFromXpath);
		WebElement dragTo = remoteDriver.findElementByXPath(dragToXpath);
		System.out.println("dragFrom =" + dragFrom + " dragTo = " + dragTo + "xOffset = " + xOffset + " yOffset =" + yOffset);
		// Setup robot
		Robot robot = new Robot();
		robot.setAutoDelay(500);
		// Fullscreen page so selenium coordinates work
		// robot.keyPress(KeyEvent.VK_F11);
		robot.mouseMove(200, 200);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		// Get size of elements
		org.openqa.selenium.Dimension fromSize = dragFrom.getSize();
		org.openqa.selenium.Dimension toSize = dragTo.getSize();
		// Get centre distance
		int xCentreFrom = fromSize.width / 2;
		int yCentreFrom = fromSize.height / 2;
		int xCentreTo = toSize.width / 2;
		int yCentreTo = toSize.height / 2;
		Point toLocation = dragTo.getLocation();
		Point fromLocation = dragFrom.getLocation();
		;
		// Make Mouse coordinate centre of element
		toLocation.x += xOffset + xCentreTo;
		toLocation.y += yOffset + yCentreTo;
		fromLocation.x += xOffset + xCentreFrom;
		fromLocation.y += yOffset + yCentreFrom;
		;
		// Move mouse to drag from location
		robot.mouseMove(fromLocation.x, fromLocation.y);
		// Thread.sleep(10000);
		// robot.mouseMove(175,250);
		// Thread.sleep(1000);
		// Click and drag
		robot.mousePress(InputEvent.BUTTON1_MASK);
		// robot.mousePress(InputEvent.
		// Drag events require more than one movement to register
		// Just appearing at destination doesn't work so move halfway first
		robot.mouseMove(((toLocation.x - fromLocation.x) / 2) + fromLocation.x, ((toLocation.y - fromLocation.y) / 2) + fromLocation.y);
		// Move to final position
		for (double i = (toLocation.x / 2); i < toLocation.x;) {
			robot.mouseMove((int) i, toLocation.y);
			i = i + 10;
		}
		// robot.mouseMove(toLocation.x, toLocation.y);
		// robot.mouseMove(175,200);
		// Drop
		// Thread.sleep(10000);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		rg.passTestCase("drag element successful");
	}

	public void scrollTo(String xpath, String element) throws Exception {
		try {
			((JavascriptExecutor) remoteDriver).executeScript("arguments[0].scrollIntoView();", remoteDriver.findElement(By.xpath(xpath)));
			rg.passTestCase("scroll to element " + "" + element + "" + " successful");
		} catch (Exception exc) {
			rg.logException("Exception on scroll to element" + element, exc);
		}
	}

	public void scrollTo(WebElement e, String element) throws Exception {
		try {
			((JavascriptExecutor) remoteDriver).executeScript("arguments[0].scrollIntoView(true);", e);
			//rg.passTestCase("scroll to element " + "" + element + "" + " successful");
		} catch (Exception exc) {
			rg.logException("Exception on scroll to element" + element, exc);
		}
	}

	public void moveToElement(String xpath1, String xpath2, String elementName) throws Exception {
		Actions actions;
		try {
			switch (toolName) {
			case "Appium":
				actions = new Actions(appiumDriver);
				actions.moveToElement(appiumDriver.findElementByXPath(xpath1)).build().perform();
				break;
			case "Selenium":
				actions = new Actions(remoteDriver);
				actions.moveToElement(remoteDriver.findElementByXPath(xpath1)).moveToElement(remoteDriver.findElementByXPath(xpath2)).build().perform();
				actions.click().build().perform();
				break;
			}
			rg.passTestCase("Move to element " + "" + elementName + "" + " successful");
		} catch (Exception exc) {
			rg.logException("Exception on Move to element" + elementName, exc);
		}
	}

	public void moveToElement(WebElement element, String elementName) throws Exception {
		Actions actions;
		try {
			switch (toolName) {
			case "Appium":
				actions = new Actions(appiumDriver);
				actions.moveToElement(element).build().perform();
				break;
			case "Selenium":
				actions = new Actions(remoteDriver);
				actions.moveToElement(element).build().perform();
				actions.click().build().perform();
				break;
			}
			rg.passTestCase("Move to element " + "" + elementName + "" + " successful");
		} catch (Exception exc) {
			rg.logException("Exception on Move to element" + elementName, exc);
		}
	}

	public void assertTrue(String msg, boolean cond) throws Exception {
		if (cond) {
			rg.passTestCase(msg);
		} else {
			rg.failTestCase(msg);
		}
	}

	public void assertTrue(boolean condition) throws Exception {
		if (!condition) {
			rg.failTestCase("Verification failed.");
		}
	}

	public void assertFalse(String message, boolean condition) throws Exception {
		if (condition) {
			rg.failTestCase(message);
		}
	}

	public void assertEquals(String message, Object expected, Object actual) throws Exception {
		if (expected == null && actual == null) {
			return;
		}
		if (expected != null && expected.equals(actual)) {
			return;
		}
		rg.failTestCase("Strings are not matched...Excepted is :" + expected + " but actual is :" + actual);
	}

	public void assertEquals(String expected, String actual) throws Exception {
		if (expected == null && actual == null) {
			return;
		}
		if (expected != null && expected.equals(actual)) {
			return;
		}
		rg.failTestCase("Strings are not matched...Excepted is :" + expected + " but actual is :" + actual);
	}

	public void assertEquals(String message, String expected, String actual) throws Exception {
		if (expected == null && actual == null) {
			rg.passTestCase(message + "...Excepted is :" + expected + " and actual is :" + actual);
			return;
		}
		if (expected != null && expected.equals(actual)) {
			rg.passTestCase(message + "...Excepted is :" + expected + " and actual is :" + actual);
			return;
		}
		rg.failTestCase(message + "...Excepted is :" + expected + " but actual is :" + actual);
	}

	public void assertEquals(String message, double expected, double actual, double delta) throws Exception {
		if (Double.compare(expected, actual) == 0) {
			return;
		}
		if (!(Math.abs(expected - actual) <= delta)) {
			rg.failTestCase(message);
		}
	}

	public void assertEquals(String message, long expected, long actual) throws Exception {
		if (new Long(expected) != null && new Long(expected).equals(new Long(actual))) {
			return;
		}
		rg.failTestCase(message);
	}

	public void waitForPageLoad() {
		switch (toolName) {
		case "Appium":
			WebDriverWait wait = new WebDriverWait(appiumDriver, 60 * 15);
			wait.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver wdriver) {
					return ((JavascriptExecutor) appiumDriver).executeScript("return document.readyState").equals("complete");
				}
			});
			break;
		case "Selenium":
			WebDriverWait wait1 = new WebDriverWait(remoteDriver, 60 * 15);
			wait1.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver wdriver) {
					return ((JavascriptExecutor) remoteDriver).executeScript("return document.readyState").equals("complete");
				}
			});
			break;
		}
	}

	public void waitFor(int wait_time) throws InterruptedException {
		Thread.sleep(wait_time * 1000);
	}

	public void waitForVisibilityOfElement(WebElement e) throws Exception {
		try {
			// WebElement e=findElement(property);
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 2000, 5);
				wait.until(ExpectedConditions.visibilityOf(e));
				wait.until(ExpectedConditions.elementToBeClickable(e));
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
				waitSelenium.until(ExpectedConditions.visibilityOf(e));
				waitSelenium.until(ExpectedConditions.elementToBeClickable(e));
				break;
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception on waiting for webelement", exc);
		}
	}

	public void waitForInvisibilityOfElement(String xpath) throws Exception {
		try {
			switch (toolName) {
			case "Appium":
				WebDriverWait wait = new WebDriverWait(appiumDriver, 60, 500);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
				break;
			case "Selenium":
				WebDriverWait waitSelenium = new WebDriverWait(remoteDriver, 60, 500);
				waitSelenium.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
				break;
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			rg.logException("Exception on waiting for webelement", exc);
		}
	}

	public boolean verifyTimeDiff(String startTime, String rewindTime) throws ParseException {
		// String StartTime="10:45:45";
		DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
		java.util.Date date = formatter.parse(startTime.replaceFirst("00", "10"));
		// ;
		// String rewindTime="10:35:45";
		// DateFormat formatter1 = new SimpleDateFormat("hh:mm:ss");
		java.util.Date date1 = formatter.parse(rewindTime.replaceFirst("00", "10"));
		// ;
		if (date1.before(date)) {
			return true;
		} else {
			return false;
		}
	}

	public void doubleClickOnString(WebElement element, String strNextWord) throws AWTException {
		JavascriptExecutor js = null;
		final String JS_GET_WORD_RECT = "var ele=arguments[0], word=arguments[1], rg=document.createRange();   " + "for(var c=ele.firstChild, i; c; c=c.nextSibling){                     " + "  if(c.nodeType != 3 || (i=c.nodeValue.indexOf(word)) < 0) continue;  " + "  rg.setStart(c, i); rg.setEnd(c, i + word.length);                   " + "  var r = ele.getBoundingClientRect(), rr = rg.getClientRects()[0];   " + "  return { left: (rr.left-r.left) | 0, top: (rr.top-r.top) | 0,       " + "           width: rr.width | 0, height: rr.height | 0 };              " + "};";
		switch (toolName) {
		case "Appium":
			js = (JavascriptExecutor) appiumDriver;
			break;
		case "Selenium":
			js = (JavascriptExecutor) remoteDriver;
			break;
		}
		// Get the text element
		// WebElement element = driver.findElement(By.cssSelector("#p4-textid2 >
		// span.p4-styleid2"));
		// Get the relative position/size {left, top, width, height} for the
		// word - strNextWord
		Map rect = (Map) js.executeScript(JS_GET_WORD_RECT, element, strNextWord);
		// Define a relative click point for the previous word "below"
		Long offset_x = (long) rect.get("left") - (long) rect.get("width") / 2;
		Long offset_y = (long) rect.get("top") + (long) rect.get("height") / 2;
		switch (toolName) {
		case "Appium":
			// Double click the word
			new Actions(appiumDriver).moveToElement(element, offset_x.intValue(), offset_y.intValue()).doubleClick().build().perform();
			break;
		case "Selenium":
			// Double click the word
			new Actions(remoteDriver).moveToElement(element, offset_x.intValue(), offset_y.intValue()).doubleClick().perform();
			break;
		}
	}

	public void selectListItem(String title, String property, String message) throws InterruptedException {
		String menuItem = title.replaceFirst("title=", "").trim();
		for (WebElement element : findElements(property)) {
			if (element.getText().equals(menuItem)) {
				// tap(element.getSize().height, element.getLocation().y,
				// message);
				element.click();
				break;
			}
		}
	}

	public void pressBack(WebDriver driver) {
		((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);
	}

	public void pressEnter(WebDriver driver) throws Exception {
		if (TestRunnerold.platFormName().equalsIgnoreCase("iOS")) {
			appiumDriver.getKeyboard().pressKey("\n");
		} else {
			((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.KEYCODE_ENTER);
			System.out.println("EnterClicked");
		}
	}

	public void scrollCPC(WebDriver driver) {
		org.openqa.selenium.Dimension dimensions = driver.manage().window().getSize();
		Double screenHeightStart = dimensions.getHeight() * 0.9;
		int scrollStart = screenHeightStart.intValue();
		Double screenHeightEnd = dimensions.getHeight() * 0.3;
		int scrollEnd = screenHeightEnd.intValue();
		((io.appium.java_client.TouchShortcuts) driver).swipe(0, scrollStart, 0, scrollEnd, 3000);
	}

	public void reverseScrollCPC(WebDriver driver) {
		Dimension dimensions = driver.manage().window().getSize();
		Double screenHeightStart = dimensions.getHeight() * 0.9;
		int scrollStart = screenHeightStart.intValue();
		Double screenHeightEnd = dimensions.getHeight() * 0.7;
		int scrollEnd = screenHeightEnd.intValue();
		((TouchShortcuts) driver).swipe(0, scrollEnd, 0, scrollStart, 1000);
	}

	public void swipeCPC(String xPath) {
		do {
			try {
				driver.findElement(By.id(xPath)).click();
				break;
			} catch (Exception NoSuchElementException) {
				Dimension dimensions = driver.manage().window().getSize();
				Double screenHeightStart = dimensions.getHeight() * 0.5;
				int scrollStart = screenHeightStart.intValue();
				Double screenHeightEnd = dimensions.getHeight() * 0.2;
				int scrollEnd = screenHeightEnd.intValue();
				((TouchShortcuts) driver).swipe(0, scrollStart, 0, scrollEnd, 2000);
			}
		} while (true);
	}

	// Method to get column values from Datasheet
	public static ArrayList<String> getColumValues(String columnWanted, String sheetName) throws Exception {
		FileInputStream fileIn = new FileInputStream(new File("./DataSheet.xls"));
		// read file
		POIFSFileSystem fs = new POIFSFileSystem(fileIn);
		HSSFWorkbook filename = new HSSFWorkbook(fs);
		// open sheet 0 which is first sheet of your worksheet
		HSSFSheet comscoreSheet = filename.getSheet(sheetName);
		// we will search for column index containing string "Your Column Name"
		// in the row 0 (which is first row of a worksheet
		Integer columnNo = null;
		// output all not null values to the list
		List<Cell> cells = new ArrayList<Cell>();
		List<String> excelParametersList = new ArrayList<String>();
		Row firstRow = comscoreSheet.getRow(0);
		for (Cell cell : firstRow) {
			if (cell.getStringCellValue().equals(columnWanted)) {
				columnNo = cell.getColumnIndex();
			}
		}
		if (columnNo != null) {
			for (Row row : comscoreSheet) {
				Cell c = row.getCell(columnNo);
				if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
					// Nothing in the cell in this row, skip it
				} else {
					cells.add(c);
					excelParametersList.add(c.toString());
				}
			}
		} else {
			System.out.println("could not find column " + columnWanted + " in first row of " + fileIn.toString());
		}
		return (ArrayList<String>) excelParametersList;
	}

	public static void compareTwoMaps(HashMap<String, String> map1, HashMap<String, String> map2) {
		test.log(LogStatus.INFO, "<b><font color=\"purple\">Parameter" + "---------" + "Expected" + "---------" + "Actual" + "---------" + "Status</b>");
		for (int i = 0; i < map1.size(); i++) {
			// Comparing Parameter From Charles and Parameter from excel
			if (map2.containsKey(map1.keySet().toArray()[i])) {
				if (map1.get(map1.keySet().toArray()[i]).equals(map2.get(map1.keySet().toArray()[i]))) {
					test.log(LogStatus.INFO, "<font color=\"black\">" + map1.keySet().toArray()[i] + "--------->" + map1.get(map1.keySet().toArray()[i]) + "" + "--------->" + "</font><font color=\"green\">" + map2.get(map1.keySet().toArray()[i]) + "--------->" + "PASS</b>");
				} else {
					test.log(LogStatus.FAIL, "<font color=\"black\">" + map1.keySet().toArray()[i] + "--------->" + map1.get(map1.keySet().toArray()[i]) + "--------->" + "</font><font color=\"red\">" + map2.get(map1.keySet().toArray()[i]) + "--------->" + "FAIL</b>");
				}
			} else {
				test.log(LogStatus.FAIL, "<font color=\"black\">" + map1.keySet().toArray()[i] + "--------->" + map1.get(map1.keySet().toArray()[i]) + "--------->" + "</font><font color=\"red\">" + map1.keySet().toArray()[i] + "    Parameter is missing" + "--------->" + "FAIL</b>");
			}
		}
	}
	
	
	public static void compareTwoMapsMixpanel(HashMap<String, String> map1, HashMap<String, String> map2) {
		test.log(LogStatus.INFO, "<font color=\"blue\">===============People Property Validations===============</font>");

		test.log(LogStatus.INFO, "<b><font color=\"purple\">Parameter" + "---------" + "Value"+"--------->" + "Status</b>");
		
		for (int i = 0; i < map1.size(); i++) {
			// Comparing Parameter From Charles and Parameter from excel
			
			if (map2.containsKey(map1.keySet().toArray()[i])) {
			
					test.log(LogStatus.INFO, "<font color=\"black\">" + map1.keySet().toArray()[i] +"--------->" + "</font><font color=\"green\">" + map2.get(map1.keySet().toArray()[i]) + "--------->" + "PASS</b>");
				} 
			else {
				test.log(LogStatus.FAIL, "<font color=\"black\">" + map1.keySet().toArray()[i] + "--------->" +"</font><font color=\"red\">" + map1.keySet().toArray()[i] + "    Parameter is not present" + "--------->" + "FAIL</b>");
			}
		}
	}
	
	
}
