package webshop.pages;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class WSWelcomePage {
    private final SelenideElement registrationButton = $(".ico-register");
    private final SelenideElement loginButton = $(".ico-login");
    private final ElementsCollection headerMenuList = $$("ul.top-menu li a");
    private final SelenideElement dropDownDesktopButton = $(byText("Desktops"));

    public WSRegistrationPage openRegistrationPage() {
        registrationButton.click();
        return new WSRegistrationPage();
    }

    public WSLoginPage openLoginPage() {
        loginButton.click();
        return new WSLoginPage();
    }

    public WSWelcomePage hoverCursorOnMenuItemById(int menuItemId){
        headerMenuList.get(menuItemId).hover();
        return this;
    }

    public WSCatalogDesktopsPage clickOnDesktopsInDropDown(){
        dropDownDesktopButton.click();
        return new WSCatalogDesktopsPage();
    }
}
        