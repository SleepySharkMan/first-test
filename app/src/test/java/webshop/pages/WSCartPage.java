package webshop.pages;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactValue;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Step;

public class WSCartPage {
    private final SelenideElement productName = $("a.product-name");
    private final SelenideElement cartQuantity = $("input.qty-input");
    private final SelenideElement productSubtotal = $("span.product-subtotal");

    @Step("Проверка названия первого продукта в корзине {itemName}")
    public WSCartPage checkProductName(String itemName) {
        productName.shouldHave(exactText(itemName));
        return this;
    }

    @Step("Проверка количества первого продукта в корзине {itemQuantity}")
    public WSCartPage checkProductQuantity(String itemQuantity) {
        cartQuantity.shouldHave(exactValue(itemQuantity));
        return this;
    }

    @Step("Проверка стоимости первого продукта в корзине {itemSubtotal}")
    public WSCartPage checkProductSubtotal(String itemSubtotal) {
        productSubtotal.shouldHave(exactText(itemSubtotal));
        return this;
    }
}