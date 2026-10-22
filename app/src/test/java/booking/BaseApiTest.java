package booking;

import org.junit.jupiter.api.BeforeAll;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class BaseApiTest {

    @BeforeAll
    static void setUp() {
        RestAssured.filters(
            new RequestLoggingFilter(),
            new ResponseLoggingFilter(),
            new AllureRestAssured()
        );
    }
}