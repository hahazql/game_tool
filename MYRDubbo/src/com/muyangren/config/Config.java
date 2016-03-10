package com.muyangren.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class Config {

	public static Config instance = new Config();

	private static Properties prop = new Properties();
	
	

	static {
		try {
//			String pro = getAppRoot() + "/app.properties";
//			System.out.println("pro>>>>>>>"+pro);
			String pro="app.properties";
			InputStream in = new BufferedInputStream(new FileInputStream(pro));
			prop.load(in);
			} catch (IOException e) {
			System.out.println("Config>>>>"+e.toString());

		}
	}

	public static String getAppRoot() {
		return System.getProperty("user.dir");
	}

	public static String get(String key) {
		String v = prop.getProperty(key);
		if (v == null)
			return v;
		return v.trim();
	}

	public static String get(String key, String def) {
		String v = prop.getProperty(key, def);
		if (v == null)
			return v;
		return v.trim();
	}
	
	
	
	
	public static void main(String[] args) {
		
		System.out.println("ini>>>>>"+get("server"));
		
		
		
	}

}
