package webshop.pages;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class WSWelcomePage {
    private final SelenideElement registrationButton = $(".ico-register");
    private final SelenideElement loginButton = $(".ico-login");

    public WSRegistrationPage openRegistrationPage() {
        registrationButton.click();
        return new WSRegistrationPage();
    }

    public WSLoginPage openLoginPage() {
        loginButton.click();
        return new WSLoginPage();
    }
}
