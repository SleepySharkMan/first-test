package booking;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import booking.dto.AuthRequest;
import booking.dto.AuthResponse;
import booking.dto.CreateBookingDto;
import booking.dto.CreateBookingResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class BookingTest {

        private static final String BOOKING_URL = "https://restful-booker.herokuapp.com";

        @BeforeAll
        static void setUp() {
                RestAssured.filters(
                                new RequestLoggingFilter(),
                                new ResponseLoggingFilter(),
                                new AllureRestAssured());
        }

        @Test
        @Tag("positive")
        @Description("Auth test")
        @Step("Авторизация с корректными учетными данными")
        void authTest() {
                AuthResponse resp = given()
                                .contentType(ContentType.JSON)
                                .body(new AuthRequest("admin", "password123"))
                                .when()
                                .post(BOOKING_URL + "/auth")
                                .then()
                                .statusCode(200)
                                .extract().as(AuthResponse.class);

                assertThat(resp.getToken()).isNotNull();
        }

        @ParameterizedTest
        @Tag("negative")
        @Description("Авторизация с некорректными учетными данными")
        @CsvFileSource(resources = "/wrong_credentials.csv", numLinesToSkip = 1)
        @Step("Авторизация с некорректными учетными данными: {username}, {password}")
        void authWrongCredentialsTest(String username, String password) {
                AuthResponse resp = given()
                                .contentType(ContentType.JSON)
                                .body(new AuthRequest(username, password))
                                .when()
                                .post(BOOKING_URL + "/auth")
                                .then()
                                .statusCode(200) // Strangely, the API returns 200 even for wrong credentials
                                .extract().as(AuthResponse.class);

                assertThat(resp.getToken()).isNull();
        }

        @Test
        @Tag("negative")
        @Step("Авторизация с пустым телом запроса")
        void authWithEmptyBody() {
                AuthResponse resp = given()
                                .contentType(ContentType.JSON)
                                .body("{}")
                                .when()
                                .post(BOOKING_URL + "/auth")
                                .then()
                                .statusCode(200) // Strangely, the API returns 200 even for wrong credentials
                                .extract().as(AuthResponse.class);

                assertThat(resp.getToken()).isNull();
        }

        @Test
        @Tag("negative")
        @Step("Авторизация  без тела запроса")
        void authWithoutyBody() {
                AuthResponse resp = given()
                                .contentType(ContentType.JSON)
                                .when()
                                .post(BOOKING_URL + "/auth")
                                .then()
                                .statusCode(200) // Strangely, the API returns 200 even for wrong credentials
                                .extract().as(AuthResponse.class);

                assertThat(resp.getToken()).isNull();
        }

        @Test
        @Tag("positive")
        @Description("Create booking test")
        @Step("Создание бронирования")
        void createTest() {
                CreateBookingResponse res = given()
                                .contentType(ContentType.JSON)
                                .body(buildCreateBookingRequest())
                                .when()
                                .post(BOOKING_URL + "/booking")
                                .then()
                                .statusCode(200)
                                .extract().as(CreateBookingResponse.class);

                assertThat(res.getBookingid()).isNotNull();
                assertThat(res.getBooking().getTotalprice()).isEqualTo(1000);
                assertThat(res.getBooking().getBookingdates().getCheckin()).isEqualTo("2026-01-01");
                assertThat(res.getBooking().getDepositpaid()).isFalse();
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("missingRequiredFields")
        @Tag("negative")
        @DisplayName("Создание бронирования без имени/фамилии")
        void createBookingWithoutRequiredFields(
                        CreateBookingDto request) {
                Response response = given()
                                .contentType(ContentType.JSON)
                                .body(request)
                                .when()
                                .post(BOOKING_URL + "/booking");

                assertThat(response.getStatusCode()).isEqualTo(500); // It would be better if the API returned 400, but
                                                                     // it returns 500 for missing required fields
                assertThat(response.getBody().asString()).contains("Internal Server Error");
        }

        @Test
        @Tag("negative")
        @DisplayName("Создание бронирования без имени/фамилии")
        void createBookingWithNegativeTotalPrice() {
                CreateBookingResponse res = given()
                                .contentType(ContentType.JSON)
                                .body(buildCreateBookingRequestWithSpecificData(
                                                "Ivan", "Ivanov", -500, false,
                                                "2026-01-01", "2027-01-01", "Newspaper"))
                                .when()
                                .post(BOOKING_URL + "/booking")
                                .then()
                                .statusCode(200)
                                .extract().as(CreateBookingResponse.class);

                assertThat(res.getBookingid()).isNotNull();
                assertThat(res.getBooking().getTotalprice()).isEqualTo(-500);
                assertThat(res.getBooking().getBookingdates().getCheckin()).isEqualTo("2026-01-01");
                assertThat(res.getBooking().getDepositpaid()).isFalse();
        }

        private static CreateBookingDto buildCreateBookingRequest() {
                return CreateBookingDto.builder()
                                .firstname("Ivan")
                                .lastname("Ivanov")
                                .totalprice(1000)
                                .depositpaid(false)
                                .bookingdates(CreateBookingDto.BookingDates.builder()
                                                .checkin("2026-01-01")
                                                .checkout("2027-01-01")
                                                .build())
                                .additionalneeds("Newspaper")
                                .build();
        }

        private static Stream<Arguments> missingRequiredFields() {
                return Stream.of(
                                Arguments.of(
                                                "Отсутствует firstname",
                                                buildCreateBookingRequestWithSpecificData(
                                                                null, "Ivanov", 1000, false,
                                                                "2026-01-01", "2027-01-01", "Newspaper")),
                                Arguments.of(
                                                "Отсутствует lastname",
                                                buildCreateBookingRequestWithSpecificData(
                                                                "Ivan", null, 1000, false,
                                                                "2026-01-01", "2027-01-01", "Newspaper")));
        }

        private static CreateBookingDto buildCreateBookingRequestWithSpecificData(
                        String firstname,
                        String lastname,
                        Integer totalprice,
                        Boolean depositpaid,
                        String checkin,
                        String checkout,
                        String additionalneeds) {
                return CreateBookingDto.builder()
                                .firstname(firstname)
                                .lastname(lastname)
                                .totalprice(totalprice)
                                .depositpaid(depositpaid)
                                .bookingdates(CreateBookingDto.BookingDates.builder()
                                                .checkin(checkin)
                                                .checkout(checkout)
                                                .build())
                                .additionalneeds(additionalneeds)
                                .build();
        }

}