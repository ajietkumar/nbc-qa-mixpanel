package com.mop.qa.testrunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.mop.qa.Utilities.TestParameter1;

public class TestRunner {
	static List<TestParameter1> list;
	static List<TestParameter1> list1;

	@Test
	public void test() {
		// public static void main(String args[]) {
		try {
			String log4jConfPath = "C://Automation//log4j.properties";
			
			PropertyConfigurator.configure(log4jConfPath);
			TestNG testNG = new TestNG();
			List<XmlSuite> suites = new ArrayList<XmlSuite>();
			List<List<TestParameter1>> testLists = testList();
			for (int j = 0; j < testLists.size(); j++) {
				List<XmlTest> tests = new ArrayList<XmlTest>();
				List<TestParameter1> suiteTest = testLists.get(j);
				XmlSuite suite = new XmlSuite();
				suite.setName("Suite" + j);
				for (int i = 0; i < suiteTest.size(); i++) {
					XmlTest test = new XmlTest(suite);
					List<XmlClass> xmlclass = new ArrayList<XmlClass>();
					xmlclass.add(new XmlClass(suiteTest.get(i).getTestClass()));
					test.setName(suiteTest.get(i).getTestName());
					test.setXmlClasses(xmlclass);
					test.addParameter("testname", suiteTest.get(i).getTestName());
					test.addParameter("udid", suiteTest.get(i).getUdid());
					test.addParameter("platFormName", suiteTest.get(i).getPlatFormName());
					test.addParameter("browser", suiteTest.get(i).getBrowser());
					tests.add(test);
				}
				suite.setTests(tests);
				suites.add(suite);
			}
			testNG.setXmlSuites(suites);
			testNG.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public XmlSuite getXmlSuite() {
		XmlSuite suite = new XmlSuite();
		suite.setName("TestSuite");
		return suite;
	}
	
	/*public static List<List<TestParameter1>> testList() throws Exception {
		String platFormName = null;
		List<List<TestParameter1>> testRunner = new ArrayList<List<TestParameter1>>();
		FileInputStream fis = new FileInputStream("./TestRunner.xls");
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		int totalSheets = wb.getNumberOfSheets();
		for (int i = 0; i < totalSheets; i++) {
			HSSFSheet sheet = wb.getSheetAt(i);
			list = new ArrayList<TestParameter1>();
			for (int count = 1; count <= sheet.getLastRowNum(); count++) {
				TestParameter1 test = new TestParameter1();
				HSSFRow row = sheet.getRow(count);
				int count1 = Integer.parseInt((row.getCell(4).toString()));
				if (row.getCell(2).toString().equalsIgnoreCase("Yes")) {
					for (int l = 1; l <= count1; l++) {
						String name = row.getCell(0).toString();
						test = new TestParameter1();
						test.setTestName(name + "-Run" + l);
						test.setTestClass(row.getCell(1).toString());
						test.setPlatFormName(row.getCell(5).toString());
						test.setUdid(row.getCell(6).toString());
						list.add(test);
					}
				}
			}
			testRunner.add(list);
		}
		return testRunner;
	}
*/
	public static List<List<TestParameter1>> testList() throws Exception {
		String platFormName = null;
        List<List<TestParameter1>> testRunner = new ArrayList<List<TestParameter1>>();
        FileInputStream fis = new FileInputStream("./TestRunner.xls");
        HSSFWorkbook wb = new HSSFWorkbook(fis);
        int totalSheets = wb.getNumberOfSheets();
        for (int i = 0; i < totalSheets; i++) {
            HSSFSheet sheet = wb.getSheetAt(i);
            list = new ArrayList<TestParameter1>();
            for (int count = 1; count <= sheet.getLastRowNum(); count++) {
                TestParameter1 test = new TestParameter1();
                HSSFRow row = sheet.getRow(count);
             /* System.out.println(row.getCell(1).toString());  
              System.out.println(row.getCell(4).toString());*/
                int count1 = Integer.parseInt((row.getCell(4).toString()));
             //   System.out.println(count1);
                if (row.getCell(2).toString().equalsIgnoreCase("Yes")&row.getCell(5).toString().equalsIgnoreCase("iOS")||row.getCell(5).toString().equalsIgnoreCase("Android")) {
                    for (int l = 1; l <= count1; l++) {
                        String name = row.getCell(0).toString();
                        test = new TestParameter1();
                        test.setTestName(name + "-Run" + l);
                        System.out.println(name + "-Run" + l);
                        test.setTestClass(row.getCell(1).toString());
                        test.setPlatFormName(row.getCell(5).toString());
                        test.setUdid(row.getCell(6).toString());
                        list.add(test);
                    }
                }
                else if(row.getCell(2).toString().equalsIgnoreCase("Yes")&row.getCell(5).toString().equalsIgnoreCase("both")){
                    count1 = Integer.parseInt((row.getCell(4).toString()))+1;
                    List<String> bothudid = Arrays.asList(row.getCell(6).toString().replaceAll("\\s", "").split(","));
                    System.out.println(bothudid.get(0));
                    System.out.println(bothudid.get(1));
                    for (int l = 1; l <= count1; l++) {
                        if(l==1){
                            String name = row.getCell(0).toString();
                            test = new TestParameter1();
                            test.setTestName(name + "-Run" + l);
                            System.out.println(name + "-Run" + l);
                            test.setTestClass(row.getCell(1).toString());
                            test.setPlatFormName("iOS");
                            test.setUdid(bothudid.get(0));
                            System.out.println(bothudid.get(0));
                            list.add(test);
                        }
                        else{
                            String name = row.getCell(0).toString();
                            test = new TestParameter1();
                            test.setTestName(name + "-Run" + l);
                            System.out.println(name + "-Run" + l);
                            test.setTestClass(row.getCell(1).toString());
                            test.setPlatFormName("Android");
                            test.setUdid(bothudid.get(1));
                            System.out.println(bothudid.get(1));
                            list.add(test);
                        }
                        
                    }
                    
                    
                }else if(row.getCell(2).toString().equalsIgnoreCase("Yes")&row.getCell(5).toString().equalsIgnoreCase("web")){
                	for (int l = 1; l <= count1; l++) {
                        String name = row.getCell(0).toString();
                        test = new TestParameter1();
                        test.setTestName(name + "-Run" + l);
                        System.out.println(name + "-Run" + l);
                        test.setTestClass(row.getCell(1).toString());
                        test.setBrowser(row.getCell(7).toString());
                      //  test.setPlatFormName(row.getCell(5).toString());
                       // test.setUdid(row.getCell(6).toString());
                         list.add(test);
                         System.out.println(list);
                    }
                }
                
            }
            testRunner.add(list);
            System.out.println(list);
        }
        return testRunner;
		
	}
	
	public static List<String> featureList() throws Exception {
		List<String> featuresList = new ArrayList<String>();
		FileInputStream fis = new FileInputStream("./TestRunnerBDD.xls");
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		HSSFSheet sheet = wb.getSheet("Features");
		// HSSFSheet sheet = wb.getSheetAt(0);
		list = new ArrayList<TestParameter1>();
		for (int count = 1; count <= sheet.getLastRowNum(); count++) {
			HSSFRow row = sheet.getRow(count);
			if (row.getCell(1).toString().equalsIgnoreCase("Yes")) {
				featuresList.add(row.getCell(0).toString());
			}
		}
		return featuresList;
	}

	public static String tagsList() throws Exception {
		String featuresList = "";
		FileInputStream fis = new FileInputStream("./TestRunnerBDD.xls");
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		HSSFSheet sheet = wb.getSheet("Tags");
		// HSSFSheet sheet = wb.getSheetAt(0);
		list = new ArrayList<TestParameter1>();
		for (int count = 1; count <= sheet.getLastRowNum(); count++) {
			HSSFRow row = sheet.getRow(count);
			if (row.getCell(1).toString().equalsIgnoreCase("Yes")) {
				featuresList = featuresList + (row.getCell(0).toString()) + ",";
			}
		}
		return featuresList;
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

	public static String platFormName() throws Exception {
		String platFormName = null;
		TestParameter1 test = new TestParameter1();
		List<List<TestParameter1>> testRunner = new ArrayList<List<TestParameter1>>();
		FileInputStream fis = new FileInputStream("./TestRunner.xls");
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		int totalSheets = wb.getNumberOfSheets();
		for (int i = 0; i < totalSheets; i++) {
			HSSFSheet sheet = wb.getSheetAt(i);
			list = new ArrayList<TestParameter1>();
			// System.out.println("No of Rows" + sheet.getLastRowNum());
			for (int count = 1; count <= sheet.getLastRowNum(); count++) {
				HSSFRow row = sheet.getRow(count);
				if (row.getCell(2).toString().equalsIgnoreCase("Yes")) {
					platFormName = row.getCell(5).toString();
					test.setPlatFormName(platFormName);
				}
			}
		}
		return test.getPlatFormName();
	}

	/*
	 * public static String getTestRunnerValue(String coulumnName) throws
	 * Exception { String platFormName = null; TestParameter1 test = new
	 * TestParameter1(); List<List<TestParameter1>> testRunner = new
	 * ArrayList<List<TestParameter1>>(); FileInputStream fis = new
	 * FileInputStream("./TestRunner.xls"); HSSFWorkbook wb = new
	 * HSSFWorkbook(fis); int totalSheets = wb.getNumberOfSheets(); for (int i =
	 * 0; i < totalSheets; i++) { HSSFSheet sheet = wb.getSheetAt(i); list = new
	 * ArrayList<TestParameter1>(); // System.out.println("No of Rows" +
	 * sheet.getLastRowNum()); for (int count = 1; count <=
	 * sheet.getLastRowNum(); count++) { HSSFRow row = sheet.getRow(count); if
	 * (row.getCell(2).toString().equalsIgnoreCase("Yes")) { platFormName =
	 * row.getCell(5).toString(); test.setPlatFormName(platFormName);
	 * 
	 * } } } return test.getPlatFormName(); }
	 */
	public static String getudid() throws Exception {
		String udid = null;
		TestParameter1 test = new TestParameter1();
		List<List<TestParameter1>> testRunner = new ArrayList<List<TestParameter1>>();
		FileInputStream fis = new FileInputStream("./TestRunner.xls");
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		int totalSheets = wb.getNumberOfSheets();
		for (int i = 0; i < totalSheets; i++) {
			HSSFSheet sheet = wb.getSheetAt(i);
			list = new ArrayList<TestParameter1>();
			// System.out.println("No of Rows" + sheet.getLastRowNum());
			for (int count = 1; count <= sheet.getLastRowNum(); count++) {
				HSSFRow row = sheet.getRow(count);
				if (row.getCell(2).toString().equalsIgnoreCase("Yes")) {
					udid = row.getCell(6).toString();
					test.setUdid(udid);
				}
			}
		}
		return test.getUdid();
	}

	
}
