package mentor.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class YandexSearchPage {

    private final SelenideElement searchInput = $("#text");

    private final SelenideElement submitButton = $("button[type='submit']");

    public YandexSearchPage openYandexSearchPage() {
        open("https://ya.ru");
        return this;
    }

    public YandexSearchPage fill(String query) {
        searchInput.setValue(query);
        return this;
    }

    public YandexSerchResults submit() {
        if (submitButton.exists()) {
            submitButton.click();
        } else {
            searchInput.pressEnter();
        }
        return new YandexSerchResults();
    }

}
