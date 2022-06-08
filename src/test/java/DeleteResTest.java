import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;


class DeleteResTest {

    @Test
     void loginTest() {

       given()
                .delete("https://reqres.in/api/login")
                .then()
                .statusCode(204);

    }
}
