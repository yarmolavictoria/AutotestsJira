package TestJira;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;

public class AutoTest {

    static WebDriver browser;
    static String baseURL = "http://jira.hillel.it:8080/";
    static String userName = "yarmolavictoria";
    static String userPass = "qwerty123";
    static String taskSummary = "Test_AQA_issue ";

    @BeforeTest
    public void openChrome() {
        System.setProperty("webdriver.chrome.driver", "C:/Users/Vika/Downloads/chromedriver_win32/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--incognito");
        browser = new ChromeDriver(options);
        browser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test(description = "Login to Jira - Negative scenario", priority = 1)
    public void loginToJiraNegative  () throws InterruptedException  {
        browser.get(baseURL);
        clearAndFill(By.id("login-form-username"), userName);
        clearAndFill(By.id("login-form-password"), userPass + 1).submit();
        Thread.sleep(5000);
        Assert.assertTrue(browser.findElement(By.id("usernameerror"))!= null);

    }
    @Test(description = "Login to Jira - Positive scenario", priority = 2)
    public void loginToJiraPositive () {
        browser.get(baseURL);
        clearAndFill(By.id("login-form-username"), userName);
        clearAndFill(By.id("login-form-password"), userPass).submit();
        String pageData = browser.findElement(By.id("header-details-user-fullname")).getAttribute("data-username");
        Assert.assertEquals(pageData, userName);
    }

    @Test(description = "Create Issue in GQR Project", priority = 3)
    public void createIssue () throws InterruptedException {
        browser.findElement(By.id("create_link")).click();
        clearAndFill(By.id("project-field"),"General QA Robert (GQR)"+"\n");
        Thread.sleep(5000);
        browser.findElement(By.id("summary")).sendKeys(taskSummary);
        browser.findElement(By.id("create-issue-submit")).click();
        WebElement successMessage = browser.findElement(By.className("issue-created-key"));
        Assert.assertTrue(successMessage != null);
    }

    @Test(description = "Open Issue", priority = 4)
    public void openIssue() {
        String link = (browser.findElement(By.className("issue-created-key")).getAttribute("href"));
        browser.get(link);
        WebElement issueLink = browser.findElement(By.id("key-val"));
        Assert.assertTrue( issueLink != null);
    }


    @AfterTest
    public void closeChrome() throws InterruptedException {
        Thread.sleep(10000);
        browser.quit();
    }

    private static WebElement clearAndFill(By selector, String data) {
        WebElement element = browser.findElement(selector);
        element.clear();
        element.sendKeys(data);
        return element;
    }

}
