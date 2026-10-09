package webshop.pages;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Step;

public class WSCartPage {
    private final SelenideElement productName = $("a.product-name");
    private final SelenideElement productQuantity = $("input.qty-input");
    private final SelenideElement productSubtotal = $("span.product-subtotal");

    @Step("Проверка названия первого продукта в корзине {itemName}")
    public WSCartPage checkProductName(String itemName) {
        productName.shouldHave(text(itemName));
        return this;
    }

    @Step("Проверка количества первого продукта в корзине {itemQuantity}")
    public WSCartPage checkProductQuantity(String itemQuantity) {
        productQuantity.shouldHave(value(itemQuantity));
        return this;
    }

    @Step("Проверка стоимости первого продукта в корзине {itemSubtotal}")
    public WSCartPage checkProductSubtotal(String itemPrice, String additionalPrice, String itemQuantity) {
        productSubtotal.shouldHave(text(String.valueOf(
                (Float.parseFloat(itemPrice) + Float.parseFloat(additionalPrice)) * Float.parseFloat(itemQuantity))));
        return this;
    }
}