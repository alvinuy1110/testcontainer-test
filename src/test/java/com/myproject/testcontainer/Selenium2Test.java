package com.myproject.testcontainer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URL;

import static org.testng.Assert.assertEquals;

/**
 * docker stop $(docker ps -a -q)
 * docker rm $(docker ps -a -q)
 *
 * docker run -d -p 4444:4444 -v /dev/shm:/dev/shm selenium/standalone-chrome
 *
 * docker run -d -p 4444:4444 -e START_XVFB=false -e SE_OPTS="-debug" -v /dev/shm:/dev/shm selenium/standalone-chrome
 *
 * THIS works: for some reason the start does not work in ubuntu 14!!!
 * docker run -d -p 4444:4444 --entrypoint=/opt/bin/start-selenium-standalone.sh -v /dev/shm:/dev/shm selenium/standalone-chrome
 */
@Slf4j
public class Selenium2Test {

    private static final Logger LOGGER = LoggerFactory.getLogger(Selenium2Test.class);

    @Test
    public void whenNavigatedToPage_thenHeadingIsInThePage() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        RemoteWebDriver driver = null;
        try {
            driver = new RemoteWebDriver(
                    new URL("http://localhost:4444/wd/hub")
                    , options);
            driver.get("http://example.com");
            String heading = driver.findElement(By.xpath("/html/body/div/h1"))
                    .getText();

            assertEquals("Example Domain", heading);

        } finally {
            // close tab/ window
            if  (driver!=null) {
                driver.close();
            }

            // close  the  session altogether
            try {
                driver.quit();
            } catch (Exception e) {

            }
        }




    }
}
