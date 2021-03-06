package com.dp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AppDownloader {
	private WebDriver driver;
	private String downloadPath = "D:/dl";
	
	public AppDownloader() {
		String downloadFilepath = downloadPath;
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		
		driver = new ChromeDriver(cap);
	}
	
	public void download(String hardware, int page) {
		if(driver == null) {
			System.out.println("Driver null!");
			return;
		}
		
		if((new File(downloadPath).list().length > 0)) {
			/*System.out.println("Please move or delete all files in download folder!");
			driver.close();
			return;*/
			
			try {
				FileUtils.cleanDirectory(new File(downloadPath));
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Unable to clean up download folder!");
				driver.close();
				return;
			}
		}
		
		try {
			FileUtils.cleanDirectory(new File("Apps/" + page));
		} catch (Exception e1) {
			//e1.printStackTrace();
		}
		
		//aplite
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.MINUTES);
		driver.navigate().to("https://apps.getpebble.com/en_US/collection/most-loved/watchapps/" + page + "?dev_settings=true&hardware=" + hardware + "&is_browser=true&platform=android&query=");	
		
		//By appBy = By.xpath("//div[@class='screenshot']");
		By appBy = By.xpath("//a[contains(@class, 'pbl-app-li hardware')]");
		List<WebElement> appList = driver.findElements(appBy);
		int size = appList.size();
		System.out.println("Page: " + page + ", total apps: " + size);
		
		Utils.createFolder("Apps");
		WebDriverWait wait5min = new WebDriverWait(driver, 300);
		
		String name = "";
		String oriName = "";
		String description = "";
		String author = "";
		String updated = "";
		String version = "";
		String hearts = "";
		String path = "";
		String img = "";
		String banner = "";
		String icon = "";
		String category = "";
		boolean appRequired = false;
		
		for(int i = 0; i < size; i++) {
			
			//Close download tab if previous step failed
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			if(tabs.size() > 1) {
				driver.switchTo().window(tabs.get(1));
				driver.close();
				driver.switchTo().window(tabs.get(0));
			}
			
			//appList.get(i).click();
			try {
				WebElement appItem = driver.findElements(appBy).get(i);
				oriName = appItem.getAttribute("title");
				name = oriName.replaceAll("[\\/:*?\"<>|]", "_").trim();
				hearts = appItem.findElement(By.xpath(".//div[contains(@class, 'counter ng-binding')]")).getText();
				img = appItem.findElement(By.xpath(".//div[@class='img-static-aspect']/img")).getAttribute("src");
				icon = appItem.findElement(By.xpath(".//div[@class='app-title']/img")).getAttribute("src");
				category = appItem.findElement(By.xpath(".//div[@class='app-title']/div[@class='app-category ng-binding']")).getText();
				appItem.click();
				System.out.println("---> " + i + ". " + oriName);
				
				//name = driver.findElement(By.xpath("//h1[contains(@class, 'app-title ng-binding')]")).getText();
				try {
					author = driver.findElement(By.xpath("//div[@id=\"main-view\"]/section/article/div[1]/div[1]/div/h2")).getText();
					description = driver.findElement(By.xpath("//pre[contains(@class, 'description-body')]")).getText();
					updated = driver.findElement(By.xpath("//div[@id=\"main-view\"]/section/article/ul[1]/li[3]/span[2]")).getText();
					version = driver.findElement(By.xpath("//div[@id=\"main-view\"]/section/article/ul[1]/li[4]/span[2]")).getText();					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					banner = driver.findElement(By.xpath("//img[@class='app-image']")).getAttribute("src");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					driver.findElement(By.xpath("//*[name()='svg']//*[name()='text']"));
					appRequired = true;
				} catch (Exception e) {
					appRequired = false;
				}
				
				path = "D:/PbApps/" + page + "/" + name;
				Utils.createFolder(path);
				Utils.writeAppInfo(path, name, oriName, author, category, description, updated, version, hearts, appRequired);
				Utils.downloadImage(img, path + "/" + name + ".gif");
				Utils.downloadImage(icon, path + "/icon.png");
				Utils.downloadImage(banner, path + "/banner.png");
				
				WebElement downloadLink = wait5min.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\"main-view\"]/section/article//a[contains(text(), 'Download PBW')]")));
				downloadLink.click();
				
				Utils.checkDownload(downloadPath);
				Utils.sleep(3);
				File file = Utils.getLatestFile(downloadPath);
				if(file != null) {
					file.renameTo(new File(path + "/" + file.getName()));
				}
				
				List<WebElement> screens = driver.findElements(By.xpath("//img[@class='screenshot']"));
				int s = screens.size();
				for(int j = 0; j < s; j++) {
					Utils.downloadImage(screens.get(j).getAttribute("src"), path + "/screenshots/screen" + j + ".png");
				}
				
				driver.navigate().back();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		Utils.sleep(5);		
	}
	
	public void close() {
		driver.close();
	}
}
