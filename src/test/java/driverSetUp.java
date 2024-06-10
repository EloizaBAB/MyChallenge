import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class driverSetUp {

    WebDriver driver;

    public driverSetUp() {
        WebDriverManager.chromedriver().setup();
        this.driver = new ChromeDriver();
    }
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Invoke browser
    public void invokeBrowser() {
        driver.get("https://www.mbway.pt/");
        driver.manage().window().maximize();
    }

    // Go to Eventos and count the number of events by getting the number of elements with the Eventos xpath
    public void countNumberOfEvents() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement buttonEventos = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='EVENTOS']")));
        buttonEventos.click();
        List<WebElement> eventosElements = driver.findElements(By.xpath("//a[@class='eventos']"));
        System.out.println("Número de elementos com a classe 'eventos': " + eventosElements.size());
    }

    // See security recommendations text and validate if message is correct
    public void checkSecurityRecommendations() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement securityButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='SEGURANÇA']")));
        securityButton.click();
        WebElement securityRButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='RECOMENDAÇÕES DE SEGURANÇA']")));
        securityRButton.click();
        WebElement h3Element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[text()='Mensagens de segurança MB WAY']")));
        String h3Message = "Mensagens de segurança MB WAY";
        assertEquals(h3Message, h3Element.getText());
    }

    //test if the navigation links go to the correct page
    public void testMainNavigationLinks() {
        Map<String, String> navigationLinks = new HashMap<>();
        navigationLinks.put("EMPRESAS", "https://www.mbway.pt/mb-way-empresas/");
        navigationLinks.put("SEGURANÇA", "https://www.mbway.pt/seguranca-mb-way/");
        navigationLinks.put("PROMOÇÕES", "https://www.mbway.pt/promos/");
        navigationLinks.put("NOVIDADES", "https://www.mbway.pt/novidades-mb-way/");

        for (Map.Entry<String, String> entry : navigationLinks.entrySet()) {
            String linkText = entry.getKey();
            String expectedUrl = entry.getValue();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement navLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));
        navLink.click();

        wait.until(ExpectedConditions.urlToBe(expectedUrl));
        String currentUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, currentUrl, "URL did not match for link: " + linkText);
        driver.navigate().back();
        }
    }

        public static void main (String[]args){
            driverSetUp test = new driverSetUp();
            test.invokeBrowser();
            test.countNumberOfEvents();
            test.checkSecurityRecommendations();
            test.testMainNavigationLinks();
            test.tearDown();
        }
    }

