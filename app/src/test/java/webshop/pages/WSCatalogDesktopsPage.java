package webshop.pages;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;

import io.qameta.allure.Step;

public class WSCatalogDesktopsPage {
    private final ElementsCollection productLinks = $$("h2.product-title a");

    @Step("Открытие страницы десктопа {name}")
    public WSDesktopPage openProduct(String name) {
        productLinks.findBy(exactText(name)).click();
        return new WSDesktopPage();
    }

}
