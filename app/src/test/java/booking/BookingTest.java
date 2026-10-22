package booking;

import static booking.config.BookingApiConfig.getBookingConfig;
import static booking.steps.BookingSteps.buildBookingRequest;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import booking.config.BookingConfig;
import booking.dto.AuthResponse;
import booking.dto.BookingApiClient;
import booking.dto.BookingDTO;
import booking.dto.CreateBookingResponse;
import booking.steps.BookingSteps;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.datafaker.Faker;

public class BookingTest extends BaseApiTest {
        private static final BookingConfig CFG = getBookingConfig();
        private static final Faker faker = new Faker();
        private BookingApiClient bookingApiClient = new BookingApiClient();
        RequestSpecification requestSpec = new RequestSpecBuilder()
                        .setContentType(ContentType.JSON)
                        .build();

        @Test
        @Tag("positive")
        @DisplayName("Авторизация с корректными учетными данными")
        void authTest() {
                Response resp = bookingApiClient.auth(CFG.username(), CFG.password());

                assertThat(resp.statusCode()).isEqualTo(200);
                assertThat(resp.as(AuthResponse.class).getToken()).isNotNull();
        }

        @ParameterizedTest(name = "Авторизация с некорректными учетными данными: {0}, {1}")
        @Tag("negative")
        @CsvFileSource(resources = "/wrong_credentials.csv", numLinesToSkip = 1)
        @DisplayName("Авторизация с некорректными учетными данными")
        void authWrongCredentialsTest(String username, String password) {
                Response resp = bookingApiClient.auth(username, password);

                assertThat(resp.statusCode()).isEqualTo(200); // Strangely, the API returns 200 for wrong credentials
                assertThat(resp.as(AuthResponse.class).getToken()).isNull();
        }

        @Test
        @Tag("negative")
        @DisplayName("Авторизация с пустым телом запроса")
        void authWithEmptyBody() {
                AuthResponse resp = given()
                                .spec(requestSpec)
                                .body("{}")
                                .when()
                                .post(CFG.bookingUrl() + "/auth")
                                .then()
                                .statusCode(200) // Strangely, the API returns 200 for wrong credentials
                                .extract().as(AuthResponse.class);

                assertThat(resp.getToken()).isNull();
        }

        @Test
        @Tag("negative")
        @DisplayName("Авторизация без тела запроса")
        void authWithoutBodyTest() {
                AuthResponse resp = given()
                                .spec(requestSpec)
                                .when()
                                .post(CFG.bookingUrl() + "/auth")
                                .then()
                                .statusCode(200) // Strangely, the API returns 200 for wrong credentials
                                .extract().as(AuthResponse.class);

                assertThat(resp.getToken()).isNull();
        }

        @Test
        @Tag("positive")
        @DisplayName("Создание бронирования")
        void createTest() {
                Response resp = bookingApiClient.createBooking(buildBookingRequest());
                assertThat(resp.statusCode()).isEqualTo(200);

                CreateBookingResponse createBookingResponse = resp.as(CreateBookingResponse.class);
                assertAll(
                                () -> assertThat(createBookingResponse).isNotNull(),
                                () -> assertThat(createBookingResponse.getBookingid()).isNotNull(),
                                () -> assertThat(createBookingResponse.getBooking()).isNotNull());
        }

        @ParameterizedTest
        @MethodSource("missingRequiredFields")
        @Tag("negative")
        @DisplayName("Создание бронирования без имени/фамилии")
        void createBookingWithoutRequiredFieldsTest(
                        BookingDTO bookingDTO) {
                Response response = bookingApiClient.createBooking(bookingDTO);

                assertAll(
                                () -> assertThat(response.getStatusCode()).isEqualTo(500), // It would be better if the
                                                                                           // API returned 400, but it
                                                                                           // returns 500 for missing
                                                                                           // required fields
                                () -> assertThat(response.getBody().asString()).contains("Internal Server Error"));

        }

        @Test
        @Tag("negative")
        @DisplayName("Создание бронирования с отрицательной стоимостью")
        void createBookingWithNegativeTotalPriceTest() {
                Response res = bookingApiClient.createBooking(
                                validBookingBuilder().totalprice(-500).build());

                assertThat(res.getStatusCode()).isEqualTo(200);
                CreateBookingResponse createBookingResponse = res.as(CreateBookingResponse.class);
                assertAll(
                                () -> assertThat(createBookingResponse.getBookingid()).isNotNull(),
                                () -> assertThat(createBookingResponse.getBooking().getTotalprice())
                                                .isEqualTo(-500));
        }

        @Test
        @Tag("positive")
        @DisplayName("Обновление бронирования")
        void updateBookingTest() {
                Response createResp = bookingApiClient.createBooking(
                                buildBookingRequest());
                assertThat(createResp.getStatusCode()).isEqualTo(200);

                BookingDTO bookingDTO = buildBookingRequest();
                Response updateResponse = bookingApiClient.updateBooking(bookingDTO,
                                createResp.as(CreateBookingResponse.class).getBookingid());

                assertThat(updateResponse.getStatusCode()).isEqualTo(200);

                BookingDTO updateBookingDTO = updateResponse.as(BookingDTO.class);
                BookingSteps.bookingsShouldBeEqual(bookingDTO, updateBookingDTO);
        }

        @Test
        @Tag("positive")
        @DisplayName("Частичное обновление бронирования")
        void partialUpdateBookingTest() {
                Response createResp = bookingApiClient.createBooking(buildBookingRequest());
                assertThat(createResp.getStatusCode()).isEqualTo(200);

                BookingDTO bookingDTO = new BookingDTO(
                                faker.football().players(),
                                faker.number().numberBetween(10001, 12000),
                                "2026-02-01");

                Response updateResponse = bookingApiClient
                                .partialUpdateBooking(bookingDTO,
                                                createResp.as(CreateBookingResponse.class).getBookingid());
                assertThat(updateResponse.getStatusCode()).isEqualTo(200);

                BookingDTO updatedBookingDto = updateResponse.as(BookingDTO.class);
                assertAll(
                                () -> assertThat(bookingDTO.getFirstname()).isEqualTo(updatedBookingDto.getFirstname()),
                                () -> assertThat(bookingDTO.getTotalprice())
                                                .isEqualTo(updatedBookingDto.getTotalprice()),
                                () -> assertThat(bookingDTO.getBookingdates().getCheckin())
                                                .isEqualTo(updatedBookingDto.getBookingdates().getCheckin()));
        }

        @Test
        @Tag("positive")
        @DisplayName("Удаление бронирования")
        void deleteBookingTest() {
                Response createResp = bookingApiClient.createBooking(buildBookingRequest());
                assertThat(createResp.getStatusCode()).isEqualTo(200);

                Integer bookingId = createResp.as(CreateBookingResponse.class).getBookingid();
                Response deleteResponse = bookingApiClient.deleteBooking(bookingId);
                assertThat(deleteResponse.getStatusCode()).isEqualTo(201);

                Response getResponse = bookingApiClient.getBooking(bookingId);
                assertThat(getResponse.getStatusCode()).isEqualTo(404);
        }

        private static BookingDTO.BookingDTOBuilder validBookingBuilder() {
                return BookingDTO.builder()
                                .firstname("Ivan")
                                .lastname("Ivanov")
                                .totalprice(1000)
                                .depositpaid(false)
                                .bookingdates(BookingDTO.BookingDates.builder()
                                                .checkin("2026-01-01")
                                                .checkout("2027-01-01")
                                                .build())
                                .additionalneeds("Newspaper");
        }

        private static Stream<Arguments> missingRequiredFields() {
                return Stream.of(
                                Arguments.of(
                                                validBookingBuilder().firstname(null).build()),
                                Arguments.of(
                                                validBookingBuilder().lastname(null).build()));
        }

}