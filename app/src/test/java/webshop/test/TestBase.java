package webshop.test;

import static webshop.config.Config.getSelenoidChromeOptions;
import static webshop.config.Config.getWebDriverConfig;

import java.net.URI;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;

import io.qameta.allure.selenide.AllureSelenide;
import webshop.config.WebDriverConfig;
import webshop.util.AttachManager;

public class TestBase {

    private static final WebDriverConfig config = getWebDriverConfig();

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        Configuration.browserSize = config.browserSize();
        Configuration.browser = config.browser();
    }

    @BeforeEach
    void createRemoteDriver() {
        if (!"remote".equals(System.getProperty("run"))) {
            return;
        }

        ClientConfig clientConfig = ClientConfig.defaultConfig()
                .baseUri(URI.create("https://" + config.selenoidUrl()))
                .authenticateAs(new UsernameAndPassword(System.getProperty("selenoidUser"), System.getProperty("selenoidPassword")))
                .readTimeout(Duration.ofMinutes(2));

        WebDriverRunner.setWebDriver(RemoteWebDriver.builder()
                .oneOf(getSelenoidChromeOptions())
                .config(clientConfig)
                .build());
    }

    @BeforeEach
    void closeDriver() {
        Selenide.closeWebDriver();
    }

    @AfterEach
    void after() {
        AttachManager.takeScreenshot();
        AttachManager.pageSource();
        AttachManager.browserConsoleLogs();

        if ("remote".equals(config.run())) {
            AttachManager.addVideo();
        }
    }
}
