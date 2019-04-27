package com.github.qq275860560.common.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author jiangyuanlin@163.com
 * webdriver工具类 
 * 创建FirefoxDriver(有界面) ,先启动本地的geckodriver，启动命令为geckodriver.exe --host 0.0.0.0,然后修改配置文件webdriver.properties的firefoxDriverUrl为http://127.0.0.1:4444
 * 创建ChromeDriver(有界面) ,先启动本地的chromedriver，启动命令为chromedriver  --whitelisted-ips 0.0.0.0,然后修改配置文件webdriver.properties的chromeDriverUrl为http://127.0.0.1:9515
 * 创建FirefoxDriver(有界面) ,先启动本地的IEDriverServer，启动命令为IEDriverServer IEDriverServer.exe  /whitelisted-ips=0.0.0.0/0,然后修改配置文件webdriver.properties的ieDriverUrl为http://127.0.0.1:5555
 * 创建HtmlUnitDriver(无界面) 
 * 关闭driver
 * 判断元素是否存在 *
 */

public class WebDriverUtil {
	private static Log log = LogFactory.getLog(WebDriverUtil.class);

	private WebDriverUtil() {
	}

	private static String chromeDriverUrl;
	private static String firefoxDriverUrl;
	private static String ieDriverUrl;

	static {
		try {
			Configuration configuration = new Configurations().properties(new File("/", "webdriver.properties"));
			chromeDriverUrl = configuration.getString("chromeDriverUrl");
			firefoxDriverUrl = configuration.getString("firefoxDriverUrl");
			ieDriverUrl = configuration.getString("ieDriverUrl");
		} catch (Exception e) {
			log.error("", e);
			System.exit(1);// 配置不准确，直接退出
		}
	}

	public static boolean checkElementIsExist(WebDriver driver, By selector) {
		try {
			WebElement webElement = driver.findElement(selector);
			if (webElement == null)
				return false;
			else
				return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean checkElementIsClickable(WebDriver driver, By by) {
		try {
			new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(by));
		} catch (WebDriverException ex) {
			return false;
		}
		return true;
	}

	public static boolean checkElementIsVisibility(WebDriver driver, By by) {
		try {
			new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (WebDriverException ex) {
			return false;
		}
		return true;
	}

	/**
	 * 创建HtmlUnitDriver
	 * 
	 * @param driver
	 * @param selector
	 * @return
	 */
	public static WebDriver createHtmlUnitDriver() {
		DesiredCapabilities desiredCapabilities = DesiredCapabilities.htmlUnit();
		// desiredCapabilities.setCapability("loadImages", false);
		return new HtmlUnitDriver(desiredCapabilities);
	}

	public static WebDriver createHtmlUnitDriver(String url) {
		WebDriver driver = createHtmlUnitDriver();
		driver.get(url);
		return driver;
	}

	/**
	 * 创建ChromeDriver
	 * 
	 * @param driver
	 * @param selector
	 * @return
	 * @throws MalformedURLException 
	 */
	public static WebDriver createChromeDriver() throws MalformedURLException {
		if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {
			System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver");
		} else if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver.exe");
		}
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--test-type", "--ignore-certificate-errors");
		// WebDriver driver = new ChromeDriver(options);
		DesiredCapabilities dc = DesiredCapabilities.chrome();
		dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		dc.setCapability(ChromeOptions.CAPABILITY, options);
		WebDriver driver = new RemoteWebDriver(new URL(chromeDriverUrl), dc);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(driver);
		eventFiringWebDriver.register(new MyWebDriverEventListener());
		return eventFiringWebDriver;
	}

	/**
	 * 创建FirefoxDriver
	 * 
	 * @param driver
	 * @param selector
	 * @return
	 */

	public static WebDriver createFirefoxDriver() throws Exception {
		if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {
			System.setProperty("webdriver.gecko.driver", "src\\test\\resources\\geckodriver");
		} else if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			System.setProperty("webdriver.gecko.driver", "src\\test\\resources\\geckodriver.exe");
		}
		FirefoxOptions options = new FirefoxOptions();
		// options.addArguments("--test-type", "--ignore-certificate-errors");
		// WebDriver driver = new FirefoxDriver(options);
		DesiredCapabilities dc = DesiredCapabilities.firefox();
		dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		dc.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
		WebDriver driver = new RemoteWebDriver(new URL(firefoxDriverUrl), dc);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(driver);
		eventFiringWebDriver.register(new MyWebDriverEventListener());
		return eventFiringWebDriver;
	}

	/**
	 * 创建IEDriver
	 * 
	 * @param driver
	 * @param selector
	 * @return
	 */

	public static WebDriver createIEDriver() throws Exception {
		// if (System.getProperty("os.name").toLowerCase().startsWith("linux"))
		// {
		// System.setProperty("webdriver.gecko.driver",
		// "src\\test\\resources\\geckodriver");
		// } else if
		// (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
		System.setProperty("webdriver.ie.driver", "src\\test\\resources\\IEDriverServer.exe");
		// }

		DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
		dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		dc.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		WebDriver driver = new RemoteWebDriver(new URL(ieDriverUrl), dc);
		// WebDriver driver = new InternetExplorerDriver(dc);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(driver);
		eventFiringWebDriver.register(new MyWebDriverEventListener());
		return eventFiringWebDriver;
	}

	/**
	 * 关闭浏览器
	 * 
	 * @param driver
	 * @param selector
	 * @return
	 */
	public static void closeDriver(WebDriver driver) {
		driver.close();
		if (driver instanceof RemoteWebDriver
				&& ((RemoteWebDriver) driver).getCapabilities().getBrowserName().toLowerCase().contains("chrome")) {
			driver.quit();
		}

		try {
			if (driver != null) {
				if (!((RemoteWebDriver) driver).getCapabilities().getBrowserName().toLowerCase().contains("firefox")) {
					driver.quit();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 模拟浏览器退出
	 */
	public static void quit(WebDriver driver) {
		driver.quit();
	}

	/**
	 * 模拟浏览器关闭
	 */
	public static void close(WebDriver driver) {
		driver.close();
	}

	/**
	 * 模拟浏览器返回
	 */
	public static void back(WebDriver driver) {
		driver.navigate().back();
	}

	/**
	 * 模拟浏览器前进
	 */
	public static void forward(WebDriver driver) {
		driver.navigate().forward();
	}

	/**
	 * 模拟浏览器刷新
	 */
	public static void refresh(WebDriver driver) {
		driver.navigate().refresh();
	}

	/**
	 * 模拟浏览器最大化
	 */
	public static void maximize(WebDriver driver) {
		driver.manage().window().maximize();
	}

	/**
	 * 获取页面url
	 */
	public static String getCurrentUrl(WebDriver driver) {
		return driver.getCurrentUrl();
	}

	/**
	 * 获取页面源码
	 */
	public static String getPageSource(WebDriver driver) {
		return driver.getPageSource();
	}

	/**获取页面截图
	 * @param driver
	 * @return
	 * @throws Exception
	 */
	public static String getScreenshot(WebDriver driver) throws Exception {
		String id = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomStringUtils.randomNumeric(15);
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File destFile = new File(System.getProperty("java.io.tmpdir") + "/" + id + ".jpg");
		FileUtils.copyFile(scrFile, destFile);
		return destFile.getAbsolutePath();
	}

	/**
	 * 执行JavaScript脚本
	 * 
	 * @param script
	 *            待执行JS脚本内容
	 * @return 执行结果
	 */
	public static Object executeJScript(WebDriver driver, String script, Object... args) {
		return ((JavascriptExecutor) driver).executeScript(script, args);
	}

	/**
	 * 清空
	 *
	 */
	public static void clear(WebDriver driver, By by) throws Exception {
		driver.findElement(by).clear();
	}

	/**
	 * 鼠标左边单击
	 */
	public static void click(WebDriver driver, By by) throws Exception {
		driver.findElement(by).click();
	}

	/**
	 * 发送文本
	 */
	public static void sendKeys(WebDriver driver, By by, String text) throws Exception {
		driver.findElement(by).sendKeys(text);
	}

	/**
	 * 鼠标悬停
	 */
	public static void clickAndHold(WebDriver driver, By by) {
		WebElement element = driver.findElement(by);
		new Actions(driver).clickAndHold(element).perform();
	}

	/**
	 * 鼠标右键
	 */
	public static void contextClick(WebDriver driver, By by) {
		WebElement element = driver.findElement(by);
		new Actions(driver).contextClick(element).perform();
	}

	/**
	 * 鼠标双击
	 */
	public static void doubleClick(WebDriver driver, By by) {
		WebElement element = driver.findElement(by);
		new Actions(driver).doubleClick(element).perform();
	}

	/**
	 * 启用禁用
	 */
	public static void enableCheckbox(WebDriver driver, By by, boolean b) {
		WebElement checkbox = driver.findElement(by);
		if (b) {// 启用
			if (!checkbox.isEnabled()) {
				checkbox.click();
			}

		} else {// 禁用
			if (checkbox.isEnabled()) {
				checkbox.click();
			}
		}
	}

	/**
	 * 选择或取消 checkboxes, options ,radio 
	 */
	public static void setSelect(WebDriver driver, By by, boolean b) {
		WebElement checkbox = driver.findElement(by);
		if (b) {// 选择
			if (!checkbox.isSelected()) {
				checkbox.click();
			}
		} else {// 取消
			if (checkbox.isSelected()) {
				checkbox.click();
			}
		}
	}

	/**
	 * 获取当前方法名
	 */
	public static String getMethodName() {
		return new Throwable().getStackTrace()[1].getMethodName();
	}

	/**
	 * checkbox全选或者全不选	
	 */
	public static void setSelected(WebDriver driver, By by, boolean b) throws Exception {
		List<WebElement> elements = driver.findElements(by);
		for (WebElement element : elements) {
			Select checkBox = new Select(element);
			checkBox.selectByIndex(0);
		}
	}

	
	/**选择下拉的文本
	 * @param driver
	 */
	public static void select(WebDriver driver, By by,  String option) {
		WebElement element = driver.findElement(by);
		Select select = new Select(element);
		select.selectByVisibleText(option);
	}
	
	/**
	 *获取某个元素
	
	 */
	public WebElement findElement(WebDriver driver, By by) throws Exception {
		return driver.findElement(by);
	}

	/**
	 *获取所有元素
	
	 */
	public List<WebElement> findElements(WebDriver driver, By by) throws Exception {
		return driver.findElements(by);
	}

	public static Boolean execute(WebDriver driver, JavascriptExecutor javascriptExecutor, String test_scene_desc,
			int step, String test_step_desc, String action, String findby, String location, Object value,
			String assertMethod, String assertKey, Object assertPatternattern) {
		Boolean b = null;
		WebElement webElement = null;
		String jqueryObj = null;
		if ("selector".equalsIgnoreCase(findby)) {
			jqueryObj = "$('" + location + "')";
			// webElement = driver.findElement(By.cssSelector(location)) ;
		} else if ("id".equalsIgnoreCase(findby)) {
			jqueryObj = "$('#" + location + "')";

		} else if ("name".equalsIgnoreCase(findby)) {
			jqueryObj = "$('[name=\"" + location + "\"]')";
		} else if ("tagName".equalsIgnoreCase(findby)) {
			jqueryObj = "$('" + location + "')";
		} else if ("xpath".equalsIgnoreCase(findby)) {
			webElement = driver.findElement(By.xpath(location));
		}

		if (action.equalsIgnoreCase("get") || action.equalsIgnoreCase("GotoURL")) {// 打开网页
			log.info("步骤" + step + "," + test_step_desc + "=" + "打开网页" + value);
			driver.get((String) value);
		} else if (action.equalsIgnoreCase("sleep") || action.equalsIgnoreCase("pause")) {// 暂停毫秒数
			log.info("步骤" + step + "," + test_step_desc + "=" + "暂停毫秒数" + value);
			sleep(Long.parseLong(value.toString()));
		}
		if (action.equalsIgnoreCase("sendKeys") || action.equalsIgnoreCase("type")) {// 输入
			log.info(
					"步骤" + step + "," + test_step_desc + "=" + findby.toLowerCase() + "为" + location + "的元素输入" + value);
			if (jqueryObj != null) {
				javascriptExecutor.executeScript(jqueryObj + ".val('" + value + "');");
			} else {
				webElement.sendKeys(value.toString());
			}
		} else if (action.equalsIgnoreCase("click")) {// 单击
			log.info("步骤" + step + "," + test_step_desc + "=" + findby.toLowerCase() + "为" + location + "的元素单击");
			if (jqueryObj != null) {
				javascriptExecutor.executeScript(jqueryObj + ".click();");
			} else {
				webElement.click();
			}
		} else if (action.equalsIgnoreCase("assert")) {// 验证
			String real_value = null;
			if (jqueryObj != null) {
				if ("getAttribute".equalsIgnoreCase(assertMethod)) {
					real_value = (String) javascriptExecutor
							.executeScript("return " + jqueryObj + ".attr('" + assertKey + "');");
				} else if ("getCssValue".equalsIgnoreCase(assertMethod)) {
					real_value = (String) javascriptExecutor
							.executeScript("return " + jqueryObj + ".css('" + assertKey + "');");
				} else if ("getText".equalsIgnoreCase(assertMethod)) {
					real_value = (String) javascriptExecutor.executeScript("return " + jqueryObj + ".text();");
				}else if ("getValue".equalsIgnoreCase(assertMethod)) {
					real_value = (String) javascriptExecutor.executeScript("return " + jqueryObj + ".val();");
				}
			} else {
				if ("getAttribute".equalsIgnoreCase(assertMethod)) {
					real_value = webElement.getAttribute(assertKey);
				} else if ("getCssValue".equalsIgnoreCase(assertMethod)) {
					real_value = webElement.getCssValue(assertKey);
				} else if ("getText".equalsIgnoreCase(assertMethod)) {
					real_value = webElement.getText();
				}else if ("getValue".equalsIgnoreCase(assertMethod)) {
					real_value = webElement.getAttribute("value");
				}
			}
			log.info("步骤" + step + "," + test_step_desc + "=" + findby.toLowerCase() + "为" + location + "的元素真实值为"
					+ real_value + ",期望的正则表达式为" + assertPatternattern);
			b = real_value.matches((String) assertPatternattern);
			if (b == true) {
				log.info("本步骤验证成功");
			} else {
				log.info("本步骤验证失败");
			}
		}
		return b;

	}

	// 单独按键
	public static void pressKeyboard(WebDriver driver, Keys k) {
		new Actions(driver).sendKeys(k).perform();
	}

	public static void pressKeyboard(WebDriver driver, By by, Keys k) {
		WebElement ele = driver.findElement(by);
		new Actions(driver).sendKeys(ele, k).perform();
	}

	// 组合键的使用
	// ALT + F4
	public static void alt_F4(WebDriver driver) {
		new Actions(driver).keyDown(Keys.ALT).sendKeys(Keys.F4).keyUp(Keys.ALT).perform();
	}
	
	/**
	 * Mimic system-level keyboard event
	 * @param keyCode
	 *            such as KeyEvent.VK_TAB, KeyEvent.VK_F11
	 * @throws  Exception 
	 */
	public void pressKeyboard(int keyCode) throws  Exception {
 
		Robot  
			rb = new Robot();
		 
		rb.keyPress(keyCode);	// press key
		rb.delay(100); 			// delay 100ms
		rb.keyRelease(keyCode);	// release key
	 
	}
	

	// 鼠标操作
	// 鼠标左键操作
	public static void clickByKeyBoard(WebDriver driver, By by) {
		new Actions(driver).click(driver.findElement(by)).perform();
	}

	// 鼠标右键操作
	public static void contextClickByKeyBoard(WebDriver driver, By by) {
		new Actions(driver).contextClick(driver.findElement(by)).perform();
	}

	// 鼠标双击操作
	public static void doubleClickByKeyBoard(WebDriver driver, By by) {
		new Actions(driver).doubleClick(driver.findElement(by)).perform();
	}

	// 鼠标拖拽
	public static void dragAndDrop(WebDriver driver, By by1, By by2) {
		new Actions(driver).dragAndDrop(driver.findElement(by1), driver.findElement(by2)).perform();
	}

	// 鼠标移动操作
	public static void moveToElement(WebDriver driver, By by) {
		new Actions(driver).moveToElement(driver.findElement(by)).perform();
	}

	// 高亮显示某个locator
	public static void highlight(WebDriver driver, By by) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(
				"element = arguments[0];" + "original_style = element.getAttribute('style');"
						+ "element.setAttribute('style', original_style + \";"
						+ "background: yellow; border: 4px solid red;\");"
						+ "setTimeout(function(){element.setAttribute('style', original_style);}, 5000);",
				driver.findElement(by));
	}

	// 锁定Iframe
	public static void switchToframeByID(WebDriver driver, String s) {
		driver.switchTo().frame(s);// ID
	}

	public static void switchToframeByIndex(WebDriver driver, int i) {
		driver.switchTo().frame(i);// Index
	}

	public static void switchToframe(WebDriver driver, WebElement ele) {
		driver.switchTo().frame(ele);// Element
	}

	public static void mouseOver(WebDriver driver, By by) throws Exception {

		Robot rb = null;

		rb = new Robot();

		rb.mouseMove(0, 0);

		// Then hover
		WebElement webElement = driver.findElement(by);

		if (driver instanceof RemoteWebDriver
				&& ((RemoteWebDriver) driver).getCapabilities().getBrowserName().toLowerCase().contains("chrome")) {
			Actions builder = new Actions(driver);
			builder.moveToElement(webElement).build().perform();
		} else if ((driver instanceof RemoteWebDriver
				&& ((RemoteWebDriver) driver).getCapabilities().getBrowserName().toLowerCase().contains("ie")) || (

		driver instanceof RemoteWebDriver
				&& ((RemoteWebDriver) driver).getCapabilities().getBrowserName().toLowerCase().contains("firefox"))) {
			for (int i = 0; i < 5; i++) {
				Actions builder = new Actions(driver);
				builder.moveToElement(webElement).build().perform();
			}
		} else if (driver instanceof RemoteWebDriver
				&& ((RemoteWebDriver) driver).getCapabilities().getBrowserName().toLowerCase().contains("Safari")) {

		}

	}

 

	// 锁定浏览器打开的新页面
	public static void popupPage(WebDriver driver) {
		// 获取当前页面的 句柄
		String currentWindow = driver.getWindowHandle();
		// 获取所有页面的句柄
		Set<String> handles = driver.getWindowHandles();

		Iterator<String> it = handles.iterator();
		while (it.hasNext()) {
			String nextHandle = it.next();
			if (currentWindow.equals(nextHandle))
				driver = driver.switchTo().window(nextHandle);

		}
	}

	// 获取单元格
	public static WebElement getCell(WebDriver driver, WebElement Row, int cell) {
		List<WebElement> cells;
		WebElement target = null;
		// 列里面有"<th>"、"<td>"两种标签，所以分开处理。
		if (Row.findElements(By.tagName("th")).size() > 0) {
			cells = Row.findElements(By.tagName("th"));
			target = cells.get(cell);
		}
		if (Row.findElements(By.tagName("td")).size() > 0) {
			cells = Row.findElements(By.tagName("td"));
			target = cells.get(cell);
		}
		return target;
	}

	public static String getTableText(WebDriver driver, By by, int row, int col) {
		WebElement table = driver.findElement(by);
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		WebElement theRow = rows.get(row);

		List<WebElement> cells;
		WebElement target = null;
		// 列里面有"<th>"、"<td>"两种标签，所以分开处理。
		if (theRow.findElements(By.tagName("th")).size() > 0) {
			cells = theRow.findElements(By.tagName("th"));
			target = cells.get(col);
		}
		if (theRow.findElements(By.tagName("td")).size() > 0) {
			cells = theRow.findElements(By.tagName("td"));
			target = cells.get(col);
		}

		return target.getText();
	}

	/**
	 * 模拟鼠标移动
	 * 
	 * @param elementdI
	 *            待移动至的控件定位符
	 */
	public static void elementMove(WebDriver driver, String elementdI) {
		WebElement sourceWebElement = driver.findElement(By.id(elementdI));
		new Actions(driver).moveToElement(sourceWebElement).perform();
	}

	/**
	 * 切换Frame句柄到新框架
	 * 
	 * @param locator
	 *            Frame元素
	 * @throws Exception
	 */
	public static void switchFrame(WebDriver driver, String elementId) throws Exception {
		WebElement element = driver.findElement(By.id(elementId));
		driver.switchTo().frame(element);
	}

	/**
	 * 切换Frame句柄到主框架 从Iframe回到主窗体
	 */
	public static void switchFrame(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	/**
	 * 检查页面引用的js文件能否正常请求
	 * 
	 *
	 * 
	 */
	public boolean checkJsURLConnect(WebDriver driver) {
		String jsurl;
		String msg = "校验有错误： \r\n";
		CloseableHttpResponse response = null;
		List<WebElement> elements = driver.findElements(By.xpath("//script[@src]"));
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < elements.size(); i++) {
			list.add(elements.get(i).getAttribute("src"));
		}
		boolean flag = true;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).toString().contains("tuniucdn.com")) {
				jsurl = list.get(i).toString().trim();
				CloseableHttpClient httpclient = HttpClients.createDefault();
				/** 发送get请求 **/
				HttpGet httpGet = new HttpGet(jsurl);
				try {
					response = httpclient.execute(httpGet);
				} catch (IOException e) {
					System.out.println("连接 " + jsurl + " 请求异常");
				}
				/** 请求发送成功，并得到响应 **/
				if (response.getStatusLine().getStatusCode() != 200) {
					flag = false;
					msg = msg + "第" + (i + 1) + " JS URL=" + jsurl + " 请求失败，返回码是 "
							+ response.getStatusLine().getStatusCode() + " ; \r\n";
				}
			}
		}
		return flag;

	}

	/**
	 * 检查页面引用的css文件能否正常请求
	 * 
	 * 
	 * 
	 */
	public boolean checkCssURLConnect(WebDriver driver) {
		String cssurl;
		String msg = "校验有错误： \r\n";
		CloseableHttpResponse response = null;
		List<WebElement> elements = driver.findElements(By.xpath("//link[@href]"));
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < elements.size(); i++) {
			list.add(elements.get(i).getAttribute("href"));
		}
		boolean flag = true;
		for (int i = 0; i < list.size(); i++) {

			cssurl = list.get(i).toString().trim();
			CloseableHttpClient httpclient = HttpClients.createDefault();
			/** 发送get请求 **/
			HttpGet httpGet = new HttpGet(cssurl);
			try {
				response = httpclient.execute(httpGet);
			} catch (IOException e) {
				System.out.println("连接 " + cssurl + " 请求异常");
			}
			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() != 200) {
				flag = false;
				msg = msg + "第" + (i + 1) + " CSS URL=" + cssurl + " 请求失败，返回码是 "
						+ response.getStatusLine().getStatusCode() + " ; \r\n";
			}

		}
		return flag;
	}

	/**
	 * 检查页面引用的image文件能否正常请求
	 * 
	 * 
	 */
	public static boolean checkPicConnect(WebDriver driver) {
		String msg = "校验有错误： \r\n";
		boolean flag = true;
		List<WebElement> elements = driver.findElements(By.xpath("//img"));// 获取所有img节点
		StringBuffer sbURL = new StringBuffer();
		String src;
		int Count200 = 0;
		int Counterror = 0;
		int CountSrcnull = 0;

		if (elements.size() == 0) {
			System.out.println("当前页面不存在img节点的图片，请确认！");
		} else {
			System.out.println("当前页面img节点的图片个数=" + elements.size());
			for (int i = 0; i < elements.size(); i++) {
				try {
					src = elements.get(i).getAttribute("SRC").trim();
					// System.out.println("第" + (i + 1) + "个图片的URL=" + src);
					if (src == null) {
						CountSrcnull = CountSrcnull + 1;
					}
				} catch (Exception e) {
					System.out.println("第" + (i + 1) + "个图片img节点不存在SRC属性，请检查!");
					continue;
				}

				CloseableHttpClient httpclient = HttpClients.createDefault();
				/** 发送get请求 **/
				HttpGet httpGet = new HttpGet(src);
				CloseableHttpResponse response = null;
				try {
					response = httpclient.execute(httpGet);
				} catch (IOException e) {
					System.out.println("连接 " + src + " 请求异常");
				}
				/** 请求发送成功，并得到响应 **/
				int code = response.getStatusLine().getStatusCode();
				if (code == 200) {
					Count200 = Count200 + 1;
				} else {
					flag = false;
					Counterror = Counterror + 1;
					msg = msg + "第" + (i + 1) + " imgae URL=" + src + " 请求失败，返回码是 "
							+ response.getStatusLine().getStatusCode() + " ; \r\n";
				}

			}
			System.out.println("图片的URL的SRC是空的个数是" + CountSrcnull);
			System.out.println("图片的URL响应消息码是200的个数是" + Count200);
			System.out.println("图片的URL响应消息码是非200的个数是" + Counterror);

		}

		return flag;
	}

	/**
	 * 滚动条纵向移动,指定移动的距离 功能描述：滚动条纵向移动 <br>
	 * 
	 * @param 滚动条滚动距离[height]。-1移动到底部
	 * @return 返回值为空
	 */
	public static void scrollVerticalBar(WebDriver driver, Integer height) {
		if (height == null || height == -1) {
			// 移动滚动条至页尾
			executeJScript(driver, "window.scrollTo(0,document.body.scrollHeight);");
		} else {
			executeJScript(driver, "window.scrollTo(0," + height + ");");
		}
	}

	/**
	 * Keyword名称：获取cookie的值 功能描述：获取cookie的value属性值 <br>
	 *
	 * @param poParam
	 *            哈希Map类型，其中包括cookie名称[cookieName],cookie的value属性值存放变量[
	 *            cookieValue]
	 * @return 返回值为空
	 */
	public String getCookieValueByName(WebDriver driver, String cookieName) {
		Cookie cookie = driver.manage().getCookieNamed(cookieName);
		return cookie.getValue();
	}

	/**
	 * Keyword名称：获取cookie的值 功能描述：获取cookie的value属性值 <br>
	 *
	 * @param cookieName
	 *            cookie的key
	 * @return 返回值为空
	 */
	public static void deleteCookieValueByName(WebDriver driver, String cookieName) {
		driver.manage().deleteCookieNamed(cookieName);
	}

	/**
	 * Keyword名称：删除所有cookies 功能描述：删除所有的cookies
	 * 
	 * @param
	 * @return 返回值为空
	 */
	public static void deleteAllCookies(WebDriver driver) {
		driver.manage().deleteAllCookies();
	}

	/**
	 * 增加cookie<br/>
	 * 功能描述：增加cookie，根据其名称和属性值
	 * 
	 * @param key
	 *            cookie的键[key]
	 * @param value
	 *            cookie的value值[value]
	 * @return 返回值为空
	 */
	public static void addCookie(WebDriver driver, String key, String value) {
		Cookie cookie = new Cookie(key, value);
		driver.manage().addCookie(cookie);
	}

	/**
	 * 增加多个cookie,批量增加cookie<br/>
	 * 功能描述：增加cookie，根据其名称和属性值
	 * 
	 * @param cookies
	 *            字符串 key=value格式，对组分号英文隔开（;）
	 * @return 返回值为空
	 */
	public static void addAllCookie(WebDriver driver, String cookies) {
		String sKey, sValue;
		String[] cookieStrArr = cookies.split(";");
		for (int i = 0; i < cookieStrArr.length; i++) {
			String[] KeyandValue = cookieStrArr[i].split("=");
			sKey = KeyandValue[0];
			sValue = KeyandValue[1];
			addCookie(driver, sKey, sValue);
		}
	}

	/**线程等待时间毫秒数
	 * @param millis
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	/**全局等待时间秒数,隐式等待
	 */
	public static void implicitlyWait(WebDriver driver, int seconds) {
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}

	/**全局等待时间秒数,显示等待 
	 */
	public static void until(WebDriver driver, int seconds, By by) {
		new WebDriverWait(driver, seconds).until(ExpectedConditions.presenceOfElementLocated(by));
	}

	/**上传文件 
	 */
	public static void upload(WebDriver driver, By by, String path) {
		driver.findElement(by).sendKeys(path);
	}

	/**获取文本
	 */
	public static void getText(WebDriver driver, By by) {
		driver.findElement(by).getText();
	}

	/**
	 * 
	* verifyComboSelectOption
	* (校验下拉选项是否存在某个值)
	* @param locator 控件定位器
	* @param text 期望值
	* @param matched boolean值，期望匹配还是不匹配
	* @return void 返回类型
	* 
	 */
	public static void verifyComboBoxSelectOption(WebDriver driver, String elementId, String text, boolean matched) {
		WebElement comboSelect = driver.findElement(By.id(elementId));
		List<WebElement> options = ((Select) comboSelect).getOptions();
		boolean bIsMatched = false;
		for (WebElement e : options) {
			if (e.getText().equalsIgnoreCase(text)) {
				bIsMatched = true;
			}
		}
		if (bIsMatched != matched) {
			throw new RuntimeException("校验下拉列表框选项失败, 期望值是[" + text + "], 期望匹配结果是[" + matched + "].");
		}
	}

	/**
	 * 
	* verifyComboSelectOption
	* (校验下拉选项是否存在某些值)
	* @param locator 控件定位器
	* @param texts 期望值字符串数组
	* @param matched boolean值，期望匹配还是不匹配
	* @return void 返回类型
	* 
	 */
	public static void verifyComboBoxSelectOption(WebDriver driver, String elementId, String[] texts, boolean matched) {
		WebElement comboSelect = driver.findElement(By.id(elementId));
		List<WebElement> options = ((Select) comboSelect).getOptions();
		boolean bIsMatched = false;
		for (int i = 0; i < texts.length; i++) {
			if (options.get(i).getText().equalsIgnoreCase(texts[i])) {
				bIsMatched = true;
				System.out.println("校验成功" + texts[i]);
			}
		}
		if (bIsMatched != matched) {
			throw new RuntimeException("校验下拉列表框选项失败, 期望值是[" + texts + "], 期望匹配结果是[" + matched + "].");
		}
	}

	public static void main(String[] args) {
		String fileName = RandomGeneratorUtil.generate6Num() + ".xls";
		File xlsFile = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
		log.info(xlsFile.getAbsolutePath());
	}

}

class MyWebDriverEventListener implements WebDriverEventListener {

	private static Log log = LogFactory.getLog(MyWebDriverEventListener.class);

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {

	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		log.info("目标跳转地址=" + url);
		log.info("当前激活页地址=" + driver.getCurrentUrl());
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		log.info("页面后退");

	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		log.info("页面前进");

	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateRefresh(WebDriver driver) {
		log.info("刷新页面");

	}

	@Override
	public void afterNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		log.info("查找元素=" + by.toString());
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		log.info("单击元素=" + element);
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		log.info("执行脚本=" + script);

	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		try {
			String path = WebDriverUtil.getScreenshot(driver);
			log.info("截图位置=" + path);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@Override
	public void beforeAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		log.info("修改元素值=" + keysToSend);

	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeSwitchToWindow(String windowName, WebDriver driver) {
		log.info("切换窗口=" + windowName);

	}

	@Override
	public void afterSwitchToWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public <X> void afterGetScreenshotAs(OutputType<X> arg0, X arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <X> void beforeGetScreenshotAs(OutputType<X> arg0) {
		// TODO Auto-generated method stub
		
	}

}