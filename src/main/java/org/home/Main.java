package org.home;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class Main
{
    private static WebDriver driver;

    public static void main(String[] args)
    {
        config();

        try {Thread.sleep(1000);}
        catch (Exception e) {e.printStackTrace();}

        Scraper scraper = new Scraper(driver, 10);
        scraper.run();
    }

    private static void config()
    {
        System.setProperty("webdriver.driver.geckodriver", "src/main/resources/geckodriver.exe");
        System.setProperty("intl.accept_languages", "en-US");

        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("intl.accept_languages", "en-US");

        driver = new FirefoxDriver(options);

        //addExtension();
    }

    private static void addExtension()
    {
        driver.get("https://addons.mozilla.org/en-US/firefox/addon/adblock-for-firefox/");

        try{Thread.sleep(2000);}
        catch (Exception e) {e.printStackTrace();}

        WebElement button = driver.findElement(By.xpath("//a[@class='Button Button--action AMInstallButton-button Button--puffy']"));
        button.click();

        try{Thread.sleep(3000);}
        catch (Exception e) {e.printStackTrace();}

        try
        {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_E);
            robot.keyRelease(KeyEvent.VK_ALT);

            try{Thread.sleep(3000);}
            catch (Exception e) {e.printStackTrace();}

            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_T);
            robot.keyRelease(KeyEvent.VK_ALT);

            try{Thread.sleep(3000);}
            catch (Exception e) {e.printStackTrace();}
        }
        catch (AWTException e) {e.printStackTrace();}
    }
}