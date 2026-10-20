package booking;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import booking.dto.AuthRequest;
import booking.dto.AuthResponse;
import booking.dto.CreateBookingDto;
import booking.dto.CreateBookingResponse;
import io.qameta.allure.Description;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;

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
    @Description ("Auth test")
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

    @Test
    @Tag("positive")
    @Description ("Create booking test")
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

    private static CreateBookingDto buildCreateBookingRequest() {
        return CreateBookingDto.builder()
                .firstname("Barack")
                .lastname("Obama")
                .totalprice(1000)
                .depositpaid(false)
                .bookingdates(CreateBookingDto.BookingDates.builder()
                        .checkin("2026-01-01")
                        .checkout("2027-01-01")
                        .build())
                .additionalneeds("Newspaper")
                .build();
    }
}