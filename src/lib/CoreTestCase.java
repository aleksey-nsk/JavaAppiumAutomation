package lib;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import junit.framework.TestCase;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.net.URL;

public class CoreTestCase extends TestCase {

  protected AppiumDriver driver;
  private static String AppiumURL = "http://127.0.0.1:4723/wd/hub";

  @Override
  public void setUp() throws Exception {
    System.out.print("\n\n***** Внутри метода setUp() *****\n\n");

    super.setUp();

    final String path = new File("apks/org.wikipedia.apk").getAbsolutePath();
    System.out.println("Путь до тестируемого приложения: " + path);

    DesiredCapabilities capabilities = new DesiredCapabilities();

    capabilities.setCapability("platformName", "Android");
    capabilities.setCapability("deviceName", "AndroidTestDevice");
    capabilities.setCapability("platformVersion", "6.0");
    capabilities.setCapability("automationName", "Appium");
    capabilities.setCapability("appPackage", "org.wikipedia");
    capabilities.setCapability("appActivity", ".main.MainActivity");
    capabilities.setCapability("app", path);

    driver = new AndroidDriver(new URL(AppiumURL), capabilities);
  }

  @Override
  public void tearDown() throws Exception {
    System.out.print("\n\n***** Внутри метода tearDown() *****\n\n");

    System.out.println("Поворачиваем экран телефона в портретный режим");
    driver.rotate(ScreenOrientation.PORTRAIT);

    System.out.println("Вызываем метод quit()");
    driver.quit();

    super.tearDown();
  }
}