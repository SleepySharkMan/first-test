package pages;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class ChooseFormPage {

    public ChooseFormPage openSection(String sectionName){
        $(byText(sectionName)).click();
        return  this;
    }

    public PracticeFormPage openRequiredForm(String formName) {
        $(byText(formName)).click();
        return new PracticeFormPage();
    }
}
