package webshop.test;

import static com.codeborne.selenide.Selenide.open;
import static webshop.config.Config.WEB_SHOP_URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import webshop.pages.WSDesktopPage;
import webshop.pages.WSWelcomePage;
import webshop.steps.AuthSteps;

public class CartTest extends TestBase {
    private final AuthSteps authSteps = new AuthSteps();

    @BeforeEach
    void beforeAll() {
        authSteps.registerNewUser();
    }

    @Test
    void addItemToCartTest() {
        int indexOfProcessor = 0; // index 0 = slow, 1 = medium, 2 = fast

        WSDesktopPage desktop = open(WEB_SHOP_URL, WSWelcomePage.class)
                .hoverCursorOnMenuItemById(1)
                .clickOnDesktopsInDropDown()
                .openDesktopPage(1);

        String itemName = desktop.getItemName();
        String itemPrice = desktop.getItemPrice();
        String quantity = "2";
        String additionalPrice = desktop.getProcessorPrice(indexOfProcessor);

        desktop.selectProcessor(indexOfProcessor)
                .setQuantity(quantity)
                .addToCart()
                .checkNotificationIsShown()
                .checkCartQuantity(quantity)
                .openCart()
                .checkProductName(itemName)
                .checkProductQuantity(quantity)
                .checkProductSubtotal(itemPrice, additionalPrice, quantity);
    }
}
