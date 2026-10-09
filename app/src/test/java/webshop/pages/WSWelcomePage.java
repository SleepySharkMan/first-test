package webshop.pages;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Step;

public class WSWelcomePage {
    private final SelenideElement registrationButton = $(".ico-register");
    private final SelenideElement loginButton = $(".ico-login");
    private final ElementsCollection headerMenuList = $$("ul.top-menu li a");
    private final SelenideElement dropDownDesktopButton = $(byText("Desktops"));

    @Step("Открытие страницы регистрации")
    public WSRegistrationPage openRegistrationPage() {
        registrationButton.click();
        return new WSRegistrationPage();
    }

    @Step("Открытие страницы входа")
    public WSLoginPage openLoginPage() {
        loginButton.click();
        return new WSLoginPage();
    }

    @Step("Наведение курсора на пункт меню по ID {menuItemId}")
    public WSWelcomePage hoverCursorOnMenuItemById(int menuItemId) {
        headerMenuList.get(menuItemId).hover();
        return this;
    }

    @Step("Клик по пункту 'Desktops' в выпадающем меню")
    public WSCatalogDesktopsPage clickOnDesktopsInDropDown() {
        dropDownDesktopButton.click();
        return new WSCatalogDesktopsPage();
    }
}
