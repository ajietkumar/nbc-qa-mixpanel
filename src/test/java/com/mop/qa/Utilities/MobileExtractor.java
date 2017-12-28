package com.mop.qa.Utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.io.Files;
import com.mop.qa.testbase.TestBase;
import com.relevantcodes.extentreports.LogStatus;

import au.com.bytecode.opencsv.CSVReader;

public class MobileExtractor extends TestBase {

	static String extracted;
	static boolean statusFlag;
	static String siteSection;
	static String matchedString = "";
	static HashMap<String, String> KeyValues = new HashMap<String, String>();

	public static void getAssetId() {
		try {
			setDownloadsPath();
			FileReader fr = new FileReader(CharlesJSONParser.getTheNewestFile(
					downloadedHarFile, "csv"));
			CSVReader reader = new CSVReader(fr);
			List<String[]> li = reader.readAll();
			Iterator<String[]> i1 = li.iterator();
			while (i1.hasNext()) {
				String[] str = i1.next();
				String find = "rest/v2/mcp/video/";
				if (str[0].contains(find)) {
					String subStr = str[0].substring(str[0].indexOf(find),
							str[0].length() - 1);
					String[] data = subStr.split("\\?")[0].split("/");
					// assetId = data[data.length - 1];
					break;
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String sessionIdExtractorLive() throws Exception {

		// Reading the latest XML file and extracting the beacons
		setDownloadsPath();
		File fr = CharlesJSONParser.getTheNewestFile(downloadedHarFile, "xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		String sessionId = "";

		int temp;

		dbFactory.setValidating(false);
		dBuilder.setEntityResolver(new EntityResolver() {

			@Override
			public InputSource resolveEntity(String arg0, String arg1)
					throws SAXException, IOException {

				if (arg1.contains("charles-session-1_0.dtd")) {

					return new InputSource(new StringReader(""));
				} else {
					return null;
				}
			}
		});

		// Getting all the first-line tags from the XML document

		Document xmlDoc = dBuilder.parse(fr);
		xmlDoc.getDocumentElement().normalize();
		NodeList nList = xmlDoc.getElementsByTagName("body");

		for (temp = 0; temp <= nList.getLength() - 1; temp++) {
			String bodyText = nList.item(temp).getTextContent();
			if (bodyText.contains("ANVATO-SESSION-ID")) {
				sessionId = bodyText.split("ANVATO-SESSION-ID:")[1].split("#")[0]
						.trim();
				break;
			}
		}
		return sessionId;
	}

	public static HashMap<String, ArrayList<String>> xmlParserForAdType() {
		HashMap<String, ArrayList<String>> adTypeMap = new HashMap<>();
		try {
			setDownloadsPath();
			File fr = CharlesJSONParser.getTheNewestFile(downloadedHarFile,
					"xml");
			InputStream io = new FileInputStream(fr);

			// XmlParser util = new XmlParser();
			String remove = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE charles-session SYSTEM \"http://www.charlesproxy.com/dtd/charles-session-1_0.dtd\">";
			removeLineFromFile(fr.getAbsolutePath(), remove);// uri to your file

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(io); // uri to your file
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();

			XPathExpression expr = xpath
					.compile("/charles-session/transaction/response/headers/first-line[contains(.,'HTTP/1.1')]");
			NodeList nodeList = (NodeList) (expr.evaluate(doc,
					XPathConstants.NODESET));
			int counter = nodeList.getLength();
			String str = null;
			if (counter > 0) {
				for (int i = 0; i < counter; i++) {

					Node parent = nodeList.item(i).getParentNode();
					Node reqNode = parent.getParentNode();
					Node reqBodyNode = reqNode.getLastChild();
					str = reqBodyNode.getTextContent();

					int matched = matchRequest(str);

					if (matched == 0) {

						if (str.contains("session_id")) {
							System.out.println(reqBodyNode.getTextContent());
							break;
						}
						// test.log(LogStatus.INFO, "Ad Request : " +
						// fr.getAbsolutePath() + "<p>"
						// +
						// StringEscapeUtils.escapeHtml4(reqBodyNode.getTextContent())
						// + "</p>");
					}

				}
				String temporalString[] = str.split("<temporalAdSlot");
				for (int i = 1; i < temporalString.length; i++) {
					String data = temporalString[i];
					boolean adFound = false, timeposClassFound = false, posFound = false;
					String adId = null, extactClass = null, position = null;
					ArrayList<String> positionList = new ArrayList<>();
					for (String string : data.split(" ")) {
						String regexParametersforAdid = "(?<=adId=)(.*)(?=)";
						String regexParametersfortimePosCLass = "(?<=timePositionClass=)(.*)(?=)";
						String regexParametersfortimePos = "(?<=timePosition=)(.*)(?=)";
						Matcher adMatcher = Pattern.compile(
								regexParametersforAdid).matcher(string);
						if (adMatcher.find()) {
							adId = adMatcher.group(1).toString()
									.replaceAll("'", "").trim();
							adFound = true;
						}
						Matcher timePosClass = Pattern.compile(
								regexParametersfortimePosCLass).matcher(string);
						if (timePosClass.find()) {
							String positionClass = timePosClass.group(1)
									.toString();
							extactClass = positionClass
									.substring(0,
											positionClass.indexOf("<") - 1)
									.replaceAll("'", "").trim();
							timeposClassFound = true;
						}
						Matcher timePos = Pattern.compile(
								regexParametersfortimePos).matcher(string);
						if (timePos.find()) {
							position = timePos.group(1).toString()
									.replaceAll("'", "").trim();
							posFound = true;
						}
						if (adFound && timeposClassFound && posFound) {
							positionList.add(extactClass);
							positionList.add(position);
							adTypeMap.put(adId, positionList);
							break;
						}
					}
					System.out.println(adTypeMap.toString());
				}
			} else {

			}

			if (counter == 0) {
				// test.log(LogStatus.INFO,
				// "XML File: " + fr.getAbsolutePath() + "<p>" + "Ad Request was
				// not generated" + "</p>");
				System.out.println("No Ads found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adTypeMap;

	}

	public static String adRequestCPCLive(String sessionId) throws Exception {
		String adRequest = null;
		DesiredCapabilities capabilities = new DesiredCapabilities();
		WebDriver driver = new org.openqa.selenium.safari.SafariDriver(
				capabilities);
		driver.navigate().to("http://demo.anv.bz/player/session.html");
		driver.manage().window().maximize();
		driver.findElement(By.id("session-id")).sendKeys(sessionId);
		driver.findElement(
				By.xpath("//input[@class='blue-button-medium login-button']"))
				.click();
		WebDriverWait wait = new WebDriverWait(driver, 20000);
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//div[@id='log-panel']//div[@id='log-content']//p")));
		Thread.sleep(2000);
		List<WebElement> parasAdRequest = driver.findElements(By
				.xpath("//div[@id='log-panel']//div[@id='log-content']//p"));
		for (WebElement para : parasAdRequest) {
			String request = para.getText().trim();
			if (request.contains("Pulling URL: http://29773.s")) {
				adRequest = request.replaceFirst("Pulling URL:", "").trim();
				System.out.println(adRequest);
				// break;
			}
		}
		Thread.sleep(2000);
		driver.quit();
		return adRequest;

	}

	public static String adRequestWithXMLValidationLive(String sessionId,
			String fileName) throws Exception {
		String adRequest = null;
		DesiredCapabilities capabilities = new DesiredCapabilities();
		WebDriver driver = new org.openqa.selenium.safari.SafariDriver(
				capabilities);
		driver.navigate().to("http://demo.anv.bz/player/session.html");
		driver.manage().window().maximize();
		driver.findElement(By.id("session-id")).sendKeys(sessionId);
		driver.findElement(
				By.xpath("//input[@class='blue-button-medium login-button']"))
				.click();
		WebDriverWait wait = new WebDriverWait(driver, 20000);
		wait.until(ExpectedConditions.presenceOfElementLocated(By
				.xpath("//div[@id='log-panel']//div[@id='log-content']//p")));
		Thread.sleep(2000);
		List<WebElement> parasAdRequest = driver.findElements(By
				.xpath("//div[@id='log-panel']//div[@id='log-content']//p"));
		String userAgent = null;
		for (WebElement para : parasAdRequest) {
			String request = para.getText().trim();
			if (request.contains("\"client_ua\":\"")) {
				userAgent = request.split("\"client_ua\":\"")[1].split("\"")[0]
						.trim();
			}
			if (request.contains("29773.s")) {
				adRequest = request.replaceFirst("Pulling URL:", "").trim();
				break;
			}
		}
		Thread.sleep(2000);
		URL url = new URL(adRequest);

		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestProperty("User-Agent", userAgent);
		String contentType = urlConn.getContentType();
		System.out.println("contentType:" + contentType);
		InputStream inputStream = urlConn.getInputStream();

		// opens an output stream to save into file
		FileOutputStream outputStream = new FileOutputStream(fileName);
		int bytesRead = -1;
		byte[] buffer = new byte[4096];
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		outputStream.close();
		inputStream.close();
		System.out.println("File downloaded");

		Scanner sc = new Scanner(new File(fileName));
		while (sc.hasNextLine()) {
			String xmlData = sc.nextLine();
			if (xmlData.contains("INVALID_ASSET_CUSTOM_ID")) {
				test.log(
						LogStatus.FAIL,
						"<b> "
								+ "Ad Response XML contains error message for Asset id validation</b>");
			} else if (xmlData.contains("INVALID_SITE_SECTION_CUSTOM_ID")) {
				test.log(
						LogStatus.FAIL,
						"<b> "
								+ "Ad Response XML contains error message for SiteSection validation</b>");
			} else {
				test.log(
						LogStatus.PASS,
						"<b> "
								+ "Ad Response XML doesnot contain any error message </b>");
			}
		}
		sc.close();
		driver.quit();
		return adRequest;
	}

	public static void xmlParserAdRequestExtractor() {

		try {

			File fr = CharlesJSONParser.getTheNewestFile(downloadedHarFile,
					"xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			String str = "";
			int counter = 0;
			NodeList nodeList = null;
			dbFactory.setValidating(false);
			dBuilder.setEntityResolver(new EntityResolver() {

				@Override
				public InputSource resolveEntity(String arg0, String arg1)
						throws SAXException, IOException {

					if (arg1.contains("charles-session-1_0.dtd")) {

						return new InputSource(new StringReader(""));
					} else {
						return null;
					}
				}
			});

			Document xmlDoc = dBuilder.parse(fr);
			xmlDoc.getDocumentElement().normalize();
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = null;
			expr = xpath
					.compile("/charles-session/transaction/request/headers/first-line[contains(.,'ad/p/1? HTTP/1.1')]");
			nodeList = (NodeList) (expr
					.evaluate(xmlDoc, XPathConstants.NODESET));
			counter = nodeList.getLength();

			if (counter == 0) {
				expr = xpath
						.compile("/charles-session/transaction/request/headers/first-line[contains(.,'/vod/vast.info/')]");
				nodeList = (NodeList) (expr.evaluate(xmlDoc,
						XPathConstants.NODESET));
				counter = nodeList.getLength();

				for (int i = 0; i < counter; i++) {
					Node parent = nodeList.item(i).getParentNode();
					Node reqNode = parent.getParentNode();
					Node transcaNode = reqNode.getParentNode();
					Node reqBodyNode = transcaNode.getLastChild();
					Node respBodyNode = reqBodyNode.getLastChild();
					str = respBodyNode.getTextContent();
					System.out.println(respBodyNode.getTextContent());
				}

				String[] rawUrl = str.split(",");
				for (String string : rawUrl) {
					if (string.contains("raw_url")) {
						System.out.println("Raw_url: "
								+ string.replaceFirst("\"raw_url\":", ""));
						test.log(LogStatus.INFO, "Ad Request: " + string);
					} else {
						test.log(LogStatus.INFO, "Ad Request: " + string);
					}

				}

			} else {
				MobileExtractor.xmlParser();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// logger.endTest(test);
		// logger.flush();
	}

	// @Test
	public static HashMap xmlParser() {
		Map.Entry mentry = null;

		try {

			File fr = CharlesJSONParser.getTheNewestFile(downloadedHarFile,
					"xml");

			InputStream io = new FileInputStream(fr);

			// Sanitize charles xml
			int counter = 0;
			int counter1 = 0;

			// XmlParser util = new XmlParser();
			String remove = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE charles-session SYSTEM \"http://www.charlesproxy.com/dtd/charles-session-1_0.dtd\">";
			removeLineFromFile(fr.getAbsolutePath(), remove);// uri to your file

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(io);
			doc.getDocumentElement().normalize();
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//transaction[@host='sb.scorecardresearch.com']";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
					doc, XPathConstants.NODESET);
			// System.out.println(nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				// System.out.println("\nCurrent Element :" +
				// nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String key = nNode.getAttributes().getNamedItem("query")
							.getNodeValue();
					List<String> HB = Arrays.asList(key.replaceAll("\\s", "")
							.split("&"));
					for (int k = 0; k < HB.size() - 1; k++) {
						// System.out.println(HB.get(k)+'\n');
						List<String> parValue = Arrays.asList(HB.get(k)
								.replaceAll("\\s", "").split("="));

						KeyValues.put(parValue.get(0), parValue.get(1));
					}

				}
				/* Display content using Iterator */
				Set set = KeyValues.entrySet();
				Iterator iterator = set.iterator();
				while (iterator.hasNext()) {
					mentry = (Map.Entry) iterator.next();
					// System.out.print("key is: " + mentry.getKey()
					// + " & Value is: ");
					// System.out.println(mentry.getValue());

				}

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		System.out.println("Im moving out from XMLParser");
		return KeyValues;
	}

	// logger.endTest(test);
	// logger.flush();

	public static void removeLineFromFile(String file, String lineToRemove) {

		try {
			File inFile = new File(file);

			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}

			// Construct the new file that will later be renamed to the original
			// filename.
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;
			// Read from the original file and write to the new
			// unless content matches data to be removed.
			while ((line = br.readLine()) != null) {

				if (!line.trim().equals(lineToRemove)) {
					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
			br.close();
			try {
				Files.copy(tempFile, new File(file));
				tempFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*
			 * //Delete the original file if (!inFile.delete()) {
			 * System.out.println("Could not delete file"); return; }
			 */

			// Rename the new file to the filename the original file had.
			/*
			 * if (!tempFile.renameTo(inFile)) System.out.println(
			 * "Could not rename file");
			 */

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static int matchRequest(String str) {
		int match = 0;

		Pattern pattern = Pattern
				.compile("httpHeader value=(.*?)auto-play=1\" name=\"referer\"");
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			if (matchedString.equals(matcher.group())) {
				match++;
				System.out.println(matchedString);
			} else {
				matchedString = matcher.group();
				// System.out.println(matchedString);
			}

		}

		return match;
	}

}