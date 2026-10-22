package booking.dto;

import static booking.config.BookingApiConfig.getBookingConfig;
import static io.restassured.RestAssured.given;

import booking.config.BookingConfig;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BookingApiClient {
    private static final BookingConfig CFG = getBookingConfig();
    RequestSpecification requestSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .build();

    @Step("Авторизация с логином: {username} и паролем: {password}")
    public Response auth(String username, String password) {
        return given()
                .spec(requestSpec)
                .body(new AuthRequest(username, password))
                .when()
                .post(CFG.bookingUrl() + "/auth")
                .then()
                .extract().response();
    }

    @Step("Создание бронирования")
    public Response createBooking(BookingDTO bookingDTO) {
        return given()
                .spec(requestSpec)
                .cookie("token", getToken())
                .body(bookingDTO)
                .when()
                .post(CFG.bookingUrl() + "/booking")
                .then()
                .extract().response();
    }

    @Step ("Обновление бронирования по ID: {id}")
    public Response updateBooking(BookingDTO bookingDTO, Integer id) {
        return given()
                .spec(requestSpec)
                .cookie("token", getToken())
                .body(bookingDTO)
                .pathParam("BOOKING_ID", id)
                .put(CFG.bookingUrl() + "/booking/{BOOKING_ID}")
                .then()
                .extract().response();
    }

    @Step("Частичное обновление бронирования по ID: {id}")
    public Response partialUpdateBooking(BookingDTO bookingDTO, Integer id) {
        return given()
                .spec(requestSpec)
                .cookie("token", getToken())
                .body(bookingDTO)
                .pathParam("BOOKING_ID", id)
                .patch(CFG.bookingUrl() + "/booking/{BOOKING_ID}")
                .then()
                .extract().response();
    }

    @Step ("Удаление бронирования по ID: {id}")
    public Response deleteBooking(Integer id) {
        return given()
                .spec(requestSpec)
                .cookie("token", getToken())
                .pathParam("BOOKING_ID", id)
                .delete(CFG.bookingUrl() + "/booking/{BOOKING_ID}")
                .then()
                .extract().response();
    }

    @Step ("Получение бронирования по ID: {id}")    
    public Response getBooking(Integer id) {
        return given()
                .pathParam("BOOKING_ID", id)
                .get(CFG.bookingUrl() + "/booking/{BOOKING_ID}")
                .then()
                .extract().response();
    }

    @Step("Получение токена авторизации")
    private String getToken() {
        return auth(CFG.username(), CFG.password()).as(AuthResponse.class).getToken();
    }
}
