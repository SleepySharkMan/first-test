package mentor.pages;

import java.time.Duration;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class YandexSerchResults {

    private final SelenideElement distributionButtonClose = $(".DistributionActions button");

    public YandexSerchResults closeDistributionWin() {
        if (distributionButtonClose.is(visible, Duration.ofSeconds(3))) {
            distributionButtonClose.click();
        }
        return this;
    }

    public WelcomePage openLink(String websiteLink) {
        $(byText(websiteLink)).click();
        Selenide.switchTo().window(1);
        return new WelcomePage();
    }

}