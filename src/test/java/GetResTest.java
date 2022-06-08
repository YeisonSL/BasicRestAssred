import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetResTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    void getSingleUserTest() {

        given()
                .get("users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("data.id", equalTo(2));

    }
}
