package webshop.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;

import io.qameta.allure.selenide.AllureSelenide;

public class TestBase {

    @BeforeAll
    static void setup() {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void closeDriver() {
        Selenide.closeWebDriver();
    }

    static void setUpAll() {
    SelenideLogger.addListener("allureSelenide", new AllureSelenide());
}
}
