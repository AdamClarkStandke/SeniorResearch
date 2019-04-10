package sr_research;

import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Selenium2Example {

	public static void main(String[] args) 
	{

        // Notice that the remainder of the code relies on the interface, 
        // not the implementation.
		//WebDriver driver = new ChromeDriver();
		WebDriver driver2 = new HtmlUnitDriver();
        // And now use this to visit Google
        //driver.get("https://www.cnn.com/2019/04/09/politics/ed-dept-texas-tech/index.html");
        driver2.get("https://www.cnn.com/2019/04/09/politics/ed-dept-texas-tech/index.html");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        // Find the text input element by its name
        

        // Enter something to search for
       

        // Now submit the form. WebDriver will find the form for us from the element
       

        // Check the title of the page
       
        
        // Google's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
        (new WebDriverWait(driver2, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
            	List<WebElement> cheeses =  d.findElements(By.tagName("div"));
            	Iterator<WebElement> x =cheeses.iterator(); 
            	while(x.hasNext())
            	{
            		WebElement div = x.next(); 
          
            	}
				return null; 
            }
        });

        // Should see: "cheese! - Google Search"
 
        
        //Close the browser
        driver2.quit();

	}

}
