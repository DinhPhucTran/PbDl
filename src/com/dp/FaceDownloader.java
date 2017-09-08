package com.dp;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FaceDownloader {
	
	private WebDriver driver;
	
	public FaceDownloader() {
		driver = new ChromeDriver();
	}
	
	public void download(String hardware, int page) {
		if(driver == null) {
			System.out.println("Driver null!");
			return;
		}
		
		//aplite
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.MINUTES);
		driver.navigate().to("https://apps.getpebble.com/en_US/collection/most-loved/watchfaces/" + page + "?dev_settings=true&hardware=" + hardware + "&is_browser=true&platform=android&query=");	
		
		//By appBy = By.xpath("//div[@class='screenshot']");
		By appBy = By.xpath("//a[contains(@class, 'pbl-face-li hardware-" + hardware + "')]");
		List<WebElement> appList = driver.findElements(appBy);
		int size = appList.size();
		System.out.println("Total faces: " + size);
		
		Utils.createFolder("Faces");
		WebDriverWait wait5min = new WebDriverWait(driver, 300);
		
		String name = "";
		String description = "";
		String author = "";
		String updated = "";
		String version = "";
		String hearts = "";
		String path = "";
		
		for(int i = 0; i < 2; i++) {
			//appList.get(i).click();
			try {
				WebElement appItem = driver.findElements(appBy).get(i);
				name = appItem.getAttribute("title");
				hearts = appItem.findElement(By.xpath("//div[contains(@class, 'counter ng-binding')]")).getText();
				appItem.click();
				System.out.println("---> " + i);
				
				//name = driver.findElement(By.xpath("//h1[contains(@class, 'app-title ng-binding')]")).getText();
				author = driver.findElement(By.xpath("//div[@id=\"main-view\"]/section/article/div[1]/div[1]/div/h2")).getText();
				description = driver.findElement(By.xpath("//pre[contains(@class, 'description-body')]")).getText();
				updated = driver.findElement(By.xpath("//div[@id=\"main-view\"]/section/article/ul[1]/li[3]/span[2]")).getText();
				version = driver.findElement(By.xpath("//div[@id=\"main-view\"]/section/article/ul[1]/li[4]/span[2]")).getText();
				
				path = "Faces/" + name;
				Utils.createFolder(path);
				Utils.writeInfo(path, name, name, author, description, updated, version, hearts);
				
				WebElement downloadLink = wait5min.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id=\"main-view\"]/section/article//a[contains(text(), 'Download PBW')]")));
				downloadLink.click();
				Utils.checkDownload();
				//Utils.sleep(5);
				driver.navigate().back();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		Utils.sleep(10);
		driver.close();
	}
	
}
