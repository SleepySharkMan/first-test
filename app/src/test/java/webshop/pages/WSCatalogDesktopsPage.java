package webshop.pages;

import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;

public class WSCatalogDesktopsPage {
    private final ElementsCollection productList = $$("div.product-grid div");

    public WSDesktopPage openDesktopPage(int prductIdOnPage) {
        productList.get(1).click();
        return new WSDesktopPage();
    }
}
