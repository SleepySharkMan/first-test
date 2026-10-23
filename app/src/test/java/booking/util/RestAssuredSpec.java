package booking.util;

import static booking.util.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

public class RestAssuredSpec {

    public static RequestSpecification requestSpec = with()
            .filter(withCustomTemplates())
            .log().method()
            .log().uri()
            .log().headers()
            .log().body();

    public static void setupRestAssured() {
        RestAssured.requestSpecification = requestSpec;
        
        RestAssured.filters(
                new ResponseLoggingFilter(LogDetail.STATUS),
                new ResponseLoggingFilter(LogDetail.BODY)
        );
    }
}
