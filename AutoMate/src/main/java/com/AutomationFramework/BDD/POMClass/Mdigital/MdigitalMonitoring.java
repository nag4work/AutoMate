package com.AutomationFramework.BDD.POMClass.Mdigital;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class MdigitalMonitoring {
	

	
	public MdigitalMonitoring(WebDriver driver,int timeout) {
	PageFactory.initElements(new AjaxElementLocatorFactory(driver,timeout),this);
	}


	@FindBy(xpath="//*[@aria-label='Shop Menu']")
	public static WebElement btnShop_Menu;
	
	@FindBy(xpath="//*[contains(text(),'Shop Used Cars')]")
	public static WebElement btnShopUsedcars;
	
	@FindBy(xpath="//*[contains(text(),' Used Cars For Sale')]")
	public static WebElement txtgetUsedCarsNumbers;
	
	@FindBy(xpath="//*[@id='maincontent']/div/div[1]/div/div[1]/div/button[2]/span")
	public static WebElement btnShopNewcars;
	
	@FindBy(xpath="//*[contains(text(),' New Cars For Sale')]")
	public static WebElement txtgetNewCarsNumbers;



}
