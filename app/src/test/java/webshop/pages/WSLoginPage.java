package webshop.pages;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Step;

public class WSLoginPage {
    private final SelenideElement pageTitle = $("div.page-title h1");
    private final SelenideElement emailInput = $("input#Email");
    private final SelenideElement passwordInput = $("input#Password");
    private final SelenideElement rememberMeCheckbox = $("input#RememberMe");
    private final SelenideElement loginButton = $("input.login-button");
    private final ElementsCollection headerLinks = $$("div.header-links ul li a");
    private final SelenideElement errorMessage = $("span.field-validation-error");

    @Step("Проверка страницы входа")
    public WSLoginPage verifyLoginPage() {
        pageTitle.shouldHave(text("Welcome, Please Sign In!"));
        return this;
    }

    @Step("Ввод email")
    public WSLoginPage enterEmail(String email) {
        emailInput.setValue(email);
        return this;
    }

    @Step("Ввод пароля")
    public WSLoginPage enterPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Выбор 'Запомнить меня'")
    public WSLoginPage selectRememberMe() {
        rememberMeCheckbox.click();
        return this;
    }

    @Step("Отправка формы входа")
    public WSLoginPage submitLogin() {
        loginButton.click();
        return this;
    }

    @Step("Проверка отображения email")
    public WSLoginPage checkEmailIsShown(String email) {
        headerLinks.get(0).shouldHave(text(email));
        return this;
    }

    @Step("Проверка отображения ошибки")
    public WSLoginPage verifyErrorMessageIsShown() {
        errorMessage.shouldBe(visible);
        return this;
    }
}
