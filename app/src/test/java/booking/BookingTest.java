package booking;

import static booking.config.BookingApiConfig.getBookingConfig;
import static booking.steps.BookingSteps.randomBooking;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
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
import booking.dto.BookingId;
import booking.dto.CreateBookingResponse;
import booking.steps.BookingSteps;
import io.restassured.response.Response;
import net.datafaker.Faker;

@Tag("API")
public class BookingTest extends BaseApiTest {
        private static final BookingConfig CFG = getBookingConfig();
        private static final Faker faker = new Faker();
        private BookingApiClient bookingApiClient = new BookingApiClient();
        private BookingSteps bookingSteps = new BookingSteps();

        @Test
        @Tag("positive")
        @DisplayName("Авторизация с корректными учетными данными")
        void auth() {
                Response resp = bookingApiClient.auth(CFG.username(), CFG.password());

                assertThat(resp.statusCode()).isEqualTo(200);
                assertThat(resp.as(AuthResponse.class).getToken()).isNotNull();
        }

        @ParameterizedTest(name = "Авторизация с некорректными учетными данными: {0}, {1}")
        @Tag("negative")
        @CsvFileSource(resources = "/wrong_credentials.csv", numLinesToSkip = 1)
        @DisplayName("Авторизация с некорректными учетными данными")
        void authWrongCredentialsTest(String username, String password) {
                Response response = bookingApiClient.auth(username, password);

                assertAll(
                                () -> assertThat(response.statusCode()).isEqualTo(200), // Strangely, the API returns
                                                                                        // 200
                                                                                        // for wrong credentials
                                () -> assertThat(response.as(AuthResponse.class).getToken()).isNull(),
                                () -> assertThat(response.jsonPath().getString("reason")).isEqualTo("Bad credentials"));
        }

        @Test
        @Tag("negative")
        @DisplayName("Авторизация с пустым телом запроса")
        void authWithEmptyBody() {
                Response response = bookingApiClient.authWithEmptyBody();
                assertThat(response.statusCode()).isEqualTo(200);

                assertAll(
                                () -> assertThat(response.statusCode()).isEqualTo(200), // Strangely, the API returns
                                                                                        // 200
                                                                                        // for wrong credentials
                                () -> assertThat(response.as(AuthResponse.class).getToken()).isNull(),
                                () -> assertThat(response.jsonPath().getString("reason")).isEqualTo("Bad credentials"));
        }

        @Test
        @Tag("negative")
        @DisplayName("Авторизация без тела запроса")
        void authWithoutBody() {
                Response response = bookingApiClient.authWithoutBody();
                assertThat(response.statusCode()).isEqualTo(200);

                assertAll(
                                () -> assertThat(response.statusCode()).isEqualTo(200), // Strangely, the API returns
                                                                                        // 200
                                                                                        // for wrong credentials
                                () -> assertThat(response.as(AuthResponse.class).getToken()).isNull(),
                                () -> assertThat(response.jsonPath().getString("reason")).isEqualTo("Bad credentials"));
        }

        @Test
        @Tag("positive")
        @DisplayName("Создание бронирования")
        void createBooking() {
                BookingDTO bookingRequest = randomBooking();
                Response resp = bookingApiClient.createBooking(bookingRequest);
                assertThat(resp.statusCode()).isEqualTo(200);

                CreateBookingResponse createBookingResponse = resp.as(CreateBookingResponse.class);
                assertThat(createBookingResponse.getBookingid()).isNotNull().isPositive();
                BookingSteps.bookingsShouldBeEqual(bookingRequest, createBookingResponse.getBooking());

        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("missingRequiredFields")
        @Tag("negative")
        @DisplayName("Создание бронирования с некорректными данными (отсутствует имя, фамилия, тело запроса)")
        void createBookingWithoutRequiredFieldsTest(String scenario,
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
        void createBookingWithNegativeTotalPrice() {
                BookingDTO bookingDTO = randomBooking();
                bookingDTO.setTotalprice(-500);
                Response res = bookingApiClient.createBooking(bookingDTO);

                assertThat(res.getStatusCode()).isEqualTo(200);
                CreateBookingResponse createBookingResponse = res.as(CreateBookingResponse.class);
                assertAll(
                                () -> assertThat(createBookingResponse.getBookingid()).isNotNull(),
                                () -> assertThat(createBookingResponse.getBooking().getTotalprice())
                                                .isEqualTo(-500));
        }

        @ParameterizedTest(name = "{0}")
        @Tag("negative")
        @DisplayName("Создание бронирования с некорректной датой")
        @MethodSource("invalidDates")
        void createBookingWithInvalidDates(String scenario, String checkin, String checkout) {
                BookingDTO bookingDTO = randomBooking();
                bookingDTO.setBookingdates(new BookingDTO.BookingDates(checkin, checkout));

                Response response = bookingApiClient.createBooking(bookingDTO);

                assertAll(
                                () -> assertThat(response.getStatusCode()).isEqualTo(200), // It would be better if the
                                                                                           // API returned 400, but it
                                                                                           // returns 500 for missing
                                                                                           // required fields
                                () -> assertThat(response.as(CreateBookingResponse.class).getBookingid()).isNotNull());
        }

        @Test
        @Tag("positive")
        @DisplayName("Обновление бронирования")
        void updateBooking() {
                CreateBookingResponse createBookingResponse = bookingSteps.createBooking();

                BookingDTO bookingDTO = randomBooking();
                Response updateResponse = bookingApiClient.updateBooking(bookingDTO,
                                createBookingResponse.getBookingid());

                assertThat(updateResponse.getStatusCode()).isEqualTo(200);

                BookingDTO updateBookingDTO = updateResponse.as(BookingDTO.class);
                BookingSteps.bookingsShouldBeEqual(bookingDTO, updateBookingDTO);
        }

        @Test
        @Tag("positive")
        @DisplayName("Частичное обновление бронирования")
        void partialUpdateBooking() {
                BookingDTO expected = randomBooking();
                Integer bookingId = bookingSteps.createBooking(expected).getBookingid();

                BookingDTO patch = BookingDTO.builder()
                                .firstname(faker.football().players())
                                .totalprice(faker.number().numberBetween(10001, 12000))
                                .bookingdates(BookingDTO.BookingDates.builder()
                                                .checkin(LocalDate.parse(expected.getBookingdates().getCheckin())
                                                                .plusDays(1).toString())
                                                .checkout(expected.getBookingdates().getCheckout())
                                                .build())
                                .build();

                Response updateResponse = bookingApiClient.partialUpdateBooking(patch, bookingId);
                assertThat(updateResponse.getStatusCode()).isEqualTo(200);
                ;
                expected.setFirstname(patch.getFirstname());
                expected.setTotalprice(patch.getTotalprice());
                expected.setBookingdates(patch.getBookingdates());
                assertThat(updateResponse.getStatusCode()).isEqualTo(200);

                BookingSteps.bookingsShouldBeEqual(expected, updateResponse.as(BookingDTO.class));
                BookingSteps.bookingsShouldBeEqual(expected,
                                bookingApiClient.getBooking(bookingId).as(BookingDTO.class));
        }

        @Test
        @Tag("positive")
        @DisplayName("Удаление бронирования")
        void deleteBooking() {
                Integer bookingId = bookingSteps.createBooking().getBookingid();
                Response deleteResponse = bookingApiClient.deleteBooking(bookingId);
                assertThat(deleteResponse.getStatusCode()).isEqualTo(201);

                Response getResponse = bookingApiClient.getBooking(bookingId);
                assertThat(getResponse.getStatusCode()).isEqualTo(404);
        }

        @Test
        @Tag("positive")
        @DisplayName("Получение бронирования")
        void getBooking() {
                BookingDTO bookingRequest = randomBooking(); // To garantee that in future check will be our booking,
                                                             // not some other one, that could be saved in the system by
                                                             // other tests or users
                Response createResp = bookingApiClient.createBooking(bookingRequest);
                assertThat(createResp.getStatusCode()).isEqualTo(200);

                Integer bookingId = createResp.as(CreateBookingResponse.class).getBookingid();
                Response getResponse = bookingApiClient.getBooking(bookingId);
                assertThat(getResponse.getStatusCode()).isEqualTo(200);

                BookingSteps.bookingsShouldBeEqual(
                                bookingRequest,
                                getResponse.as(BookingDTO.class));
        }

        @Test
        @Tag("positive")
        @DisplayName("Получение списка бронирований по фамилии")
        void getBookingsByLastName() {
                int bookingQuantity = 5;
                String lastName = UUID.randomUUID().toString(); // Using a random uuid as last name to avoid conflicts
                                                                // with existing bookings. Bad practice, because using
                                                                // this last name would be forbidden in a real project

                Integer[] bookingIds = generateBookings(bookingQuantity, lastName); // Using massive instead of List to
                                                                                    // make test faster, because we know
                                                                                    // the size of the array in advance
                                                                                    // and don't need to resize it

                Response resp = bookingApiClient.getBookings(Map.of("lastname", lastName));
                assertThat(resp.getStatusCode()).isEqualTo(200);

                BookingId[] bookings = resp.as(BookingId[].class);
                BookingSteps.bookingListShouldBeValid(bookings, bookingIds, bookingQuantity);
        }

        private static Stream<Arguments> missingRequiredFields() {
                BookingDTO withoutFirstname = randomBooking();
                withoutFirstname.setFirstname(null);

                BookingDTO withoutLastname = randomBooking();
                withoutLastname.setLastname(null);

                return Stream.of(
                                Arguments.of("Отсутствует имя", withoutFirstname),
                                Arguments.of("Отсутствует фамилия", withoutLastname),
                                Arguments.of("Пустое тело", new BookingDTO()));
        }

        private static Stream<Arguments> invalidDates() {
                return Stream.of(
                                Arguments.of("Дата в неверном формате", "not-a-date", "2027-01-01"),
                                Arguments.of("Выезд раньше заезда", "2027-01-01", "2026-01-01"));
        }

        private Integer[] generateBookings(int bookingQuantity, String lastName) {
                Integer[] bookingIds = new Integer[bookingQuantity];
                for (int i = 0; i < bookingQuantity; i++) {
                        BookingDTO bookingDTO = randomBooking();
                        bookingDTO.setLastname(lastName);

                        Integer bookingId = bookingSteps.createBooking(bookingDTO).getBookingid();
                        bookingIds[i] = bookingId;

                }
                return bookingIds;
        }
}