package com.dp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

public class Utils {
	
	public static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void createFolder(String name) {
		new File(name).mkdirs();
	}
	
	public static void writeInfo(String path, String fileName, String appName, String author, String des, String updateTime, String version, String hearts) {
		try{
		    PrintWriter writer = new PrintWriter(path + "/" + fileName + ".txt", "UTF-8");
		    writer.println("App name: " + appName);
		    writer.println("Author: " + author);
		    writer.println("Description: " + des);
		    writer.println("Updated: " + updateTime);
		    writer.println("Version: " + version);
		    writer.println("Hearts: " + hearts);
		    writer.close();
		} catch (IOException e) {
		   // do something
		}
	}
	
	public static void writeAppInfo(String path, String fileName, String appName, String author, String category, String des, String updateTime, String version, String hearts, boolean appRequired) {
		try{
		    PrintWriter writer = new PrintWriter(path + "/" + fileName + ".txt", "UTF-8");
		    writer.println("App name: " + appName);
		    writer.println("Author: " + author);
		    writer.println("Category: " + category);
		    writer.println("Description: " + des);
		    writer.println("Updated: " + updateTime);
		    writer.println("Version: " + version);
		    writer.println("Hearts: " + hearts);
		    if(appRequired) {
		    	writer.println("Companion app required.");
		    }
		    writer.close();
		} catch (IOException e) {
		}
	}
	
	public static boolean isDownloadCompeleted() {
		String downloadsPath = System.getProperty("user.home") + "/Downloads";
		long startTime = System.currentTimeMillis();
		
		while (System.currentTimeMillis() - startTime < 60000) {
			if(getLatestFile(downloadsPath).getName().endsWith(".pbw")) {
				return true;
			}
			sleep(2);
		}
		return false;
	}
	
	public static void checkDownload() {
		String downloadsPath = System.getProperty("user.home") + "/Downloads";
		long startTime = System.currentTimeMillis();
		
		while (System.currentTimeMillis() - startTime < 60000) {
			File file = getLatestFile(downloadsPath);
			if(file != null) {
				if(file.getName().endsWith(".pbw")) {
					break;
				} else {
					sleep(2);
				}
			}
		}
	}
	
	public static void checkDownload(String downloadPath) {
		long startTime = System.currentTimeMillis();
		System.out.print("---------");
		long t = 0;
		while (System.currentTimeMillis() - startTime < 120000) {
			File file = getLatestFile(downloadPath);
			if(file != null) {
				if(!file.getName().endsWith("crdownload")) {
					break;
				} else {
					t = (System.currentTimeMillis() - startTime) / 1000;
					System.out.print(" " + t);
					sleep(1);
				}
			} else {
				sleep(1);
			}
		}
		System.out.println("");
	}
	
	public static File getLatestFile(String path) {
		File dir = new File(path);
	    File[] files = dir.listFiles();
	    if (files == null || files.length == 0) {
	        return null;
	    }
	    
	    File lastModifiedFile = files[0];
	    for (int i = 1; i < files.length; i++) {
	       if (lastModifiedFile.lastModified() < files[i].lastModified()) {
	           lastModifiedFile = files[i];
	       }
	    }
	    return lastModifiedFile;
	}
	
	public static void saveImage(String url, String path) {
		try {
			InputStream inputStream = new URL(url).openStream();
			Files.copy(inputStream, Paths.get(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void downloadImage(String url, String path) {
		try {
			FileUtils.copyURLToFile(new URL(url), new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
