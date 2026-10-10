package webshop.test;

import static com.codeborne.selenide.Selenide.open;
import static webshop.config.Config.WEB_SHOP_URL;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import net.datafaker.Faker;
import webshop.pages.WSWelcomePage;

@Epic("Авторизация")
@Feature("Регистрация")
public class RegistrationTest extends TestBase {
    private static final Faker faker = new Faker();

    @DisplayName("Успешная регистрация нового пользователя")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Andrey")
    @Link(name = "#71434683", url = "https://...")
    @Test
    void registrationTest() {
        String password = faker.harryPotter().character() + faker.number().positive();
        String email = faker.internet().emailAddress();
        open(WEB_SHOP_URL, WSWelcomePage.class)
                .openRegistrationPage()
                .verifyRegistrationPage()
                .selectMaleGender()
                .enterFirstName(faker.name().firstName())
                .enterLastName(faker.name().lastName())
                .enterEmail(email)
                .enterPassword(password)
                .confirmPassword(password)
                .submitRegistration()
                .checkRegistrationCompleted()
                .checkEmailIsShown(email);
    }
}
