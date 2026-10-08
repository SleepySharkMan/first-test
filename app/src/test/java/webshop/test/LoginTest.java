package webshop.test;

import static com.codeborne.selenide.Selenide.clearBrowserCookies;
import static com.codeborne.selenide.Selenide.clearBrowserLocalStorage;
import static com.codeborne.selenide.Selenide.open;
import static webshop.config.Config.WEB_SHOP_REGISTRATION_URL;
import static webshop.config.Config.WEB_SHOP_URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.datafaker.Faker;
import webshop.pages.WSRegistrationPage;
import webshop.pages.WSWelcomePage;

public class LoginTest extends TestBase {
    private static final Faker faker = new Faker();
    private String email;
    private String password;

    @BeforeEach
    void beforeAll() {
        email = faker.internet().emailAddress();
        password = faker.harryPotter().character() + faker.number().positive();
        open(WEB_SHOP_REGISTRATION_URL, WSRegistrationPage.class)
                .register(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        email,
                        password)
                .checkEmailIsShown(email);
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

    @Test
    void successLoginTest() {
        open(WEB_SHOP_URL, WSWelcomePage.class)
                .openLoginPage()
                .verifyLoginPage()
                .enterEmail(email)
                .enterPassword(password)
                .selectRememberMe()
                .submitLogin()
                .checkEmailIsShown(email);
    }
}
