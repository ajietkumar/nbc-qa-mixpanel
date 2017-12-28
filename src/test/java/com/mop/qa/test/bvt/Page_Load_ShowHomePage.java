package com.mop.qa.test.bvt;

import org.testng.annotations.Test;


import com.mop.qa.pageobject.Mixpanel2;

import com.mop.qa.testbase.TestBase;

public class Page_Load_ShowHomePage extends TestBase {
	
	@Test
	public void PageLoadshowTest() throws Exception {
		
		String sheetName = rds.getValue("DATA", currentTest, "SheetName");
		String userName = rds.getValue("DATA", currentTest, "Username");
		String Password = rds.getValue("DATA", currentTest, "Password");	
		String EventType = rds.getValue("DATA", currentTest, "Event_Type");
		String SubEvent_Type = rds.getValue("DATA", currentTest, "SubEvent_Type");
		Mixpanel2 P=new Mixpanel2();
		
		P.head(sheetName, userName, Password,EventType,SubEvent_Type);
}
}