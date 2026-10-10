package webshop.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;

import io.qameta.allure.selenide.AllureSelenide;
import webshop.util.AttachManager;

public class TestBase {

    @BeforeAll
    static void setup() {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void closeDriver() {
        Selenide.closeWebDriver();
    }

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("allureSelenide", new AllureSelenide());
    }

    @AfterEach 
    void after() {
        AttachManager.takeScreenshot();
        AttachManager.pageSource();
        AttachManager.browserConsoleLogs();
    }

}
