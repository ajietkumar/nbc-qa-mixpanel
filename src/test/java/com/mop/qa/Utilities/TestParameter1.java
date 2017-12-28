package com.mop.qa.Utilities;

public class TestParameter1 {
	public String getTestName() {
		return testName;
	}
	

	public void setTestName(String testName1) {
		this.testName = testName1;
	}

	public String getTestClass() {
		return testClass;
	}

	public void setTestClass(String testClass1) {
		testClass = testClass1;
	}

	public String getExcetionFlag() {
		return ExcetionFlag;
	}

	public void setExcetionFlag(String excetionFlag) {
		ExcetionFlag = excetionFlag;
	}

	private String toolName;
	private String testName;
	private String browser;
	private String No_of_Instances;
	private String 	platFormName;
	private String udid;


	public String getUdid() {
		return udid;
	}


	public void setUdid(String udid) {
		this.udid = udid;
	}


	public String getPlatFormName() {
		return platFormName;
	}


	public void setPlatFormName(String platFormName) {
		this.platFormName = platFormName;
	}


	public String getNo_of_Instances() {
		return No_of_Instances;
	}


	public void setNo_of_Instances(String no_of_Instances) {
		No_of_Instances = no_of_Instances;
	}


	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getToolName() {
		return toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	private String testClass;
	private String ExcetionFlag;
}
