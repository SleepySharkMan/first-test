package webshop.test;

import static com.codeborne.selenide.Selenide.open;
import static webshop.config.Config.WEB_SHOP_URL;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import webshop.pages.WSDesktopPage;
import webshop.pages.WSWelcomePage;
import webshop.steps.AuthSteps;

@Tag ("UI")
public class CartTest extends TestBase {
    private final AuthSteps authSteps = new AuthSteps();
    private static final String PRODUCT_NAME = "Build your own cheap computer";
    private static final String ITEM_PRICE = "800";
    private static final String ITEM_QUANTITY = "2";
    private static final int INDEX_OF_PROCESSOR = 0; // index 0 = slow, 1 = medium, 2 = fast

    @BeforeEach
    void beforeAll() {
        authSteps.registerNewUser();
    }

    @Tag("positive")
    @DisplayName("Успешное добавление компьюьтера в корзину")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Andrey")
    @Link(name = "#71434683", url = "https://...")
    @Test
    void addItemToCartTest() {

        WSDesktopPage desktop = open(WEB_SHOP_URL, WSWelcomePage.class)
                .hoverMenuItem("Computers")
                .clickOnDesktopsInDropDown()
                .openProduct(PRODUCT_NAME);

        String additionalPrice = desktop.getProcessorPrice(INDEX_OF_PROCESSOR);

        desktop.selectProcessor(INDEX_OF_PROCESSOR)
                .setQuantity(ITEM_QUANTITY)
                .addToCart()
                .checkNotificationIsShown()
                .checkCartQuantity(ITEM_QUANTITY)
                .openCart()
                .checkProductName(PRODUCT_NAME)
                .checkProductQuantity(ITEM_QUANTITY)
                .checkProductSubtotal(
                        calculateSubtotal(ITEM_PRICE, additionalPrice, ITEM_QUANTITY));
    }

    private String calculateSubtotal(String price, String additionalPrice, String quantity) {
        return new BigDecimal(price)
                .add(new BigDecimal(additionalPrice))
                .multiply(new BigDecimal(quantity))
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString();
    }
}
