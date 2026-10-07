package webshop.pages;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class WSLoginPage {
    private final SelenideElement pageTitle = $("div.page-title h1");
    private final SelenideElement emailInput = $("input#Email");
    private final SelenideElement passwordInput = $("input#Password");
    private final SelenideElement rememberMeCheckbox = $("input#RememberMe");
    private final SelenideElement loginButton = $("input.login-button");
    private final ElementsCollection headerLinks = $$("div.header-links ul li a");

    public WSLoginPage verifyLoginPage() {
        pageTitle.shouldHave(text("Welcome, Please Sign In!"));
        return this;
    }

    public WSLoginPage enterEmail(String email) {
        emailInput.setValue(email);
        return this;
    }

    public WSLoginPage enterPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public WSLoginPage selectRememberMe() {
        rememberMeCheckbox.click();
        return this;
    }

    public WSLoginPage submitLogin() {
        loginButton.click();
        return this;
    }

    public WSLoginPage checkEmailIsShown(String email) {
        headerLinks.get(0).shouldHave(text(email));
        return this;
    }
}
