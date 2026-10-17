package webshop.steps;

import static com.codeborne.selenide.Selenide.open;
import static webshop.config.Config.WEB_SHOP_REGISTRATION_URL;

import net.datafaker.Faker;
import webshop.pages.WSRegistrationPage;

public class AuthSteps {
    private static final Faker faker = new Faker();

    public void registerNewUser() {
        String email = faker.internet().emailAddress();
        String password = faker.harryPotter().character() + faker.number().positive();
        open(WEB_SHOP_REGISTRATION_URL, WSRegistrationPage.class)
                .register(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        email,
                        password)
                .checkEmailIsShown(email);
    }
}
