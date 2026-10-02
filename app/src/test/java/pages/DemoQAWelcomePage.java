package pages;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DemoQAWelcomePage {

    private final String demoqaURL = "https://demoqa.com/";

    public DemoQAWelcomePage openDemoQA() {
        open(demoqaURL);
        return this;
    }

    public ChooseFormPage openСhapterPage(String chapterName) {
        $(byText(chapterName)).scrollTo().click();
        return new ChooseFormPage();
    }
}
