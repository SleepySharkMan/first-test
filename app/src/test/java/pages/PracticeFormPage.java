package pages;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.anyOf;
import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Condition.text;

public class PracticeFormPage {

    private final SelenideElement firstNameInputName = $("#firstName");
    private final SelenideElement lastNameInputName = $("#lastName");
    private final SelenideElement genderRadio = $$("input[type=\"radio\"]").get(new Random().nextInt(3));
    private final SelenideElement numberInput = $("#userNumber");
    private final SelenideElement subjectInput = $("#subjectsInput");
    private final SelenideElement subjectDropDownEl = $$(".subjects-auto-complete__option").first();
    private final SelenideElement submitButton = $("#submit");
    private final ElementsCollection resultTable = $$("tr");
    private final ElementsCollection requiredFields = $$("[required]");
    private final ElementsCollection genderRadioButtons = $$("[required]");

    public PracticeFormPage fillFirstName(String firstName) {
        firstNameInputName.setValue(firstName);
        return this;
    }

    public PracticeFormPage fillLastName(String lastName) {
        lastNameInputName.setValue(lastName);
        return this;
    }

    public PracticeFormPage fillGenderRadio() {
        genderRadio.click();
        return this;
    }

    public PracticeFormPage fillNumber(String number) {
        numberInput.setValue(number);
        return this;
    }

    public PracticeFormPage checkRequiredFields() {
        ArrayList<String> problems = new ArrayList<String>();

        requiredFields.forEach(element -> {
            if (element.getAttribute("type") != "radio" && element.getValue().isEmpty()) {
                problems.add("Required field is empty: " + element);
            }
        });

        if (genderRadioButtons.filterBy(selected).size() == 0) {
            problems.add("No gender selected");
        }

        assert problems.isEmpty() : String.join("\n", problems);

        return this;
    }

    public PracticeFormPage fillSubject(String subjectName) {
        subjectInput.setValue(subjectName).pressEnter();
        return this;
    }

    public PracticeFormPage fillSubjectByDropDown(String subjectName) {
        subjectInput.setValue(subjectName);
        subjectDropDownEl.click();
        return this;
    }

    public PracticeFormPage submit() {
        submitButton
                .scrollTo()
                .click();
        return this;
    }

    public PracticeFormPage checkStudentName(String studentName) {
        resultTable
                .findBy(text("Student Name"))
                .shouldHave(text(studentName));
        return this;
    }

    public PracticeFormPage checkStudentNumber(String studentNumber) {
        resultTable
                .filterBy(text("Mobile"))
                .filterBy(text(studentNumber))
                .shouldHave(size(1));
        return this;
    }

    public PracticeFormPage checkStudentGender() {
        resultTable.findBy(text("Gender"))
                .shouldHave(anyOf(text("Other"), text("Female"), text("Male")));
        return this;
    }

    public PracticeFormPage checkStudentSubjects(String subjects) {
        resultTable
                .findBy(text("Subjects"))
                .shouldHave(text("subjects"));
        return this;
    }
}
