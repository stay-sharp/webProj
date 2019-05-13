package com.ruiyang.du.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeleniumUtils {
//    static WebDriver webDriver;
//    static String chromedriver = "/Users/yp-tc-m-7122/devTools/chromedriver/chromedriver";//chromedriver文件路径
//
//    static {
//        System.setProperty("webdriver.chrome.driver", chromedriver);
//        webDriver = loadDriver();
//    }


    public static WebDriver loadDriver(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--user-data-dir=/Users/yp-tc-m-7122/Library/Application Support/Google/Chrome/");
        options.addArguments("--headless");
        options.addArguments("--window-size=1400,1400");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriver webDriver = new ChromeDriver(options);
       /* WebDriver webDriver = new ChromeDriver();*/
        return webDriver;
    }



    public static WebElement findElementById(WebDriver webDriver, String id) {
        WebElement element = webDriver.findElement(By.id(id));
        return element;
    }

    public static WebElement findElementByClass(WebDriver webDriver, String className) {
        WebElement element = webDriver.findElement(By.className(className));
        return element;
    }

    public static void main(String[] args) {
        String input = "123";
        Pattern pattern = Pattern.compile("^([A-Za-z]+)+(\\d+)$");
        Matcher matcher = pattern.matcher(input);
        System.out.println(matcher.find());

    }
}
