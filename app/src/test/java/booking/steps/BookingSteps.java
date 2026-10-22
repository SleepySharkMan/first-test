package booking.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import booking.dto.BookingDTO;
import io.qameta.allure.Step;
import net.datafaker.Faker;

public class BookingSteps {

    private static final Faker faker = new Faker();

    @Step("Проверка, что данные бронирования совпадают с ожидаемыми")
    public static void bookingsShouldBeEqual(BookingDTO expected, BookingDTO actual) {
        assertAll(
                () -> assertThat(actual.getFirstname())
                        .as("firstname отличается от ожидаемого")
                        .isEqualTo(expected.getFirstname()),
                () -> assertThat(actual.getLastname())
                        .as("lastName отличается от ожидаемого")
                        .isEqualTo(expected.getLastname()),
                () -> assertThat(actual.getTotalprice())
                        .as("totalPrice отличается от ожидаемого")
                        .isEqualTo(expected.getTotalprice()),
                () -> assertThat(actual.getDepositpaid())
                        .as("depositpaid отличается от ожидаемого")
                        .isEqualTo(expected.getDepositpaid()),
                () -> assertThat(actual.getAdditionalneeds())
                        .as("additionalNeeds отличается от ожидаемого")
                        .isEqualTo(expected.getAdditionalneeds()),
                () -> assertThat(actual.getBookingdates())
                        .as("bookingdates равен null")
                        .isNotNull(),
                () -> assertThat(actual.getBookingdates().getCheckin())
                        .as("checkin отличается от ожидаемого")
                        .isEqualTo(expected.getBookingdates().getCheckin()),
                () -> assertThat(actual.getBookingdates().getCheckout())
                        .as("checkout отличается от ожидаемого")
                        .isEqualTo(expected.getBookingdates().getCheckout()));
    }

    @Step("Создание объекта BookingDTO с рандомными данными")
    public static BookingDTO buildBookingRequest() {
                return BookingDTO.builder()
                                .firstname(faker.name().firstName())
                                .lastname(faker.name().lastName())
                                .totalprice(faker.number().numberBetween(0, 10000))
                                .depositpaid(faker.bool().bool())
                                .bookingdates(BookingDTO.BookingDates.builder()
                                                .checkin("2026-01-01")
                                                .checkout("2027-01-01")
                                                .build())
                                .additionalneeds(faker.food().fruit())
                                .build();
        }
}