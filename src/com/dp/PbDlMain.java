package com.dp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PbDlMain {
	
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "C:/Users/PhucTD3/Documents/chromedriver.exe");
		
		List<WebElement> faceList = new ArrayList<WebElement>();
		
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.navigate().to("https://apps.getpebble.com/en_US/collection/most-loved/watchfaces/1?dev_settings=true&hardware=aplite&is_browser=true&platform=android&query=");	
		
		faceList = driver.findElements(By.xpath("//div[@class='screenshot']"));
		int size = faceList.size();
		System.out.println("Total faces: " + size);
		
		WebDriverWait wait5min = new WebDriverWait(driver, 300);
		
		for(int i = 0; i < size; i++) {
			wait5min.until(ExpectedConditions.elementToBeClickable(faceList.get(i)));
			faceList.get(i).click();
			System.out.println("---> " + i);
			WebElement downloadLink = wait5min.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"main-view\"]/section/article/ul[2]/li[4]/a")));
			downloadLink.click();
			driver.navigate().back();	
			
		}
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		driver.close();
	}
}
