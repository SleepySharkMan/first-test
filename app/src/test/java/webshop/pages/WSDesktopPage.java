package webshop.pages;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import io.qameta.allure.Step;

public class WSDesktopPage {
    private final SelenideElement itemNameHolder = $("[itemprop=name]");
    private final SelenideElement itemPriceHolder = $("[itemprop=price]");
    private final ElementsCollection processorRadioList = $$(".option-list").get(0).$$("li label");
    private final SelenideElement quantityInput = $("input.qty-input");
    private final SelenideElement addToCartButton = $("input.add-to-cart-button");
    private final SelenideElement notificationBar = $("div.bar-notification.success");
    private final SelenideElement cartQuantity = $("span.cart-qty");
    private final SelenideElement openCartButton = $("a.ico-cart");

    @Step("Выбор процессора по индексу {indexOfProcessor}")
    public WSDesktopPage selectProcessor(int indexOfProcessor) {
        // index 0 = slow, 1 = medium, 2 = fast
        processorRadioList.get(indexOfProcessor).click();
        return this;
    }

    @Step("Установка количества продукта {itemQuantity}")
    public WSDesktopPage setQuantity(String itemQuantity) {
        quantityInput.setValue(itemQuantity);
        return this;
    }

    @Step("Добавление продукта в корзину")
    public WSDesktopPage addToCart() {
        addToCartButton.click();
        return this;
    }

    @Step("Проверка отображения уведомления")
    public WSDesktopPage checkNotificationIsShown() {
        notificationBar.shouldBe(visible);
        return this;
    }

    @Step("Проверка количества продукта в корзине {itemQuantity}")
    public WSDesktopPage checkCartQuantity(String itemQuantity) {
        cartQuantity.shouldHave(text("(" + itemQuantity + ")"));
        return this;
    }

    @Step("Открытие корзины")
    public WSCartPage openCart() {
        openCartButton.click();
        return new WSCartPage();
    }

    @Step("Получение названия продукта")
    public String getItemName() {
        return itemNameHolder.getText();
    }

    @Step("Получение цены продукта")
    public String getItemPrice() {
        return itemPriceHolder.getText();
    }

    @Step("Получение цены процессора по индексу {index}")
    public String getProcessorPrice(int index) {
        String digits = $$(".option-list").get(0).$$("li label")
                .get(index).getText()
                .replaceAll("[^0-9.]", ""); // только цифры и точка

        return digits.isEmpty() ? "0.0" : digits;
    }
}