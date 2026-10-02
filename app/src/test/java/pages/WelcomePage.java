package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class WelcomePage {

    private final SelenideElement priceMenuItem = $$(".t-menu__list li").last();

    private final SelenideElement openWindowButton = $x("/html/body/div[1]/div[42]/div/div/div[32]/div/a/div/span");

    private final SelenideElement openPricePageButton = $(byText("Бегу оплачивать"));

    public WelcomePage clickPriceMenuItem() {
        priceMenuItem.shouldBe(clickable).click();
        return this;
    }

    public WelcomePage openWindow() {
        openWindowButton.click();
        return this;
    }

    public PricePage openPricePage() {
        openPricePageButton.click();
        Selenide.switchTo().window(2);
        return new PricePage();
    }

}
