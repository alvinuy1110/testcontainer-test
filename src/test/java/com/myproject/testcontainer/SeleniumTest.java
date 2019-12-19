package com.myproject.testcontainer;

import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by pulsar on 18/12/19.
 */
@Slf4j
public class SeleniumTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumTest.class);

    @Test
    public void test1() {
        LOGGER.info("test1");
    }


    public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
            .withCapabilities(new ChromeOptions());

    @BeforeClass
    public void setupClass() {
        chrome.start();
    }
    @AfterClass
    public void teardownClass() {
        chrome.stop();
    }
    @Test
    public void whenNavigatedToPage_thenHeadingIsInThePage() {
        RemoteWebDriver driver = chrome.getWebDriver();
        driver.get("http://example.com");
        String heading = driver.findElement(By.xpath("/html/body/div/h1"))
                .getText();

        assertEquals("Example Domain", heading);
    }
}
