package com.mop.qa.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mop.qa.testbase.PageBase;
import com.mop.qa.testbase.TestBase;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class JSONParser extends PageBase {
	public static Map<String, String> getLiveAnvackKeys(String url, String Response) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> featureEnabled;
		Map<String, Object> map = new HashMap<String, Object>();
		// convert JSON string to Map
		map = objectMapper.readValue(Response, new TypeReference<Map<String, Object>>() {
		});
		@SuppressWarnings("unchecked")
		ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) map.get("modules");
		System.out.println("size of Data" + data.size());
		Map<String, String> anvackKeys = new HashMap<String, String>();
		ArrayList<String> appKeys = new ArrayList<String>();
		ArrayList<String> secretKeys = new ArrayList<String>();
		int i = data.size();
		for (Map<String, Object> show : data) {
			String name = (String) show.get("name");
			if (i == 1) {
				break;
			}
			if (i == 2) {
				// System.out.println("=====" + name + "============");
				Map<String, Object> attributes = (Map<String, Object>) show.get("specificConfig");
				String appKey = (String) attributes.get("appKey");
				String secretKey = (String) attributes.get("secretKey");
				Map<String, Object> config = (Map<String, Object>) attributes.get("config");
				for (String key : config.keySet()) {
					// System.out.println(key + ":" +
					// String.valueOf(config.get(key)));
				}
				anvackKeys.put("appKey", appKey);
				anvackKeys.put("secretKey", secretKey);
			}
			// System.out.println(anvackKeys);
			/*
			 * appKeys.add(appKey); secretKeys.add(secretKey);
			 */
			i--;
		}
		return anvackKeys;
	}

	public static Map<String, String> checkFeaturesEnabledVOD(String url, String Response) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> featureEnabled = null;
		Map<String, Object> map = new HashMap<String, Object>();
		// convert JSON string to Map
		map = objectMapper.readValue(Response, new TypeReference<Map<String, Object>>() {
		});
		@SuppressWarnings("unchecked")
		ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) map.get("modules");
		// System.out.println("size of Data" + data.size());
		Map<String, String> anvackKeys = new HashMap<String, String>();
		ArrayList<String> appKeys = new ArrayList<String>();
		ArrayList<String> secretKeys = new ArrayList<String>();
		int i = data.size();
		for (Map<String, Object> show : data) {
			String name = (String) show.get("name");
			if (i == 1) {
				break;
			}
			if (i == 3) {
				System.out.println("=====" + name + "============");
				Map<String, Object> attributes = (Map<String, Object>) show.get("specificConfig");
				String appKey = (String) attributes.get("appKey");
				String secretKey = (String) attributes.get("secretKey");
				Map<String, String> config = (Map<String, String>) attributes.get("config");
				featureEnabled = (Map<String, String>) attributes.get("config");
				System.out.println(featureEnabled);
				int counter = featureEnabled.size();
				for (String key : config.keySet()) {
					System.out.println(key + ":" + String.valueOf(config.get(key)));
					// test.log(LogStatus.INFO, key + ":" +
					// String.valueOf(config.get(key)));
					featureEnabled.put(key, String.valueOf(config.get(key)));
				}
				anvackKeys.put("appKey", appKey);
				anvackKeys.put("secretKey", secretKey);
			}
			i--;
		}
		return featureEnabled;
	}

	public static Map<String, String> checkFeaturesEnabledLive(String url, String Response) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> featureEnabled = null;
		Map<String, Object> map = new HashMap<String, Object>();
		// convert JSON string to Map
		map = objectMapper.readValue(Response, new TypeReference<Map<String, Object>>() {
		});
		@SuppressWarnings("unchecked")
		ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) map.get("modules");
		// System.out.println("size of Data" + data.size());
		Map<String, String> anvackKeys = new HashMap<String, String>();
		ArrayList<String> appKeys = new ArrayList<String>();
		ArrayList<String> secretKeys = new ArrayList<String>();
		int i = data.size();
		for (Map<String, Object> show : data) {
			String name = (String) show.get("name");
			if (i == 1) {
				break;
			}
			if (i == 2) {
				System.out.println("=====" + name + "============");
				Map<String, Object> attributes = (Map<String, Object>) show.get("specificConfig");
				String appKey = (String) attributes.get("appKey");
				String secretKey = (String) attributes.get("secretKey");
				Map<String, String> config = (Map<String, String>) attributes.get("config");
				featureEnabled = (Map<String, String>) attributes.get("config");
				for (String key : config.keySet()) {
					System.out.println(key + ":" + String.valueOf(config.get(key)));
					// test.log(LogStatus.INFO, key + ":" +
					// String.valueOf(config.get(key)));
					featureEnabled.put(key, String.valueOf(config.get(key)));
				}
				anvackKeys.put("appKey", appKey);
				anvackKeys.put("secretKey", secretKey);
			}
			i--;
		}
		return featureEnabled;
	}

	public static boolean checkNielsen(Map<String, String> module) {
		boolean enabled = true;
		for (String key : module.keySet()) {
			if (key.equalsIgnoreCase("enableRevisedNielson") & module.get(key).equalsIgnoreCase("true")) {
				test.log(LogStatus.INFO, "<b><font color=\"green\">Nielsen Enbled</font></b>");
				test.log(LogStatus.INFO, key + ":" + module.get(key));
				enabled = true;
			} else if (key.equalsIgnoreCase("enableRevisedNielson") & module.get(key).equalsIgnoreCase("false")) {
				test.log(LogStatus.INFO, "<b><font color=\"green\">Nielsen Disabled</font></b>");
				test.log(LogStatus.INFO, "<b><font color=\"green\">" + key + ":" + module.get(key) + "</font></b>");
				enabled = false;
			}
		}
		return enabled;
	}

	public static void checkAdobeAudience(Map<String, String> module) {
		for (String key : module.keySet()) {
			System.out.println(key + ":" + module.get(key));
			if (key.equalsIgnoreCase("enableAudienceManager") & module.get(key).equalsIgnoreCase("true")) {
				test.log(LogStatus.INFO, "Adobe Audience Enbled");
				test.log(LogStatus.INFO, key + ":" + module.get(key));
			} else if (key.equalsIgnoreCase("enableAudienceManager") & module.get(key).equalsIgnoreCase("false")) {
				test.log(LogStatus.INFO, "Adobe Audience Disabled");
				test.log(LogStatus.INFO, key + ":" + module.get(key));
			}
		}
	}

	public static Map<String, String> getVODAnvackKey(String url, String Response) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> featureEnabled;
		Map<String, Object> map = new HashMap<String, Object>();
		// convert JSON string to Map
		map = objectMapper.readValue(Response, new TypeReference<Map<String, Object>>() {
		});
		@SuppressWarnings("unchecked")
		ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) map.get("modules");
		// System.out.println("size of Data" + data.size());
		Map<String, String> anvackKeys = new HashMap<String, String>();
		ArrayList<String> appKeys = new ArrayList<String>();
		ArrayList<String> secretKeys = new ArrayList<String>();
		int i = data.size();
		for (Map<String, Object> show : data) {
			String name = (String) show.get("name");
			if (i == 1) {
				break;
			}
			if (i == 3) {
				System.out.println("=====" + name + "============");
				Map<String, Object> attributes = (Map<String, Object>) show.get("specificConfig");
				String appKey = (String) attributes.get("appKey");
				String secretKey = (String) attributes.get("secretKey");
				Map<String, Object> config = (Map<String, Object>) attributes.get("config");
				featureEnabled = (Map<String, Object>) attributes.get("config");
				for (String key : config.keySet()) {
					System.out.println(key + ":" + config.get(key));
					// test.log(LogStatus.INFO, key + ":" + config.get(key));
					featureEnabled.put(key, String.valueOf(config.get(key)));
				}
				anvackKeys.put("appKey", appKey);
				anvackKeys.put("secretKey", secretKey);
			}
			// System.out.println(anvackKeys);
			/*
			 * appKeys.add(appKey); secretKeys.add(secretKey);
			 */
			i--;
		}
		return anvackKeys;
	}

	public static Map<String, String> getGlobalConfig(String url, String Response) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> globalSettings = new HashMap<String, String>();
		;
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> settings = new HashMap<String, Object>();
		// convert JSON string to Map
		map = objectMapper.readValue(Response, new TypeReference<Map<String, Object>>() {
		});
		@SuppressWarnings("unchecked")
		ArrayList<String> global = new ArrayList<String>();
		HashMap<String, Object> data = (HashMap<String, Object>) map.get("globalConfig");
		// ArrayList<Map<String, Object>> data = (ArrayList<Map<String,
		// Object>>) map.get("globalConfig");
		Map<String, Object> attributes = (Map<String, Object>) data.get("settings");
		boolean forceServiceZip = (boolean) attributes.get("forceServiceZip");
		boolean allowOverrideGeolocation = (boolean) attributes.get("allowOverrideGeolocation");
		boolean collectGeolocationOnInit = (boolean) attributes.get("collectGeolocationOnInit");
		globalSettings.put("forceServiceZip", String.valueOf(collectGeolocationOnInit));
		globalSettings.put("allowOverrideGeolocation", String.valueOf(allowOverrideGeolocation));
		globalSettings.put("collectGeolocationOnInit", String.valueOf(collectGeolocationOnInit));
		System.out.println(String.valueOf("collectGeolocationOnInit"));
		System.out.println(globalSettings);
		System.out.println("**********Global Config Settings********");
		for (String key : globalSettings.keySet()) {
			System.out.println(key + ":" + globalSettings.get(key));
		}
		return globalSettings;
	}

	public static HashMap<String, String> feedResponse() throws Exception {
		String Response = SendGetPost.sendGet("http://feed.theplatform.com/f/HNK2IC/nbcd_app_adstitch_v3_prod?byGUID=3489393");
		System.out.println(Response);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> featureEnabled;
		Map<String, Object> map = new HashMap<String, Object>();
		// convert JSON string to Map
		map = objectMapper.readValue(Response, new TypeReference<Map<String, Object>>() {
		});
		// System.out.println(map.size());
		Map<String, String> dynamicValuesMap = new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) map.get("entries");
		ArrayList<Map<String, Object>> seriesName;
		for (Map<String, Object> show : data) {
			// System.out.println(show.get("pl1$entitlement"));
			// System.out.println(show.get("pl1$fullEpisode"));
			// System.out.println(show.get("title"));
			// System.out.println(show.get("pl1$dayPart"));
			if (show.get("pl1$fullEpisode").toString().equalsIgnoreCase("true")) {
				dynamicValuesMap.put("s:stream:type", "VOD Episode");
			} else {
				dynamicValuesMap.put("s:stream:type", "VOD Clip");
			}
			if (show.get("pl1$entitlement").toString().equalsIgnoreCase("free")) {
				dynamicValuesMap.put("s:meta:videostatus", "Unrestricted");
			} else {
				dynamicValuesMap.put("s:meta:videostatus", "Restricted");
			}
			// System.out.println(show.get("pl1$episodeNumber"));
			// System.out.println(show.get("pl1$seasonNumber"));
			// System.out.println(show.get("guid"));
			dynamicValuesMap.put("s:meta:videoguid", show.get("guid").toString());
			dynamicValuesMap.put("s:meta:videodaypart", show.get("pl1$dayPart").toString());
			dynamicValuesMap.put("s:meta:videoepnumber", show.get("pl1$episodeNumber").toString());
			dynamicValuesMap.put("s:meta:videotitle", show.get("title").toString());
			dynamicValuesMap.put("s:meta:videoseason", show.get("pl1$seasonNumber").toString());
			// System.out.println(show.get("title"));
			seriesName = (ArrayList<Map<String, Object>>) show.get("media$categories");
			for (Map<String, Object> series : seriesName) {
				System.out.println(series.get("media$name"));
				dynamicValuesMap.put("s:meta:videoprogram", series.get("media$name").toString().replace("Series/", ""));
			}
		}
		for (String parameter : dynamicValuesMap.keySet()) {
			System.out.println(parameter + ":" + dynamicValuesMap.get(parameter));
		}
		return (HashMap<String, String>) dynamicValuesMap;
	}

	public static void main(String arg[]) throws Exception {
		Map<String, String> responseAnvack;
		Map<String, String> checkFeaturesEnabledVOD;
		Map<String, String> checkFeaturesEnabledLive;
		String url = "https://ws-cloudpath-stage.media.nbcuni.com/cpc/ws/services/config?key=cloudpath_nbcd_ios_prod_release_gracenote_n4bfcinslip4ou2a9txj0q49wa4cpmghpn9nfa86&version=";
		String Response = SendGetPost.sendGet(url);
		JSONParser responseBody = new JSONParser();
		/*
		 * System.out.println(responseBody.getVODAnvackKey(url, Response)); //
		 * System.out.println(responseBody.getLiveAnvackKeys(url, Response));
		 * checkFeaturesEnabledVOD = JSONParser.checkFeaturesEnabledVOD(url,
		 * Response); JSONParser.checkNielsen(checkFeaturesEnabledVOD);
		 * JSONParser.checkAdobeAudience(checkFeaturesEnabledVOD);
		 * checkFeaturesEnabledLive = JSONParser.checkFeaturesEnabledLive(url,
		 * Response); JSONParser.checkNielsen(checkFeaturesEnabledLive);
		 * JSONParser.checkAdobeAudience(checkFeaturesEnabledLive);
		 * feedResponse();
		 */
		getGlobalConfig(url, Response);
	}
}
