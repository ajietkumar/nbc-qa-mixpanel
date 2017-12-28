package com.mop.qa.testbase;

import io.appium.java_client.AppiumDriver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;
import net.lightbody.bmp.proxy.CaptureType;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.mop.qa.Utilities.CharlesIntegration;
import com.mop.qa.Utilities.CharlesJSONParser;
import com.mop.qa.Utilities.ReadDataSheet;
//import com.mop.qa.pageobject.Ev2ModelerFlowPageObject;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestBase {
	public static long startTime;
	public static String startTimeUpdate;
	public static long endTime;
	public static long totalTime;
	public static String totalTimeTaken;
	public static String osType = System.getProperty("os.name");
	public static String reportFolder = "";
	public static String toolName = "";
	private static ExtentReports extent;
	public static ExtentTest test;
	public static String currentTest;
	public static AppiumDriver appiumDriver;
	public static RemoteWebDriver remoteDriver;
	public String appium_port = null;
	// public static Client client = null;
	public static String downloadedHarFile = "";
	public static String platFormName = null, udid = null;
	Proxy proxy;
	int port;
	protected static String httpsAdServerURL = ""; // 29773,cue
	protected static String adServerURL = ""; // 29773,cue
	protected static String dataTab; // Android/iOS
	protected static String applicationType = "prod"; // prod/stage
	protected static String videoType = "VOD"; // VOD/LIVE
	public static String IP = "";
	protected static String[] beaconsURL = { "/ad/l/1?" };
	public ReadDataSheet rds = new ReadDataSheet();
	public static BrowserMobProxyServer server = new BrowserMobProxyServer();
	public static HashMap<String, ArrayList<String>> preRollBeaconsAdId = new HashMap<String, ArrayList<String>>();
 
	@BeforeSuite
	public void executeSuite(ITestContext ctx) {
		try {
			// ;
			//extent = getReporter();
			/*server.start(5555);
			proxy = ClientUtil.createSeleniumProxy(server);*/
		//	server.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
			// extent.loadConfig(new File("configuration.xml"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getSuiteName(ISuite ist) {
		try {
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			SimpleDateFormat sdfDateReport = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// dd/MM/yyyy
			Date now = new Date();
			reportFolder = "HtmlReport_" + sdfDateReport.format(now);
			String s = new File("ReportGenerator/" + reportFolder + "/TestReport.html").getPath();
			extent = new ExtentReports(s, true, Locale.ENGLISH);
			extent.addSystemInfo("Selenium Version", "2.46");
			extent.addSystemInfo("Environment", "Production");
			extent.assignProject("Disney Photo");
		}
		return extent;
	}
	


	@BeforeTest
	//USER NAME 
	public void startTestReport(ITestContext ctx) {
		extent = getReporter();
		currentTest = ctx.getCurrentXmlTest().getParameter("testname");
		udid = ctx.getCurrentXmlTest().getParameter("udid");
		platFormName = ctx.getCurrentXmlTest().getParameter("platFormName");
		System.out.println("===============================The Testcase running is " + currentTest+"=====================================");
		System.out.println("UDID:" + udid);
		System.out.println("platFormName:" + platFormName);
		test = extent.startTest(ctx.getName());
		String browser = ctx.getCurrentXmlTest().getParameter("browser");
		System.out.println("Browser:" + browser);
		if (remoteDriver == null){			

			initiateDriver(browser, udid, platFormName);
			
		
			WebElement username = remoteDriver.findElement(By.id("id_email"));
			username.sendKeys(new String[] { "prasanthkumar.venkatakrishnan@nbcuni.com" });
			WebElement password = remoteDriver.findElement(By.id("id_password"));
			password.sendKeys(new String[] { "Mighty@0715" });
			
			//remoteDriver.findElement(By.cssSelector(".sign_in > input:nth-child(3)")).click();
			//remoteDriver.findElement(By.cssSelector("input[type="submit"]")).click();
			
			
			//*[@id="bottom"]/input
			
			remoteDriver.findElement(By.xpath("//*[@id='bottom']/input")).click();
			
			remoteDriver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		
		}
	}

	@BeforeMethod
	public void startMethodReport(ITestContext ctx) throws IOException {
		//server.newHar("teknosa.har");
		
		//test.log(LogStatus.INFO, "Selected Device :" + platFormName);
	//	test.log(LogStatus.INFO, "Device UDID:  " + udid);
		//test.log(LogStatus.INFO, "Selected Tool: " + rds.getAppProperties("tool"));
		//test.log(LogStatus.INFO, "<font color=\"purple\">==============================</font>");
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void teardown() throws Exception {
		try {
			if (rds.getAppProperties("tool").equalsIgnoreCase("Appium")) {
				
			} else if(rds.getAppProperties("tool").equalsIgnoreCase("selenium")) {
				
			}
				else {
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<HarEntry> getHar() throws IOException {
		Har har = server.getHar();
		HarLog log = har.getLog();
		String path = System.getProperty("user.dir") + "/HarFiles";
		File harPath = new File(path);
		if (!harPath.exists())
			harPath.mkdirs();
		File harFile = new File(path + "/" + currentTest + "-" + platFormName + ".har");
		List<HarEntry> entries = new CopyOnWriteArrayList<HarEntry>(log.getEntries());
		System.out.println(entries);
		for (HarEntry entry : entries) {
			System.out.println(entry.getRequest().getUrl());
			// test.log(LogStatus.INFO, entry.getRequest().getUrl());
		}
		har.writeTo(harFile);
		return entries;
	}

	@AfterMethod
	protected void afterMethod(ITestResult result) throws IOException {
		extent.endTest(test);
		extent.flush();
		// server.abort();
		/*
		 * Har har = server.getHar(); HarLog log = har.getLog(); File harFile =
		 * new File("/Users/557743/Desktop/USA/Binaries/GMO.har");
		 * List<HarEntry> entries = new
		 * CopyOnWriteArrayList<HarEntry>(log.getEntries());
		 * System.out.println(entries); for (HarEntry entry : entries) {
		 * System.out.println(entry.getRequest().getUrl());
		 * test.log(LogStatus.INFO, entry.getRequest().getUrl()); }
		 * har.writeTo(harFile);
		 */
		if (appiumDriver != null) {
			appiumDriver.quit();
			// appiumDriver.close();
		}/*else if (remoteDriver!= null){
			remoteDriver.quit();
			System.out.println(remoteDriver);
			
			
		}*/
	}

	//
	@AfterSuite
	public void finishExecution() throws Exception {
		try {
			extent.close();
		if (remoteDriver != null) {
				remoteDriver.quit();
				// remoteDriver.close();  
			}
			if (appiumDriver != null) {
				appiumDriver.quit();
				// appiumDriver.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void startIOSServerForAndroidDevice() throws IOException, InterruptedException {
		;
		File wd = new File(".");
		;
		Process proc = null;
		try {
			proc = Runtime.getRuntime().exec("/bin/bash", null, wd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (proc != null) {
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
			out.println("pwd");
			String nodePathForAndroid = getPropertyValue("appium_ServerPath_Mac");
			// String nodePathForiOS = getPropertyValue("nodePathForiOSDevice");
			// out.println("./iOScmd.sh");
			out.println(nodePathForAndroid);
			out.println("exit");
			try {
				String line;
				while ((line = in.readLine()) != null) {
					if (line.contains("LogLevel: debug")) {
						System.out.println("process value is " + proc.exitValue());
					}
				}
				proc.waitFor();
				in.close();
				out.close();
				proc.destroy();
			} catch (Exception e) {
				// e.printStackTrace();
				return;
			}
		}
	}

	public static void startIOSServerForiOSDevice() throws IOException, InterruptedException {
		try {
			;
			Process p = Runtime.getRuntime().exec("open -a /Applications/Utilities/Terminal.app ./startAppiumServer.sh");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread.sleep(10000);
	}

	public BrowserMobProxyServer startBrowserMobProxy() {
		server = new BrowserMobProxyServer();
		server.start(5555);
		proxy = ClientUtil.createSeleniumProxy(server);
		return server;
	}

	public void initiateDriver(String browser, String udid, String platFormName) {
		try {
			String devicePlatform = null;
			String locality = rds.getAppProperties("locality");
			appium_port = getPort();
			// server = startBrowserMobProxy();
			System.out.println(server);
			int port = server.getPort();
			if (rds.getAppProperties("tool").equalsIgnoreCase("Appium")) {
				PageBase pagebaseclass = new PageBase(appiumDriver);
				startIOSServerForiOSDevice(udid, appium_port);
				appiumDriver = pagebaseclass.launchApp(udid, appium_port, platFormName, proxy, port);
			}
			if (rds.getAppProperties("tool").equalsIgnoreCase("selenium")) {
				PageBase pagebaseclass = new PageBase(remoteDriver);
				remoteDriver = pagebaseclass.launchSite(browser, locality);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void startWindowsServer() throws IOException, InterruptedException {
		try {
			String appiumPortVal = getPropertyValue("appiumPort");
			String nodePath_windows = getPropertyValue("nodePath_windows");
			String appiumJSPath_windows = getPropertyValue("appiumJSPath_windows");
			CommandLine command = new CommandLine(nodePath_windows);
			command.addArgument(appiumJSPath_windows, false);
			command.addArgument("--address", false);
			command.addArgument("127.0.0.1");
			command.addArgument("--port", false);
			command.addArgument(appiumPortVal);
			command.addArgument("--no-reset", false);
			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
			DefaultExecutor executor = new DefaultExecutor();
			executor.setExitValue(1);
			executor.execute(command, resultHandler);
			Thread.sleep(15000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void stopServer() {
		try {
			String filePath = "";
			if (System.getProperty("os.name").contains("Win")) {
				filePath = "cmd /c taskkill /F /IM node.exe";
				Runtime.getRuntime().exec(filePath);
			} else {
				Runtime.getRuntime().exec(new String[] { "bash", "-c", "killall node" });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getPropertyValue(String key) throws IOException {
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

	public static void updateProperty(String updateTime, String startTime) {
		try {
			FileInputStream in = new FileInputStream("report.properties");
			Properties props = new Properties();
			props.load(in);
			in.close();
			FileOutputStream out = new FileOutputStream("report.properties");
			props.setProperty("TOTAL_TIME", totalTimeTaken.toString());
			props.setProperty("RUN_STARTED", startTime.toString());
			props.store(out, null);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* charles integration methods */
	public static void setDownloadsPath() {
		String sep = Pattern.quote(File.separator);
		String path = System.getProperty("user.dir");
		String[] strs = path.split("Users");
		String userName = strs[1].trim().split(sep)[1];
		String os = System.getProperty("os.name");
		try {
			if (os.contains("Windows"))
				downloadedHarFile = "C:/Users/" + userName + "/Dropbox (NBCUniversal)/Cognizant (New)/Automation/ExecutionResults/" + CharlesIntegration.brand + "/CharlesFiles/" + CharlesJSONParser.date + "/";
			else
				downloadedHarFile = "/Users/" + userName + "/Dropbox (NBCUniversal)/Cognizant (New)/Automation/ExecutionResults/" + CharlesIntegration.brand + "/CharlesFiles/" + CharlesJSONParser.date + "/";
		} catch (Exception e) {
			if (os.contains("Windows"))
				downloadedHarFile = "C:/Users/" + userName + "/Downloads/" + CharlesJSONParser.date + "/";
			else
				downloadedHarFile = "/Users/" + userName + "/Downloads/" + CharlesJSONParser.date + "/";
		}
		new File(downloadedHarFile).mkdirs();
	}

	/* direct Launch Appium */
	public void startIOSServerForiOSDevice(String udid, String port) throws IOException, InterruptedException {
		try {
			String chromePort = getPort();
			String bootstrapPort = getPort();
			CommandLine command = new CommandLine("/Applications/Appium.app/Contents/Resources/node/bin/node");
			command.addArgument("/Applications/Appium.app/Contents/Resources/node_modules/appium/lib/server/main.js", false);
			command.addArgument("--address", false);
			command.addArgument("0.0.0.0");
			command.addArgument("--port", false);
			command.addArgument(port);
			command.addArgument("--session-override", false);
			command.addArgument("--bootstrap", false);
			command.addArgument(bootstrapPort);
			command.addArgument("--udid", false);
			command.addArgument(udid);
			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
			DefaultExecutor executor = new DefaultExecutor();
			executor.setExitValue(1);
			executor.execute(command, resultHandler);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread.sleep(10000);
	}

	// This will check for free ports
	public String getPort() throws Exception {
		ServerSocket socket = new ServerSocket(0);
		socket.setReuseAddress(true);
		String port = Integer.toString(socket.getLocalPort());
		socket.close();
		return port;
	}

	public static void beaconsValidationWithoutVideoViewCSV(ArrayList<String> urls, ArrayList<String> timeStamps) throws Exception {
		ArrayList<String> ctValues = new ArrayList<String>();
		ArrayList<String> initValues = new ArrayList<String>();
		boolean videoViewFound = false;
		try {
			ArrayList<String> slots = new ArrayList<String>();
			ArrayList<String> RetrievedAdIDArray = new ArrayList<String>();
			ArrayList<String> RetrievedParameterArray = new ArrayList<String>();
			String extractedAdId = " ", tpos = "";
			for (int i = 0; i < urls.size(); i++) {
				String queryString = urls.get(i);
				String timeStamp = timeStamps.get(i);
				if ((!queryString.contains("videoView")) && (queryString.contains(beaconsURL[0])) && (queryString.contains(adServerURL))) {
					String[] splittedStrings = queryString.split("&");
					// System.out.println(queryString);
					for (String string : splittedStrings) {
						System.out.println(string);
						if (string.contains("slot")) {
							slots.add(string.replaceFirst("cn=", ""));
							test.log(LogStatus.INFO, "<b><font color=\"blue\">slotImpression Beacon details: " + string.replaceFirst("cn=", "") + "-->Timestamp:" + timeStamp + "</font></b>");
							System.out.println("slotImpression Beacon details: " + string.replaceFirst("cn=", ""));
						}
						String regexParametersforAdid = "(?<=adid=)(.*)(?=)";
						Matcher adMatcher = Pattern.compile(regexParametersforAdid).matcher(string);
						if (adMatcher.find()) {
							extractedAdId = adMatcher.group(1).toString();
							if (!RetrievedAdIDArray.contains(extractedAdId)) {
								RetrievedAdIDArray.add(extractedAdId);
								RetrievedParameterArray = new ArrayList<String>();
							}
							if (queryString.contains(extractedAdId)) {
								String regexParameters = "(?<=cn=)(.*)(?=)";
								Matcher m = Pattern.compile(regexParameters).matcher(queryString);
								String tposRegexParameters = "(?<=tpos=)(.*)(?=)";
								Matcher tPosMatch = Pattern.compile(tposRegexParameters).matcher(queryString);
								if (tPosMatch.find()) {
									String extractedtPosString = tPosMatch.group(1).toString();
									tpos = extractedtPosString.split("&")[0];
									System.out.println("tpos:" + tpos);
								}
								if (m.find()) {
									String extractedString = m.group(1).toString();
									String extracted = extractedString.split("&")[0];
									System.out.println(extracted);
									RetrievedParameterArray.add(extracted + "_" + timeStamp);
									RetrievedParameterArray.remove("videoView");
									RetrievedParameterArray.remove("_e_timeout");
									RetrievedParameterArray.remove("_e_io");
								}
								preRollBeaconsAdId.put(extractedAdId + "_" + tpos, RetrievedParameterArray);
								break;
							}
						}
					}
				} else if (queryString.contains("videoView")) {
					videoViewFound = true;
					boolean ctFound = false, initFound = false;
					String[] str2 = queryString.split("&");
					for (String string : str2) {
						String regexParameters = "(?<=ct=)(.*)(?=)";
						String initParameters = "(?<=init=)(.*)(?=)";
						Matcher m = Pattern.compile(regexParameters).matcher(string);
						if (m.find()) {
							String extracted = m.group(1).toString();
							// System.out.println("ct=" + extracted);
							if (extracted.length() != 0) {
								ctFound = true;
								ctValues.add(extracted + "_" + timeStamp);
							}
						}
						Matcher match = Pattern.compile(initParameters).matcher(string);
						if (match.find()) {
							initFound = true;
							String extracted = match.group(1).toString();
							initValues.add(extracted);
						}
						if (ctFound && initFound)
							break;
					}
				}
			}
			ArrayList<String> ExpectedParameterArray = new ArrayList<String>();
			Collections.addAll(ExpectedParameterArray, /* "slotImpression", */"defaultImpression", "firstQuartile", "midPoint", "thirdQuartile", "complete"); // "adEnd",
			if (!preRollBeaconsAdId.isEmpty()) {
				for (String beaconCounter : preRollBeaconsAdId.keySet()) {
					ArrayList<String> parameterListWithTime = preRollBeaconsAdId.get(beaconCounter);
					ArrayList<String> parameterList = new ArrayList<>();
					for (String param : parameterListWithTime) {
						// parameterList.add(param.split("_")[0]);
						parameterList.add(param.substring(0, param.lastIndexOf("_")));
					}
					if (Arrays.equals(ExpectedParameterArray.toArray(), parameterList.toArray())) {
						System.out.println("All beacons are displayed correctly for the Ad Id: " + beaconCounter.split("_")[0]);
						test.log(LogStatus.PASS, "<b><font color=\"green\">All beacons are displayed correctly for the Ad Id: " + beaconCounter.split("_")[0] + "</font></b>");
						test.log(LogStatus.INFO, "<b><font color=\"green\">Beacon List for Ad Id: " + beaconCounter.split("_")[0] + "</font></b>");
						for (String MatchedBeacon : parameterListWithTime) {
							System.out.println(MatchedBeacon.split("_")[0]);
							test.log(LogStatus.INFO, "<b><font color=\"green\">" + MatchedBeacon.split("_")[0] + "-->Timestamp:" + MatchedBeacon.split("_")[1] + "</font></b>");
						}
					} else {
						System.out.println("All beacons are not displayed correctly for the Ad Id: " + beaconCounter.split("_")[0]);
						if (parameterList.isEmpty()) {
							System.out.println("None of the expected beacons retrieved for the Ad Id: " + beaconCounter.split("_")[0]);
							test.log(LogStatus.FAIL, "<b><font color=\"red\">None of the expected beacons retrieved for the Ad Id: " + beaconCounter.split("_")[0] + "</font></b>");
						} else {
							System.out.println("Beacon List for Ad Id: " + beaconCounter.split("_")[0]);
							test.log(LogStatus.FAIL, "<b><font color=\"red\">All beacons are not displayed correctly for the Ad Id: " + beaconCounter.split("_")[0] + "</font></b>");
							test.log(LogStatus.INFO, "<b><font color=\"red\">Beacon List for Ad Id: " + beaconCounter.split("_")[0] + "</font></b>");
							for (String unMatchedBeacon : parameterListWithTime) {
								System.out.println(unMatchedBeacon.split("_")[0]);
								test.log(LogStatus.INFO, "<b><font color=\"red\">" + unMatchedBeacon.split("_")[0] + "-->Timestamp:" + unMatchedBeacon.split("_")[1] + "</font></b>");
							}
						}
					}
					// test.log(LogStatus.INFO,
					// "<b><font color=\"green\">Tpos value: </font></b>" +
					// beaconCounter.split("_")[1]);
					// System.out.println("Tpos value: " +
					// beaconCounter.split("_")[1]);
				}
			} else {
				test.log(LogStatus.FAIL, "<b><font color=\"red\">No Preroll Ad found OR NO freewheel integration with " + adServerURL + "</font></b>");
			}
			if (videoViewFound) {
				for (int i = 0; i < ctValues.size(); i++) {
					test.log(LogStatus.INFO, "<b><font color=\"purple\">videoView beacon fired with ct=" + ctValues.get(i).split("_")[0] + " , init=" + initValues.get(i) + ".-->Timestamp:" + ctValues.get(i).split("_")[1] + "</font></b>");
				}
			}
			// int slotImpressionFreq = Collections.frequency(slots,
			// "slotImpression");
			// if (CharlesIntegration.slotImpressionCount == 0)
			// CharlesIntegration.slotImpressionCount = 1;
			// if (slotImpressionFreq == CharlesIntegration.slotImpressionCount)
			// {
			// System.out.println("SlotImpression and Slot impression count are
			// equal");
			// test.log(LogStatus.PASS, "slotImpression beacons are
			// displayed correctly");
			// } else {
			// test.log(LogStatus.FAIL, "slotImpression beacons are not
			// displayed correctly");
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setAdServerHostURL() {
		try {
			adServerURL = "29773.s.fwmrm.net";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpsAdServerURL = "https://" + adServerURL;
	}
}
