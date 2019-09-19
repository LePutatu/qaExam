package test.selenium.sample;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import test.selenium.sample.utils.TestSettings;
import test.selenium.sample.utils.WebDriverFactory;
import test.selenium.sample.utils.WebDriverType;

public class QAExamTest {

    private final static WebDriverType DRIVER_TYPE = WebDriverType.CHROME;

    private final static String PAGE_TITLE_XPATH = "//h2";

    private WebDriver webDriver;

    @Before
    public void setUp() throws Exception {
        webDriver = WebDriverFactory.getInstance(DRIVER_TYPE);
    }

    @After
    public void tearDown() throws Exception {
        webDriver.quit();
    }

    @Test
    public void testXSS() {

        String djangoURL = "https://docs.djangoproject.com/en/2.0/topics/security/";

        String xssURL = "https://danibudi.github.io/Cross-Site%20Scripting%20(XSS).html";

        webDriver.get(TestSettings.URL_XSS);

        Assert.assertNotNull(webDriver.findElement(By.linkText(djangoURL)));


        Assert.assertNotNull(webDriver.findElement(By.linkText(xssURL)));


        webDriver.get(xssURL);

        Assert.assertEquals(djangoURL, webDriver.getCurrentUrl());

        webDriver.navigate().back();


        // Check if there is an alert present on the page
        Alert alert = null;
        try {
            alert = webDriver.switchTo().alert();
        } catch (NoAlertPresentException nape) {
            Assert.fail("Alert was expected, but not found on page");
        }

        // Check the alert text
        Assert.assertEquals("42", alert.getText());

        // Check page title
        alert.accept();
        assertPageTitle(PAGE_TITLE_XPATH, "Cross-Site Scripting (XSS)- How it works");

        takeScreenshot("xss");
    }

    private void assertPageTitle(String elementXpath, String expectedTitle) {
        WebElement h2Element = null;
        try {
            h2Element = webDriver.findElement(By.xpath(elementXpath));
        } catch (NoSuchElementException nsee) {
            Assert.fail("Title element was not found on page");
        }

        Assert.assertEquals(expectedTitle, h2Element.getText());
    }

    private void takeScreenshot(String page) {
        String filePath = "d:\\tmp\\screenshots\\" + page + "_" + System.currentTimeMillis() + ".png";
        File screenshotFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshotFile, new File(filePath));
        } catch (IOException ioe) {
            Assert.fail("Issue with saving screenshot in file " + filePath);
        }
    }

}
