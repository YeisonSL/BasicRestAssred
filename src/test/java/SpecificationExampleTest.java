import Pojo.CreateUserRequest;
import Pojo.CreateUserResponse;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SpecificationExampleTest {

    @BeforeAll
    public static void setup() {
        RestAssured.requestSpecification = defaultRequestSpecification();

    }


    @Test
    void createUserPojoTest() {

        CreateUserRequest user = new CreateUserRequest();
        user.setName("morpheus");
        user.setJob("leader");

        CreateUserResponse response = given().spec(requestSpecification("https://reqres.in", "/api", ContentType.JSON))
                .when()
                .body(user)
                .post("users")
                .then()
                .spec(responseSpecification(201, ContentType.JSON))
                .contentType(equalTo("application/json; charset=utf-8"))
                .extract()
                .body()
                .as(CreateUserResponse.class);
        assertThat(response.getName(), equalTo(user.getName()));
        assertThat(response.getJob(), equalTo(user.getJob()));


    }


    private static RequestSpecification defaultRequestSpecification() {
        List<Filter> filters = new ArrayList<>();
        filters.add(new RequestLoggingFilter());
        filters.add(new ResponseLoggingFilter());

        return new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .setBasePath("/api")
                .setContentType(ContentType.JSON)
                .build();
    }


    private RequestSpecification requestSpecification(String url, String path, ContentType contentType) {
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setBasePath(path)
                .setContentType(contentType)
                .build();
    }

    private ResponseSpecification responseSpecification(int status, ContentType contentType) {
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .expectContentType(contentType)
                .build();
    }
}
