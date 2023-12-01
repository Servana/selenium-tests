package org.servana.selenium.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.List;

public class ChromeDriverTest {

    private String testUrl;
    private WebDriver driver;

    @Before
    public void prepare() {
        //setup chromedriver
        System.setProperty(
                "webdriver.chrome.driver",
                "chromedriver-linux64/chromedriver");
        options.setBinary("/usr/bin/google-chrome-stable");
        testUrl = "https://google.com";
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        driver.get(testUrl);

    }

    @Test
    public void testTitle() throws IOException {

        List<WebElement> elements = driver
                .findElements(By.cssSelector("[lang=\"READ_MORE_BTN\"]"));

    }

    @After
    public void teardown() throws IOException {
        driver.quit();
    }

}

116.0.5845.179