package booking;

import static booking.config.BookingApiConfig.getBookingConfig;
import static booking.steps.BookingSteps.randomBooking;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
                Response resp = bookingApiClient.auth(username, password);

                assertThat(resp.statusCode()).isEqualTo(200); // Strangely, the API returns 200 for wrong credentials
                assertThat(resp.as(AuthResponse.class).getToken()).isNull();
        }

        @Test
        @Tag("negative")
        @DisplayName("Авторизация с пустым телом запроса")
        void authWithEmptyBody() {
                Response response = bookingApiClient.authWithEmptyBody();
                assertThat(response.statusCode()).isEqualTo(200);

                assertThat(response.as(AuthResponse.class).getToken()).isNull();
        }

        @Test
        @Tag("negative")
        @DisplayName("Авторизация без тела запроса")
        void authWithoutBody() {
                Response response = bookingApiClient.authWithoutBody();
                assertThat(response.statusCode()).isEqualTo(200);

                assertThat(response.as(AuthResponse.class).getToken()).isNull();
        }

        @Test
        @Tag("positive")
        @DisplayName("Создание бронирования")
        void createBooking() {
                Response resp = bookingApiClient.createBooking(randomBooking());
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
        @DisplayName("Создание бронирования с некорректными данными (отсутствует имя, фамилия)")
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
                CreateBookingResponse createBookingResponse = bookingSteps.createBooking();

                BookingDTO bookingDTO = new BookingDTO(
                                faker.football().players(),
                                faker.number().numberBetween(10001, 12000),
                                "2026-02-01");

                Response updateResponse = bookingApiClient
                                .partialUpdateBooking(bookingDTO,
                                                createBookingResponse.getBookingid());
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

                BookingSteps.bookingsShouldBeEqual(createResp.as(CreateBookingResponse.class).getBooking(),
                                bookingRequest);
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
                                Arguments.of(withoutFirstname),
                                Arguments.of(withoutLastname));
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