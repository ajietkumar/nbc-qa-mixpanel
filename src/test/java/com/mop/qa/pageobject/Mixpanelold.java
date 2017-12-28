package com.mop.qa.pageobject;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.javascript.host.file.File;
import com.itextpdf.text.log.SysoCounter;
import com.mop.qa.Utilities.MPException;
import com.mop.qa.testbase.PageBase;
import com.mop.qa.testbase.TestBase;

import org.testng.annotations.Test;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xbean.recipe.ParameterNameLoader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Class repository for Mixpanel Validations
 * 
 * @author 509967-Sainadh
 *
 */
public class Mixpanelold extends PageBase {

	public Mixpanelold() {
		super();
	}

	static String rootFolderPath = System.getProperty("user.dir");
	WebElement elem;

	WebDriver driver;
	String sheetName;
	String sheetRef;
	String propName;
	String propVal;
	FileInputStream fileIn;
	HSSFWorkbook wb = null;

	int rowCount;
	String pplPropcellVal = null;
	String liveViewCellVal = null;
	public static HashMap<String, String> ActualValue = new HashMap<String, String>();
	public static HashMap<String, String> PeoplePropMapActual = new HashMap<String, String>();
	public static HashMap<String, String> PeoplePropMapExcel = new HashMap<String, String>();

	public void head(String sheetName, String userName, String Password, String EventType, String SubEvent_Type)
			throws Exception {
		remoteDriver.navigate().refresh();
		Thread.sleep(5000);
		ValidateEvent(sheetName, EventType, SubEvent_Type);
	}

	// To validate parameters and values from mixpanel site and data sheet
	public static void ValidateEvent(String eventName, String EventType, String SubEvent_Type) throws Exception {

		String eventNameSubCat = null;
		String[] eventNameArr;
		boolean isPresent = false;

		List<WebElement> evenNamsGrp;
		List<WebElement> evenNamsGrp1;
		List<WebElement> eveNames;

		WebElement propContainer;
		WebElement parentEveName;

		boolean eventvalidate = false;
		boolean EventExists = Eventcheck(EventType);

		while (!EventExists) {

			ClickShowMore();
			EventExists = Eventcheck(EventType);
		}

		evenNamsGrp = remoteDriver.findElements(By.cssSelector(".event_group"));
		System.out.println(evenNamsGrp.toString());

		Outerloop1: for (WebElement eveName : evenNamsGrp) {
			String evntTxt = eveName.getText();
			System.out.println(evntTxt);
			boolean eventcheck = false;
			if (evntTxt.contains(EventType)) {

				parentEveName = eveName.findElement(By.xpath("parent::*"));
				scrollToElement(eveName, evntTxt);
				eveName.click();
				eveNames = eveName.findElements(By.cssSelector(".event_name"));
				evenNamsGrp1 = remoteDriver.findElements(By.cssSelector(".event_info"));
				int e = eveNames.size();
				System.out.println("No. of " + EventType + "s found are :" + e);
				if (e > 1) {
					for (int a = 1; a < e; a++) {

						eveNames.get(a).click();
						eventcheck = IsEventExists(eventName, EventType, SubEvent_Type);
						if (eventcheck) {
							System.out.println("Event Type Found.Validating the Event");
							WebValidation(EventType, eventName, SubEvent_Type);
							PeoplepropertyValidation(eventName);
							eventvalidate = true;
							eveNames.get(a).click();
							eveNames.get(0).click();
							JavascriptExecutor js = (JavascriptExecutor) remoteDriver;
							js.executeScript("window.scrollBy(0,0)", "");
							break Outerloop1;

						} else {
							eveNames.get(a).click();

						}
					}
					eveNames.get(0).click();
				} else {
					WebElement singEveName = eveName.findElement(By.cssSelector(".event_name"));

					if (EventType.equalsIgnoreCase("page load") || EventType.equalsIgnoreCase("click action")
							|| EventType.equalsIgnoreCase("Video Start")) {
						eventcheck = IsEventExists(eventName, EventType, SubEvent_Type);
						if (eventcheck) {
							System.out.println("Event Type Found.Validating the Event");
							WebValidation(EventType, eventName, SubEvent_Type);
							PeoplepropertyValidation(eventName);
							eventvalidate = true;
							singEveName.click();
							JavascriptExecutor js = (JavascriptExecutor) remoteDriver;
							js.executeScript("window.scrollBy(0,0)", "");
							break Outerloop1;
						} else {
							System.out.println("Event Type not found");
							singEveName.click();
						}
					} else {
						System.out.println("Event Type Found.Validating the Event");
						WebValidation(EventType, eventName, SubEvent_Type);
						PeoplepropertyValidation(eventName);
						eventvalidate = true;
						singEveName.click();
						JavascriptExecutor js = (JavascriptExecutor) remoteDriver;
						js.executeScript("window.scrollBy(0,0)", "");
						break Outerloop1;
					}
				}

			}

		}

		if (!eventvalidate) {
			System.out.println("Event type doesnot found.");
			test.log(LogStatus.FAIL, "Event Type does not Exists in the Page");
			throw new Exception();
		}

		test.log(LogStatus.INFO, "<font color=\"purple\">===============Event is Ending===============</font>");

	}

	public static void PeoplepropertyValidation(String eventName) throws Exception {

		String propTitle = null;
		String pplPropvalue = null;

		for (int k = 1; k <= getColumValues("Property_Name", eventName).size() - 1; k++) {
			if (getColumValues("People_Property", eventName).get(k).equalsIgnoreCase("y")) {
				PeoplePropMapExcel.put(getColumValues("Property_Name", eventName).get(k),
						getColumValues("People_Property", eventName).get(k));
			}

		}
		// System.out.println(PeoplePropMapExcel);

		WebElement mytable = remoteDriver.findElement(By.className("mp_editable_property_list"));

		List<WebElement> tableRow = mytable.findElements(By.className("mp_editable_property"));

		for (WebElement rowElement : tableRow) {

			propTitle = rowElement.findElement(By.cssSelector(".name_contents>span")).getText();
			pplPropvalue = rowElement.findElement(By.cssSelector(".value_contents")).getText();
			PeoplePropMapActual.put(propTitle, pplPropvalue);

		}

		compareTwoMapsMixpanel(PeoplePropMapExcel, PeoplePropMapActual);
		PeoplePropMapExcel.clear();
		PeoplePropMapActual.clear();
	}

	public static boolean IsEventExists(String eventName, String EventType, String SubEvent_Type) throws Exception {

		boolean EventExists = false;
		// int
		// blocksize=remoteDriver.findElements(By.xpath(".//*[@class='event_name'][contains(text(),'"+EventType+"')]/following::div[2]//span[@class='property_name']")).size();

		/*
		 * for(int i=0;i<blocksize;i++){ String
		 * ParameterName=remoteDriver.findElements(By.xpath(
		 * ".//*[@class='event_name'][contains(text(),'"+EventType+
		 * "')]/following::div[2]//span[@class='property_name']")).get(i).
		 * getText().toString().toLowerCase(); String
		 * ParameterValue=remoteDriver.findElements(By.xpath(
		 * ".//*[@class='event_name'][contains(text(),'"+EventType+
		 * "')]/following::div[2]//span[@class='property_value']")).get(i).
		 * getText().toString().toLowerCase();
		 * 
		 * Parametercheck.put(ParameterName, ParameterValue);
		 * 
		 * }
		 */
		HashMap<String, String> Parametercheck = new HashMap<String, String>();

		// Pagetype validations
		if (EventType.equalsIgnoreCase("Page Load")) {
			String PageTypeName = remoteDriver
					.findElement(By
							.xpath(".//*[@class='event_info has_properties active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Page Type:')]"))
					.getText().toString();
			String PageTypeValue = remoteDriver
					.findElement(By
							.xpath(".//*[@class='event_info has_properties active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Page Type:')]/following-sibling::*"))
					.getText().toString();
			String PageName = remoteDriver
					.findElement(By
							.xpath(".//*[@class='event_info has_properties active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Page Name: ')]"))
					.getText().toString();
			String PageNameValue = remoteDriver
					.findElement(By
							.xpath(".//*[@class='event_info has_properties active']//following-sibling::div/div//span[@class='property_name'][contains(text(),'Page Name: ')]/following-sibling::*"))
					.getText().toString();

			Parametercheck.put(PageName, PageNameValue);
			Parametercheck.put(PageTypeName, PageTypeValue);

			System.out.println("Found:" + Parametercheck);
			if (Parametercheck.containsKey("Page Type:")) {
				if (Parametercheck.get("Page Type:").equalsIgnoreCase(SubEvent_Type)) {
					// System.out.println(Parametercheck);
					EventExists = true;
				} else if (Parametercheck.get("Page Type:").equalsIgnoreCase("auth funnel")
						&& SubEvent_Type.equalsIgnoreCase("mvpd picker: full list")
						&& Parametercheck.get("Page Name:").equalsIgnoreCase("mvpd picker: full list")) {
					// System.out.println(Parametercheck);
					EventExists = true;
				} else if (Parametercheck.get("Page Type:").equalsIgnoreCase("auth funnel")
						&& SubEvent_Type.equalsIgnoreCase("mvpd picker")
						&& Parametercheck.get("Page Name:").equalsIgnoreCase("mvpd picker")) {
					// System.out.println(Parametercheck);
					EventExists = true;
				}
				// PAgeload-favorites
				// marketing
				else if (Parametercheck.get("Page Type:").equalsIgnoreCase("auth funnel")
						&& SubEvent_Type.equalsIgnoreCase("Favorites Marketing")
						&& Parametercheck.get("Page Name:").equalsIgnoreCase("Favorites Marketing")) {
					// System.out.println(Parametercheck);
					EventExists = true;
				}
			}
		}

		else if (EventType.equalsIgnoreCase("Click Action")) {
			String PageTypeName = remoteDriver
					.findElement(By
							.xpath(".//*[@class='event_info has_properties active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Page Type:')]"))
					.getText().toString();
			String PageTypeValue = remoteDriver
					.findElement(By
							.xpath(".//*[@class='event_info has_properties active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Page Type:')]/following-sibling::*"))
					.getText().toString();
			String ClickActionType = remoteDriver
					.findElement(By
							.xpath(".//*[@class='event_info has_properties active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Click Action Type: ')]"))
					.getText().toString();
			String ClickActionTypeValue = remoteDriver
					.findElement(By
							.xpath(".//*[@class='event_info has_properties active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Click Action Type: ')]/following-sibling::*"))
					.getText().toString();

			Parametercheck.put(PageTypeName, PageTypeValue);

			Parametercheck.put(ClickActionType, ClickActionTypeValue);
			System.out.println("Found:" + Parametercheck);

			if (Parametercheck.get("Page Type:").equalsIgnoreCase("show home page")
					&& (SubEvent_Type.equalsIgnoreCase("show home page"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			} else if (Parametercheck.get("Page Type:").equalsIgnoreCase("homepage")
					&& (SubEvent_Type.equalsIgnoreCase("homepage"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			}

			else if (Parametercheck.get("Click Action Type:").equalsIgnoreCase("Carousel Click")
					&& (SubEvent_Type.equalsIgnoreCase("Dynamic Lead Slide"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			}

			else if (Parametercheck.get("Click Action Type:").equalsIgnoreCase("Carousel Click")
					&& (SubEvent_Type.equalsIgnoreCase("Dynamic Lead"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			}
			if (Parametercheck.get("Page Type:").equalsIgnoreCase("Responsive Global Home Page")
					&& (SubEvent_Type.equalsIgnoreCase("Responsive Global Home Page"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			} else if (Parametercheck.get("Page Type:").equalsIgnoreCase("Responsive Show Home Page")
					&& (SubEvent_Type.equalsIgnoreCase("Responsive Show Home Page"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			} else if (Parametercheck.get("Click Action Type:").equalsIgnoreCase("Collapse Content")
					&& (SubEvent_Type.equalsIgnoreCase("Video Details"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			} else if (Parametercheck.get("Click Action Type:").equalsIgnoreCase("Expanded Content")
					&& (SubEvent_Type.equalsIgnoreCase("Expanded Content"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			} else if (Parametercheck.get("Click Action Type:").equalsIgnoreCase("Carousel Click")
					&& (SubEvent_Type.equalsIgnoreCase("Dynamic Lead Slide"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			} else if (Parametercheck.get("Click Action Type:").equalsIgnoreCase("Filter")
					&& (SubEvent_Type.equalsIgnoreCase("Clip Filter"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			} else if (Parametercheck.get("Click Action Type:").equalsIgnoreCase("Activation")
					&& (SubEvent_Type.equalsIgnoreCase("Auth Funnel"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			}
		}

		// *******************************************************PRASANTH****************************************************************************************************************

		else if (EventType.equalsIgnoreCase("Video Start"))

		{
			String CustomShelf = remoteDriver
					.findElement(By
							.xpath(".//*[@class='event_info has_properties active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Custom Shelf Title: ')]"))
					.getText().toString();

			String CustomShelfValue = remoteDriver
					.findElement(By
							.xpath(".//*[@class='event_info has_properties active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Custom Shelf Title: ')]/following-sibling::*"))
					.getText().toString();

			String Entitlement = remoteDriver
					.findElement(By
							.xpath(".//*[@class='event_info has_properties active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Entitlement: ')]"))
					.getText().toString();

			String EntitlementValue = remoteDriver
					.findElement(By
							.xpath(".//*[@class='event_info has_properties active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Entitlement: ')]/following-sibling::*"))
					.getText().toString();

			Parametercheck.put(CustomShelf, CustomShelfValue);
			Parametercheck.put(Entitlement, EntitlementValue);

			System.out.println("Found:" + Parametercheck);
			if ((Parametercheck.get("Custom Shelf Title:").equalsIgnoreCase("dynamic lead"))
					&& (SubEvent_Type.equalsIgnoreCase("Dynamic Lead"))) {
				// System.out.println(Parametercheck);
				EventExists = true;

			}

			else if ((Parametercheck.get("Custom Shelf Title:").equalsIgnoreCase("Smart Tile"))
					&& (SubEvent_Type.equalsIgnoreCase("Smart Tile"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			}

			else if ((Parametercheck.get("Entitlement:").equalsIgnoreCase("free"))
					&& (SubEvent_Type.equalsIgnoreCase("Free"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			} else if ((Parametercheck.get("Entitlement:").equalsIgnoreCase("entitled"))
					&& (SubEvent_Type.equalsIgnoreCase("Entitled"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			}

			else if (EventType.equalsIgnoreCase("Video Start") && SubEvent_Type.equalsIgnoreCase("Video Start")) {
				// System.out.println(Parametercheck);
				EventExists = true;
			}
			else if ((Parametercheck.get("Page Type:").equalsIgnoreCase("Open Text Template")) && (SubEvent_Type.equalsIgnoreCase("Open Text Template"))) {
				// System.out.println(Parametercheck);
				EventExists = true;
			}
		}

		// *******************************************************PRASANTH****************************************************************************************************************

		// else if (EventType.equalsIgnoreCase("Video Start") &&
		// SubEvent_Type.equalsIgnoreCase("Dynamic Lead"))
		// {
		// String CustomShelf = remoteDriver
		// .findElement(By
		// .xpath(".//*[@class='event_info has_properties
		// active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Custom
		// Shelf Title: ')]"))
		// .getText().toString();
		// System.out.println(CustomShelf);
		// String CustomShelfValue = remoteDriver
		// .findElement(By
		// .xpath(".//*[@class='event_info has_properties
		// active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Custom
		// Shelf Title: ')]/following-sibling::*"))
		// .getText().toString();
		// System.out.println(CustomShelfValue);
		// Parametercheck.put(CustomShelf, CustomShelfValue);
		// if ((Parametercheck.get("Custom Shelf
		// Title:").equalsIgnoreCase("dynamic lead"))
		// && (SubEvent_Type.equalsIgnoreCase("Dynamic Lead"))) {
		// // System.out.println(Parametercheck);
		// EventExists = true;
		// }
		//
		// }
		//
		// //
		// =====================================Lakshmikanth==========================
//		 else if (EventType.equalsIgnoreCase("Video Start") &&
//		 SubEvent_Type.equalsIgnoreCase("Smart Tile")) {
//		 String CustomShelf = remoteDriver
//		 .findElement(By
//		 .xpath(".//*[@class='event_info has_properties
//		 active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Custom
//		 Shelf Title: ')]"))
//		 .getText().toString();
//		 System.out.println(CustomShelf);
//		 String CustomShelfValue = remoteDriver
//		 .findElement(By
//		 .xpath(".//*[@class='event_info has_properties
//		 active']/following-sibling::div/div//span[@class='property_name'][contains(text(),'Custom
//		 Shelf Title: ')]/following-sibling::*"))
//		 .getText().toString();
//		 System.out.println(CustomShelfValue);
//		 Parametercheck.put(CustomShelf, CustomShelfValue);
//		 if ((Parametercheck.get("Custom Shelf
//		 Title:").equalsIgnoreCase("Smart Tile"))
//		 && (SubEvent_Type.equalsIgnoreCase("Smart Tile"))) {
//		 // System.out.println(Parametercheck);
//		 EventExists = true;
//		 }
		//
		// }
		// // +++++++++++++++++++LAkshmikanth++++++++++++++
		// else if (EventType.equalsIgnoreCase("Video Start") &&
		// SubEvent_Type.equalsIgnoreCase("Video Start")) {
		// // System.out.println(Parametercheck);
		// EventExists = true;
		// }

		return EventExists;

	}

	public static void scrollToElement(WebElement e, String element) throws Exception {
		try {
			((JavascriptExecutor) remoteDriver).executeScript("arguments[0].scrollIntoView(true);", e);
			// System.out.println("scroll to element " + "" + element + "" + "
			// successful");
		} catch (Exception exc) {
			test.log(LogStatus.FAIL, "Exception on scroll to element" + element);
			throw new Exception();
		}
	}

	public static void ClickShowMore() throws Exception {
		boolean ShowButtonCheck = remoteDriver.findElement(By.xpath("//*[@id='show_more_button']")).isDisplayed();
		if (ShowButtonCheck) {
			remoteDriver.findElement(By.xpath("//*[@id='show_more_button']")).click();
			test.log(LogStatus.INFO, "Clicking Show More button");
		} else {
			test.log(LogStatus.FAIL, "Show More is not present and Event also not Present");
			throw new Exception();
		}

	}

	public static boolean Eventcheck(String eventName) throws Exception {

		List<WebElement> evenNamsGrp;
		List<WebElement> eveNames;
		boolean EventExists = false;
		evenNamsGrp = remoteDriver.findElements(By.cssSelector(".event_group"));
		Outerloop1: for (WebElement eveName : evenNamsGrp)

		{
			String evntTxt = eveName.getText();

			System.out.println(evntTxt);

			if (evntTxt.contains(eventName)) {
				EventExists = true;
				break;
			}
		}
		return EventExists;
	}

	public static void WebValidation(String EventType, String eventName, String SubEvent_Type) throws Exception {
		// get the values of parameter names and values site inside the events
		// i.e. videostart
		test.log(LogStatus.INFO, "<font color=\"purple\">===============Event is Starting===============</font>");
		test.log(LogStatus.INFO, "<font color=\"purple\">=============Started validating " + EventType + "  "
				+ SubEvent_Type + "=================</font>");
		int blocksize = remoteDriver
				.findElements(By
						.xpath(".//*[@class='event_info has_properties active']/following-sibling::div[@class='properties_bg' and @style='display: block;']/div//span[@class='property_name']"))
				.size();
		test.log(LogStatus.INFO, "", test.addScreenCapture(RemoteDrivertakeScreenShot()));
		JavascriptExecutor jse = (JavascriptExecutor) remoteDriver;
		jse.executeScript("window.scrollBy(0,500)", "");
		test.log(LogStatus.INFO, "", test.addScreenCapture(RemoteDrivertakeScreenShot()));
		JavascriptExecutor jse1 = (JavascriptExecutor) remoteDriver;
		// takeScreenShot();
		// System.out.println("The Validating Events Block Size is:" +
		// blocksize);
		for (int i = 0; i < blocksize; i++) {
			String ParameterName = remoteDriver
					.findElements(By
							.xpath(".//*[@class='event_info has_properties active']/following-sibling::div/div//span[@class='property_name']"))
					.get(i).getText().toString();
			String ParameterValue = remoteDriver
					.findElements(By
							.xpath(".//*[@class='event_info has_properties active']/following-sibling::div/div//span[@class='property_value']"))
					.get(i).getText().toString();
			// System.out.println(ParameterName + "-----" + ParameterValue);
			ActualValue.put(ParameterName, ParameterValue);
		}
		test.log(LogStatus.INFO, "", test.addScreenCapture(RemoteDrivertakeScreenShot()));
		System.out.println(ActualValue);

		HashMap<String, String> excelValueMap = new HashMap<String, String>();
		for (int k = 1; k <= getColumValues("Property_Name", eventName).size() - 1; k++) {
			if (getColumValues("Live_View", eventName).get(k).equalsIgnoreCase("y")) {
				excelValueMap.put(getColumValues("Property_Name", eventName).get(k) + ":",
						getColumValues("Expected", eventName).get(k));
			}
		}
		// System.out.println(excelValueMap);

		HashMap<String, String> dynamicValuesMap = new HashMap<String, String>();

		HashMap<String, String> staticValuesMap = new HashMap<String, String>();

		HashMap<String, String> staticValuesMapMutliple = new HashMap<String, String>();

		for (String expected : excelValueMap.keySet()) {

			if (excelValueMap.get(expected).equalsIgnoreCase("Dyn")) {
				dynamicValuesMap.put(expected, excelValueMap.get(expected));
			} else if (excelValueMap.get(expected).contains("/")) {

				staticValuesMapMutliple.put(expected, excelValueMap.get(expected));
			} else {

				staticValuesMap.put(expected, excelValueMap.get(expected));
			}
		}

		for (String keyExcel : excelValueMap.keySet()) {
			if (dynamicValuesMap.containsKey(keyExcel)) {
				excelValueMap.put(keyExcel, dynamicValuesMap.get(keyExcel));
			} else if (staticValuesMap.containsKey(keyExcel)) {

				excelValueMap.put(keyExcel, staticValuesMap.get(keyExcel));
			} else if (staticValuesMapMutliple.containsKey(keyExcel)) {

				excelValueMap.put(keyExcel, staticValuesMapMutliple.get(keyExcel));
			} else {
				System.out.println("Not there in static and Dynamic");
			}
		}
		// System.out.println("********" + excelValueMap + "************");

		test.log(LogStatus.INFO, "<b><font color=\"purple\">Parameter" + "---------" + "Expected" + "---------"
				+ "Actual" + "---------" + "Status</b>");
		for (int i = 0; i < excelValueMap.size(); i++) {
			boolean MutlipleStaticcheck = false;
			// Comparing Parameter From website and Parameter from excel
			// if(!(PeoplePropMapExcel.containsKey(excelValueMap.keySet().toArray()[i]))){
			if ((ActualValue.containsKey(excelValueMap.keySet().toArray()[i]))) {
				if (excelValueMap.get(excelValueMap.keySet().toArray()[i]).contains("/")) {
					MutlipleStaticcheck = CheckMutlipleStaticinExcel(
							excelValueMap.get(excelValueMap.keySet().toArray()[i]),
							ActualValue.get(excelValueMap.keySet().toArray()[i]));
					if (MutlipleStaticcheck) {
						test.log(LogStatus.PASS, "<font color=\"black\">" + excelValueMap.keySet().toArray()[i]
								+ "--------->" + excelValueMap.get(excelValueMap.keySet().toArray()[i]) + ""
								+ "--------->" + "</font><font color=\"green\">"
								+ ActualValue.get(excelValueMap.keySet().toArray()[i]) + "--------->" + "PASS</b>");
					} else {
						test.log(LogStatus.FAIL, "<font color=\"black\">" + excelValueMap.keySet().toArray()[i]
								+ "--------->" + excelValueMap.get(excelValueMap.keySet().toArray()[i]) + "--------->"
								+ "</font><font color=\"red\">" + ActualValue.get(excelValueMap.keySet().toArray()[i])
								+ "--------->" + "FAIL</b>");
					}
				}

				else if (excelValueMap.get(excelValueMap.keySet().toArray()[i])
						.contains(ActualValue.get(excelValueMap.keySet().toArray()[i]))) {

					if (ActualValue.get(excelValueMap.keySet().toArray()[i]).length() <= 0) {
						test.log(LogStatus.FAIL, "<font color=\"black\">" + excelValueMap.keySet().toArray()[i]
								+ "--------->" + excelValueMap.get(excelValueMap.keySet().toArray()[i]) + "--------->"
								+ "</font><font color=\"red\">" + ActualValue.get(excelValueMap.keySet().toArray()[i])
								+ "--------->" + "FAIL</b>");
					} else {
						test.log(LogStatus.PASS,
								"<font color=\"black\">" + excelValueMap.keySet().toArray()[i] + "--------->"
										+ excelValueMap.get(excelValueMap.keySet().toArray()[i]) + "" + "--------->"

										+ "</font><font color=\"green\">"
										+ ActualValue.get(excelValueMap.keySet().toArray()[i]) + "--------->"
										+ "PASS</b>");
					}
				} else {

					// check whether dyn value has any data in website. if data
					// exists, give warning. otherwise fail the tc.
					if ((ActualValue.get(excelValueMap.keySet().toArray()[i]) != null)
							&& (excelValueMap.get(excelValueMap.keySet().toArray()[i]).contains("Dyn"))) {

						test.log(LogStatus.WARNING, "<font color=\"black\">" + excelValueMap.keySet().toArray()[i]
								+ "--------->" + excelValueMap.get(excelValueMap.keySet().toArray()[i]) + "--------->"
								+ "</font><font color=\"orange\">"
								+ ActualValue.get(excelValueMap.keySet().toArray()[i]) + "--------->" + "WARNING</b>");
					} else {
						test.log(LogStatus.FAIL, "<font color=\"black\">" + excelValueMap.keySet().toArray()[i]
								+ "--------->" + excelValueMap.get(excelValueMap.keySet().toArray()[i]) + "--------->"
								+ "</font><font color=\"red\">" + ActualValue.get(excelValueMap.keySet().toArray()[i])
								+ "--------->" + "FAIL</b>");
					}
				}
			} else {
				test.log(LogStatus.FAIL,
						"<font color=\"black\">" + excelValueMap.keySet().toArray()[i] + "--------->"
								+ excelValueMap.get(excelValueMap.keySet().toArray()[i]) + "--------->"
								+ "</font><font color=\"red\">" + excelValueMap.keySet().toArray()[i]
								+ "    Parameter is missing" + "--------->" + "FAIL</b>");
				// test.log(LogStatus.FAIL, "Parameter" +
				// excelValueMap.keySet().toArray()[i] + "is missing");
			}
			// }

		}
		dynamicValuesMap.clear();
		staticValuesMapMutliple.clear();
		staticValuesMap.clear();
		ActualValue.clear();
		excelValueMap.clear();

		jse.executeScript("window.scrollBy(0,-500)", "");
		jse.executeScript("window.scrollBy(0,-500)", "");
	}

	public static boolean CheckMutlipleStaticinExcel(String ExcelValue, String ActualValue) throws Exception {

		List<String> MutlipleStatic = Arrays.asList(ExcelValue.split("/"));
		if (MutlipleStatic.contains(ActualValue)) {
			return true;
		} else {
			return false;
		}
	}
}
