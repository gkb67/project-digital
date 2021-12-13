import java.util.List;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestRedirection {

	WebDriver driver;
	private int invalidLinksCount;
	private int validLinksCount;
	private String link = "https://www.vanmoof.com/s3";
	PrintWriter writer;

	/*
	 * This function executes before each Test tag in redirection.xml
	 * 
	 * @param browser
	 * 
	 * @throws Exception
	 */
	@BeforeTest
	@Parameters("browser")
	public void setup(String browserName) throws Exception {
		if (browserName.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", "SeleniumDrivers/geckodriver");
			driver = new FirefoxDriver();
		} else if (browserName.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "SeleniumDrivers/chromedriver");
			driver = new ChromeDriver();
		} else if (browserName.equalsIgnoreCase("safari")) {
			driver = new SafariDriver();
		}
	}

	/*
	 * Test to check if the link is redirected to the exact given link
	 */
	@Test
	public void redirectionWithDefaultColor() throws InterruptedException {
		String expectedLink = "https://www.vanmoof.com/en-NL/s3";
		driver.get(link); // To open the URL
		Thread.sleep(3000); // To suspend the code for a specific amount of time
		String redirectLink = driver.getCurrentUrl();
		Assert.assertTrue(redirectLink.contains(expectedLink));
	}

	/*
	 * Test to check if the link is redirected to the URL which has the given link
	 */
	@Test
	public void redirectionWithoutDefaultColor() throws InterruptedException {
		String expectedLink = "https://www.vanmoof.com/en-NL/s3";
		driver.get(link);
		Thread.sleep(3000);
		String redirectLink = driver.getCurrentUrl();
		Assert.assertEquals(redirectLink, expectedLink);
	}

	/*
	 * Test to check if each link in the page opens successfully The 'for' loop goes
	 * through each list element which corresponds to a link and checks its validity
	 */
	@Test
	public void linksInThePage() throws FileNotFoundException, Exception {
		driver.get(link);
		// To create a txt file for displaying all the links with response codes in a
		// document
		writer = new PrintWriter("VerifyLinksReport.txt", "UTF-8");

		try {
			invalidLinksCount = 0;
			validLinksCount = 0;
			List<WebElement> list = driver.findElements(By.tagName("a"));
			for (WebElement e : list) {
				if (e != null) {
					String url = e.getAttribute("href");
					if (url != null) {
						verifyLinks(url);
						validLinksCount++;
					} else {
						invalidLinksCount++;
					}
				}
			}

			// if there is an invalid link in the page, the test fails
			if (invalidLinksCount > 0)
				Assert.assertTrue(false);
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	/*
	 * Method to check the response code of the links in the page
	 */
	public void verifyLinks(String linkUrl) {
		try {
			URL url = new URL(linkUrl);
			// Create URL connection and get the response code
			HttpURLConnection httpURLConnect = (HttpURLConnection) url.openConnection();
			httpURLConnect.setConnectTimeout(5000);
			httpURLConnect.connect();
			// Check the status code
			if (httpURLConnect.getResponseCode() != 200) {
				invalidLinksCount++;
				writer.println(linkUrl + " cannot open successfully - " + httpURLConnect.getResponseCode()
						+ httpURLConnect.getResponseMessage());
			}

			else {
				writer.println(linkUrl + " opens successfully - " + httpURLConnect.getResponseCode()
						+ httpURLConnect.getResponseMessage());
			}

		} catch (Exception e) {

		}
	}

	/*
	 * This function executes at the end of the tests to close the browsers
	 * automatically
	 */
	@AfterTest
	public void quitDriver() {
		driver.close();
	}
}