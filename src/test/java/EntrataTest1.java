import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class EntrataTest1 {
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(10));

    @BeforeTest
    public void launchBrowser() throws InterruptedException {
        driver.manage().window().maximize();
        //Open the Url
        driver.get("https://www.entrata.com/");


    }
    @Test // 1st Test case - To verify the Watch Demo functionality
    public void verifyWatchDemoPageTitle() throws InterruptedException {

        WebElement demo= driver.findElement(By.xpath("//a[text()='Watch Demo']")); //to capture locator for watch demo button
        // applying explicit wait to hold the execution until give condition is met
        wait.until(ExpectedConditions.elementToBeClickable(demo));
        demo.click();
        WebElement header= driver.findElement(By.cssSelector(".mktoImg"));
        wait.until(ExpectedConditions.visibilityOf(header));
        // Capture the text on the Watch Demo page
        String demotext= driver.findElement(By.xpath("//*[@id = 'Banner_Title']")).getText();
        //verifying if the given text and text showing on page is matching
        Assert.assertEquals(demotext, "Optimize property management with\n" + "one platform");
    }
    @Test(dependsOnMethods = "verifyWatchDemoPageTitle")// // Verify the Form functionality on the Watch Demo page
    public void verifyForm (){

        //Locate the textbox on the form page and submit the details
        WebElement firstname=  driver.findElement(By.cssSelector("#FirstName"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(firstname));
        firstname.sendKeys("Bhavesh");
        driver.findElement(By.id("LastName")).sendKeys("Chauhan");
        driver.findElement(By.id("Email")).sendKeys("heybhavesh08@gmail.com");
        driver.findElement(By.xpath("//*[@id= 'Company']")).sendKeys("Infosys Limited Pune");
        driver.findElement(By.xpath("//*[@id= 'Phone' and @name ='Phone']")).sendKeys("8275119867");
        Select dd = new Select(driver.findElement(By.id("Unit_Count__c")));
        dd.selectByIndex(1);
        driver.findElement(By.xpath("//*[@id= 'Title' and @type = 'text']")).sendKeys("Technology Analyst");
        driver.findElement(By.cssSelector("[type='submit'].mktoButton")); // .click()
    }


    @Test(dependsOnMethods = "verifyForm") //Verify the broken links on the homepage
    public void verifyBrokenlinks() throws IOException, InterruptedException {

        driver.get("https://www.entrata.com/");
        //adding implicit wait to hold the execution for 5 seconds
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        // store the links present on the page
         List <WebElement> allLinks = driver.findElements(By.tagName("a"));
        SoftAssert a = new SoftAssert();
        for (WebElement link : allLinks){
            String url= link.getAttribute("href");
            if (url!= null && !url.isEmpty()) {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("HEAD");
                conn.connect();
                int respCode = conn.getResponseCode();
                a.assertTrue(respCode > 400, "The link with the text" + link.getText() + " is broken" + respCode);
            }
        }

    }
    @AfterTest
    public void closeBrowser(){
       driver.quit();

    }
}
