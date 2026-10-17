package mentor.tests;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.anyOf;
import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.switchTo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

public class QaTest {

        @AfterEach
        public void tearDown() {
                WebDriverRunner.closeWebDriver();
        }

        @Test
        @Tag("flaky")
        void mentoringPriceShouldBe47000Test() {
                Configuration.browser = "chrome";
                Configuration.holdBrowserOpen = true;
                open("https://ya.ru");
                SelenideElement searchInput = $("#text");
                searchInput.setValue("blugakov qa");
                if ($("button[type='submit']").isDisplayed()) {
                        $("button[type='submit']").click();
                } else {
                        searchInput.pressEnter();
                }
                SelenideElement distribution = $(".DistributionActions button");
                if (distribution.is(visible, Duration.ofSeconds(3))) {
                        distribution.click();
                }
                $(byText("ivanbulgakovqa.ru")).click();
                switchTo().window(1);
                $$(".t-menu__list li").last().shouldBe(clickable).click();
                $x("/html/body/div[1]/div[42]/div/div/div[32]/div/a/div/span").click();
                $(byText("Бегу оплачивать")).click();
                switchTo().window(2);
                $$("aside h3").shouldHave(size(2)).first().shouldHave(text("\r\n" + //
                                "₽ 47 000.00"));
        }

        @Test
        void myFirstTest() {

                /*
                 ** Минимум:**
                 * 
                 * - Не менее **3 шагов** в сценарии
                 * - Хотя бы один **клик** по элементу
                 * - Хотя бы один **ввод данных** в поле
                 * - Хотя бы одна **проверка результата** (assert) — без неё это не тест, а
                 * просто скрипт
                 ** 
                 * По желанию (усложнения):**
                 * 
                 * - Поиск нужного элемента через **коллекцию** (`$$`)
                 * - **Скролл** к элементу
                 * - Работа с **выпадающими списками**, чекбоксами, радио-кнопками
                 * - Несколько проверок в одном тесте
                 * 
                 * 
                 * Зайти на https://demoqa.com/
                 * Проскролить до блока "Elements"
                 * Перейти в раздел Elements
                 * Нажать на Forms
                 * Заполнить обязательные поля форму (First Name, Last Name, Gender, Mobile
                 * Number)
                 * Проверить, что обязательные поля заполнены
                 * Добавть 2 предмета (Maths, English) в поле Subjects
                 * Проскролить до Submit
                 * Нажать на кнопку Submit
                 * Проверить, что в таблице с результатами отображаются введенные данные
                 */

                String name = "Ivan";
                String lastName = "Ivanov";
                String userNumber = Long.toString(new Random().nextLong(1000000000L, 9999999999L));

                Configuration.browser = "chrome";
                Configuration.holdBrowserOpen = true;
                open("https://demoqa.com/");
                $(byText("Elements")).scrollTo().click();
                $$(".element-group").get(1).click();
                $$(".router-link").findBy(text("Practice Form")).click();

                $("#firstName").setValue(name);
                $("#lastName").setValue(lastName);
                $$("input[type=\"radio\"]").get(new Random().nextInt(3)).click();

                $("#userNumber").setValue(userNumber);

                ArrayList<String> problems = new ArrayList<String>();

                $$("[required]").forEach(element -> {
                        if (element.getAttribute("type") != "radio" && element.getValue().isEmpty()) {
                                problems.add("Required field is empty: " + element);
                        }
                });

                if ($$("input[name='gender']").filterBy(selected).size() == 0) {
                        problems.add("No gender selected");
                }

                assert problems.isEmpty() : String.join("\n", problems);

                $("#subjectsInput").setValue("Maths").pressEnter();
                $("#subjectsInput").setValue("e");
                $$(".subjects-auto-complete__option").first().click();

                $("#submit").scrollTo().click();

                $$("tr")
                                .findBy(text("Student Name"))
                                .shouldHave(text(name + " " + lastName));

                $$("tr")
                                .filterBy(text("Mobile"))
                                .filterBy(text(userNumber))
                                .shouldHave(size(1));

                $$("tr")
                                .findBy(text("Gender"))
                                .shouldHave(anyOf(text("Other"), text("Female"), text("Male")));

                $$("tr")
                                .findBy(text("Subjects"))
                                .shouldHave(text("Maths, English"));
        }
}
