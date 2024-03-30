package atvd.lr2;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;


import java.time.Duration;

public class mytest {
    private WebDriver chromeDriver;
    private static final String baseUrl = "https://www.olx.ua/uk/";
    @BeforeClass(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");
        options.setImplicitWaitTimeout(Duration.ofSeconds(15));
        this.chromeDriver = new ChromeDriver(options);
    }
    @BeforeMethod
    public void precondition() {
        chromeDriver.get(baseUrl);
    }
    @AfterClass(alwaysRun = true)
    public void tearDown() {
        chromeDriver.quit();
    }
    @Test
    public void testClick() {
        WebElement dogButton = chromeDriver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div[3]/div/div/div/a[7]"));
        Assert.assertNotNull(dogButton);
        dogButton.click();
        Assert.assertEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }
    @Test
    public void testSearchField() {
        WebElement searchField = chromeDriver.findElement(By.tagName("input"));
        Assert.assertNotNull(searchField);

        System.out.println(String.format("Placeholder attribute: %s", searchField.getAttribute("placeholder")) +
                String.format("\nID attribute: %s", searchField.getAttribute("id")) +
                String.format("\nType attribute: %s", searchField.getAttribute("type")) +
                String.format("\nPosition: (%d; %d)", searchField.getLocation().x, searchField.getLocation().y) +
                String.format("\nSize: %dx%d", searchField.getSize().height, searchField.getSize().width));
        String inputValue = "Кошеня";
        searchField.sendKeys(inputValue);
        Assert.assertEquals(searchField.getAttribute("value"), inputValue);
        System.out.println(String.format(searchField.getAttribute("value")));
    }
    @Test
    public void testXpath() {
        WebElement element = chromeDriver.findElement(By.xpath("//*[@id=\"location-input\"]"));
        Assert.assertNotNull(element);
        System.out.println("Знайдено елемент: " + element.getTagName());
        System.out.println(String.format("ID attribute: %s", element.getAttribute("id")));

        WebElement secondElement = chromeDriver.findElement(By.xpath("//a[contains(@class, 'css-o73al1')]"));
        Assert.assertNotNull(element);
        System.out.println("Знайдено елемент: " + secondElement.getTagName());
        System.out.println(String.format("Data path: %s", secondElement.getAttribute("data-path")));

        Assert.assertEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }
    @Test
    public void testPageNavigation() {
        WebElement button = chromeDriver.findElement(By.xpath("//*[@id=\"searchmain-container\"]/div[3]/div/div/div/a[15]"));
        button.click();
        String expectedUrl = "https://www.olx.ua/uk/obmen-barter/";
        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));

        String currentUrl = chromeDriver.getCurrentUrl();

        Assert.assertEquals(expectedUrl, currentUrl);
    }
    @Test
    public void testCondition() {
        WebElement textElement = chromeDriver.findElement(By.className("css-1rwzo2t"));

        String actualText = textElement.getText();
        String expectedText = "Тварини";

        System.out.println("Фактичний текст: " + actualText);

        Assert.assertTrue(actualText.contains(expectedText));
    }
}
