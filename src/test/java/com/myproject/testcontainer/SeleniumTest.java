package com.myproject.testcontainer;

import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;
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


    public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer<>()
            .withNetwork(buildNetwork())

            // TODO modify the entry point
            // TODO if boot up takes long the same port maybe used up!!
            // based on info, still calling cmd={/opt/bin/entry_point.sh}
            .withCreateContainerCmdModifier(cmd -> {

                cmd.withCmd("");
                cmd.withEntrypoint("/opt/bin/start-selenium-standalone.sh");
                    }
            )

            // TODO for some reason, becuase we override the entrypoint, the containers keeps attempting
            .withStartupAttempts(1)

            .withCapabilities(new ChromeOptions())

            //// change  the waiting  strategy
//            .withExposedPorts(4444)6

            // TODO does not seem to work...
            .withStartupTimeout(Duration.of(60, SECONDS))
            //.waitingFor(Wait.forHealthcheck())
            //.waitingFor(Wait.forHttp("/wd/hub/status").forStatusCode(200))

            // TODO try logwait strategy
            //02:04:24.120 INFO [SeleniumServer.boot] - Selenium Server is up and running on port 4444

    ;

    @BeforeClass
    public void setupClass() {
        chrome.start();
    }
    @AfterClass
    public void teardownClass() {
        chrome.stop();
    }

    private Network buildNetwork() {
        return Network.builder().id("host").build();
    }
    @Test
    public void whenNavigatedToPage_thenHeadingIsInThePage() {
        RemoteWebDriver driver = null;

        try {
            driver = chrome.getWebDriver();
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
