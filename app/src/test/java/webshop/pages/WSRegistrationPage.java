package webshop.pages;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

public class WSRegistrationPage {
    private final SelenideElement maleGenderRadio = $("input#gender-male");
    private final SelenideElement pageTitle = $("div.page-title");
    private final SelenideElement firstNameInput = $("input#FirstName");
    private final SelenideElement lastNameInput = $("input#LastName");
    private final SelenideElement emailInput = $("input#Email");
    private final SelenideElement passwordInput = $("input#Password");
    private final SelenideElement confirmPasswordInput = $("input#ConfirmPassword");
    private final SelenideElement submitRegisterButton = $("input#register-button");
    private final SelenideElement resultText = $("div.result");
    private final ElementsCollection headrLinks = $$("div.header-links ul li a");

    public WSRegistrationPage register(String firstName, String lastName,String email, String password) {
        selectMaleGender()
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .enterEmail(email)
                .enterPassword(password)
                .confirmPassword(password)
                .submitRegistration()
                .checkRegistrationCompleted();
        return this;
    }

    public WSRegistrationPage verifyRegistrationPage() {
        pageTitle.shouldHave(text("Register"));
        return this;
    }

    public WSRegistrationPage selectMaleGender() {
        maleGenderRadio.click();
        return this;
    }

    public WSRegistrationPage enterFirstName(String firstName) {
        firstNameInput.setValue(firstName);
        return this;
    }

    public WSRegistrationPage enterLastName(String lastName) {
        lastNameInput.setValue(lastName);
        return this;
    }

    public WSRegistrationPage enterEmail(String email) {
        emailInput.setValue(email);
        return this;
    }

    public WSRegistrationPage enterPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public WSRegistrationPage confirmPassword(String password) {
        confirmPasswordInput.setValue(password);
        return this;
    }

    public WSRegistrationPage submitRegistration() {
        submitRegisterButton.click();
        return this;
    }

    public WSRegistrationPage checkRegistrationCompleted() {
        resultText.shouldHave(text("Your registration completed"));
        return this;
    }

    public WSRegistrationPage checkEmailIsShown(String email) {
        headrLinks.get(0).shouldHave(text(email));
        return this;
    }
}
