package webshop.test;

import static com.codeborne.selenide.Selenide.open;
import static webshop.config.Config.WEB_SHOP_URL;

import org.junit.jupiter.api.Test;

import net.datafaker.Faker;
import webshop.pages.WSWelcomePage;

public class RegistrationTest {
    private static final Faker faker = new Faker();

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
