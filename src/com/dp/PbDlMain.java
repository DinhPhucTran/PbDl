package com.dp;

import java.util.Date;

public class PbDlMain {
	
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		
		//page max: 287
		//FaceDownloader faceDownloader = new FaceDownloader();
		/*for(int i = 7; i <= 20; i++) {
			faceDownloader.download("aplite", i);
			if((new File("Faces/" + i).list().length < 39)) {
				System.out.println("Page " + i + " has less than 39 faces!");
				Utils.createFolder("Faces/Error_" + i);
			}
		}*/
		//faceDownloader.close();
		
		/*System.out.println("Start: " + (new Date()));
		faceDownloader.download("aplite", 20);
		faceDownloader.close();
		System.out.println("Download complete: " + (new Date()));*/
		
		AppDownloader appDownloader = new AppDownloader();
		System.out.println("Start: " + (new Date()));
		appDownloader.download("aplite", 10);
		appDownloader.close();
		System.out.println("Download complete: " + (new Date()));
	}
	
}
