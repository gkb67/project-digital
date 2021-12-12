import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;


public class TestRedirection {

	private WebDriver driver;
	private int invalidLinksCount;

	
	@Test
	public void redirectionWithDefaultColor() {
		
		System.setProperty("webdriver.chrome.driver", "/Users/gozde/Documents/Selenium/chromedriver");
		driver = new ChromeDriver();
		
		String link = "https://vanmoof.com/s3";
		String expectedLink = "https://www.vanmoof.com/en-NL/s3";
		
		driver.get(link);
		
		String redirectLink = driver.getCurrentUrl();
		
		System.out.println(redirectLink);
		
		Assert.assertTrue(redirectLink.contains(expectedLink));
	}
	
	@Test
	public void redirectionWithoutDefaultColor() {
		
		System.setProperty("webdriver.chrome.driver", "/Users/gozde/Documents/Selenium/chromedriver");
		driver = new ChromeDriver(); //webdriver is interface
		
		String link = "https://vanmoof.com/s3";
		String expectedLink = "https://www.vanmoof.com/en-NL/s3";
		
		driver.get(link);
		
		String redirectLink = driver.getCurrentUrl();
		
		System.out.println(redirectLink);
		
		Assert.assertEquals(redirectLink,expectedLink);
	}
	
	@Test
	public void LinksInThePage() {
		System.setProperty("webdriver.chrome.driver", "/Users/gozde/Documents/Selenium/chromedriver");
		driver = new ChromeDriver();
		
		String link = "https://vanmoof.com/en-NL/s3";
		
		driver.get(link);
		try {
			invalidLinksCount = 0;
			List<WebElement> list = driver.findElements(By
					.tagName("a"));
			System.out.println("Total number of links are "
					+ list.size());
			for (WebElement e : list) {
				if (e != null) {
					String url = e.getAttribute("href");
					if (url != null) {
						verifyLinks(url);
					} else {
						invalidLinksCount++;
					}
				}
			}

			System.out.println("Total number of invalid links are "
					+ invalidLinksCount);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	
	
	public void verifyLinks(String linkUrl)
    {
        try
        {
            URL url = new URL(linkUrl);

            //Create URL connection and get the response code
            HttpURLConnection httpURLConnect=(HttpURLConnection)url.openConnection();
            httpURLConnect.setConnectTimeout(5000);
            httpURLConnect.connect();
            
            //Check the status code
            if(httpURLConnect.getResponseCode()!=200)
            {
            	System.out.println(linkUrl + " cannot open successfully - " + httpURLConnect.getResponseCode() + httpURLConnect.getResponseMessage());
            	invalidLinksCount++;
            }    
       
            else
            {
                System.out.println(linkUrl + " opens successfully - " + httpURLConnect.getResponseCode() + httpURLConnect.getResponseMessage());
            }
        }
            catch (Exception e) {
            	
      }
        }
}