import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostResTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    void loginTest() {

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}"
                )
                .post("https://reqres.in/api/login")
                .then()
                .log().all()
                .statusCode(200)
                .body("token", notNullValue());

    }

    @Test
    void allUsers() {

        Response response = given()
                .get("users?page=2");

        Headers headers = response.headers();
        int statuCode = response.statusCode();
        String body = response.getBody().asString();
        String contentType = response.getContentType();

        assertThat(statuCode, equalTo(HttpStatus.SC_OK));
        System.out.println("Body: "+ body);
        System.out.println("Headers:" + headers);
        System.out.println("ContenType: " + contentType);
        System.out.println("*********************");
        System.out.println(headers.get("Transfer-Encoding"));
    }
}
