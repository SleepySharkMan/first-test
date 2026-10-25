package booking;

import org.junit.jupiter.api.BeforeAll;

import booking.util.RestAssuredSpec;

public class BaseApiTest {

    @BeforeAll
    static void setUp() {
        RestAssuredSpec.setupRestAssured();
    }
}