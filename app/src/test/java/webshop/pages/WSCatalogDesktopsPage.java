package webshop.pages;

import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;

import io.qameta.allure.Step;

public class WSCatalogDesktopsPage {
    private final ElementsCollection productList = $$("div.product-grid div");

    @Step("Открытие страницы десктопа {productIdOnPage}")
    public WSDesktopPage openDesktopPage(int productIdOnPage) {
        productList.get(productIdOnPage).click();
        return new WSDesktopPage();
    }
}
