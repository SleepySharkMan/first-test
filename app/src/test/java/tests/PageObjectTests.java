package tests;

import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.WebDriverRunner;
import pages.DemoQAWelcomePage;
import pages.YandexSearchPage;

import static com.codeborne.selenide.Selenide.open;

public class PageObjectTests {

        @AfterEach
        public void tearDown() {
                WebDriverRunner.closeWebDriver();
        }

        @Test
        @Tag("flaky")
        @DisplayName ("Проверить, что цена обучения — 47000 ₽")
        void mentoringPriceShouldBe47000Test() {
                new YandexSearchPage()
                                .openYandexSearchPage()
                                .fill("blugakov qa")
                                .submit()
                                .closeDistributionWin()
                                .openLink("ivanbulgakovqa.ru")
                                .clickPriceMenuItem()
                                .openWindow()
                                .openPricePage()
                                .checkPrice("₽ 47 000.00");
        }

        @Test
        void myFirstTest() {

                /*
                 ** Минимум:**F
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

                String firstName = "Ivan";
                String lastName = "Ivanov";
                String userNumber = Long.toString(new Random().nextLong(1000000000L, 9999999999L));
                String subject = "Maths";
                String substringOfSubject = "e";

                open("https://demoqa.com/", DemoQAWelcomePage.class)
                                .openСhapterPage("Elements")
                                .openSection("Forms")
                                .openRequiredForm("Practice Form")
                                .fillFirstName(firstName)
                                .fillLastName(lastName)
                                .fillNumber(userNumber)
                                .fillGenderRadio()
                                .checkRequiredFields()
                                .fillSubject(subject)
                                .fillSubjectByDropDown(substringOfSubject)
                                .submit()
                                .checkStudentName(firstName + " " + lastName)
                                .checkStudentNumber(userNumber)
                                .checkStudentGender()
                                .checkStudentSubjects(subject +", " + substringOfSubject.toUpperCase());
        }
}
