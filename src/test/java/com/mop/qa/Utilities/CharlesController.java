package com.mop.qa.Utilities;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import com.mop.qa.testbase.TestBase;

/**
 * @author 472823
 *
 */
public class CharlesController extends CharlesIntegration {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void startRecording(String link1) {
		// TODO Auto-generated method stub
		try {
			System.setProperty("http.proxySet", "true");
			System.setProperty("http.proxyHost", "127.0.0.1");
			System.setProperty("http.proxyPort", "8888");
			URL link = new URL(link1); // The file that you want to download
			InputStream in = new BufferedInputStream(link.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (in.read() < 0) {
				new RuntimeException();
			}
		} catch (Exception e) {
		}
	}

	public static void IEDownloader(String link1, String downloadFileName) {
		// TODO Auto-generated method stub
		try {
			System.setProperty("http.proxySet", "true");
			System.setProperty("http.proxyHost", "127.0.0.1");
			System.setProperty("http.proxyPort", "8888");
			// String exportLink= "http://control.charles/session/export-xml";
			TestBase.setDownloadsPath();
			String fileName = TestBase.downloadedHarFile + downloadFileName;
			URL link = new URL(link1); // The file that you want to download
			InputStream in = new BufferedInputStream(link.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(response);
			fos.close();
			System.out.println("response:" + response);
			System.out.println("Finished");
		} catch (Exception e) {
		}
	}
}
