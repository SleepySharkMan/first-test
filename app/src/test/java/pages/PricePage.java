package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class PricePage {

    SelenideElement priceHolder = $$("aside h3").shouldHave(size(2)).first();

    public PricePage checkPrice(String price) {
        priceHolder.shouldHave(text("\r\n" + //
                "₽ 47 000.00"));
        return this;
    }
}
