package webshop.pages;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class WSCartPage {
    private final SelenideElement productName = $("a.product-name");
    private final SelenideElement productQuantity = $("input.qty-input");
    private final SelenideElement productSubtotal = $("span.product-subtotal");

    public WSCartPage checkProductName(String itemName) {
        productName.shouldHave(text(itemName));
        return this;
    }

    public WSCartPage checkProductQuantity(String itemQuantity) {
        productQuantity.shouldHave(value(itemQuantity));
        return this;
    }

    public WSCartPage checkProductSubtotal(String itemPrice, String additionalPrice, String itemQuantity) {
        productSubtotal.shouldHave(text(String.valueOf(
                (Float.parseFloat(itemPrice) + Float.parseFloat(additionalPrice)) * Float.parseFloat(itemQuantity))));
        return this;
    }
}