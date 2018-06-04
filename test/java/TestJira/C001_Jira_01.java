package P001_Jira;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class C001_Jira_01 {

    static Long startTime;
    static WebDriver browser;

    static String baseURL = "http://jira.hillel.it:8080/";
    static String userName = "ivnzak";
    static String userPass = "123Qwerty";

    static String userNameWrong = "qqq";
    static String userPassWrong = "111";

    static String dataStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
    static String taskSummary = "Test_AQA_issue: " + dataStamp;


    @BeforeTest
    public void openChrome() {

        startTime = System.currentTimeMillis();

        System.setProperty("webdriver.chrome.driver", "/Users/ivanzakoretskyi/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--incognito");

        browser = new ChromeDriver(options);

        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }


    @AfterTest
    public void closeChrome() {

        browser.quit();

        // my additional debug info
        System.out.println("> Time used: " + "\t" + (System.currentTimeMillis() - startTime) + " milliseconds");
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("> Memory Used: " + "\t" + usedBytes + " bytes (" + usedBytes / 1048576.0 + " Megabytes)");

    }

//
//    @DataProvider (name = "just for example")
//    public static Object[][] dataProvider1() {
//        return new Object[][]{{"ivnzak", "123"}, {"ivnzak", "123Qwerty"},};
//    }


    @Test(description = "Login to Jira - Positive scenario", priority = 1)
    public void TC001() {

        browser.get(baseURL);
        clearAndFill(By.cssSelector("input#login-form-username"), userName);
        clearAndFill(By.cssSelector("input#login-form-password"), userPass).submit();

        String pageData = browser.findElement(By.cssSelector("#header-details-user-fullname")).getAttribute("data-username");
        System.out.println("0:: pageData: " + pageData);

        Assert.assertEquals(userName, pageData);
        Assert.assertTrue(pageData.contains(userName), "Negative case message: TC001");

    }


    @Test(description = "Create Issue in GQR Project", priority = 2)
    public void TC002() throws InterruptedException {

        browser.get("http://jira.hillel.it:8080/secure/RapidBoard.jspa?rapidView=3&projectKey=GQR");

        browser.findElement(By.id("create_link")).click();

        browser.findElement(By.id("summary")).sendKeys(taskSummary);
        browser.findElement(By.id("assign-to-me-trigger")).click();
        browser.findElement(By.id("create-issue-submit")).click();

        System.out.println("1:: taskSummary: " + taskSummary);

        clearAndFill(By.cssSelector("input#quickSearchInput"), taskSummary + "\n");
        String pageData2 = browser.findElement(By.cssSelector("#summary-val")).getText();

        System.out.println("2:: pageData2: " + pageData2);

        Assert.assertEquals(taskSummary, pageData2);
        Assert.assertTrue(pageData2.contains(taskSummary), "Negative case message: TC002");

    }


    @Test(description = "Open Issue", priority = 3)
    public void TC003() {

        String pageData3 = browser.findElement(By.cssSelector("#key-val")).getAttribute("data-issue-key");
        System.out.println("3:: pageData3: " + pageData3);

        browser.get("http://jira.hillel.it:8080/browse/" + pageData3);

        String pageData2 = browser.findElement(By.cssSelector("#summary-val")).getText();
        Assert.assertTrue(pageData2.contains(taskSummary), "Negative case message: TC003");
    }


    @Test(description = "Login to Jira - Negative scenario", priority = 4)
    public void TC004() {

        String logOut = (browser.findElement(By.id("log_out")).getAttribute("href"));
        browser.get(logOut);

        browser.get(baseURL);
        clearAndFill(By.cssSelector("input#login-form-username"), userNameWrong);
        clearAndFill(By.cssSelector("input#login-form-password"), userPassWrong).submit();

        String pageData4 = browser.findElement(By.cssSelector("#usernameerror")).getText();
        System.out.println("4:: pageData: " + pageData4);

        Assert.assertTrue(pageData4.contains("Sorry, your username and password are incorrect - please try again."), "Negative case message: TC004");

    }


    // Robert's feature
    // id   - "input#login-form-password"
    // name - "input[name=max]"
    private static WebElement clearAndFill(By selector, String data) {
        WebElement element = browser.findElement(selector);
        element.clear();
        element.sendKeys(data);

        return element;
    }


}
