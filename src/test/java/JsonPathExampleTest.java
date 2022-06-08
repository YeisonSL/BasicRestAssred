import Pojo.CreateUserRequest;
import Pojo.CreateUserResponse;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class JsonPathExampleTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
     void allUsers() {

        String response = given()
                .when()
                .get("users?page=2")
                .then()
                .extract()
                .body()
                .asString();

        int pageNumber = from(response).get("page");
        int totalPages = from(response).get("total_pages");
        int idFirstUser = from(response).get("data[0].id");

        System.out.println("pageNumber: " +pageNumber);
        System.out.println("totalPages: " +totalPages);
        System.out.println("idFirstUser: " +idFirstUser);


        List<Map> userWithIdGreaterThan10  = from(response).get("data.findAll { user -> user.id > 10 }" );
        String email = userWithIdGreaterThan10.get(0).get("id").toString();

        List<Map> user  = from(response).get("data.findAll { user -> user.id > 10 && user.last_name =='Howell'}" );
        int id = Integer.valueOf(user.get(0).get("id").toString());


    }


    @Test
     void createUserTest(){
        String response = given()
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}")
                .post("users")
                .then().extract().body().asString();

        CreateUserResponse user = from(response).getObject("", CreateUserResponse.class);
        System.out.println("User id: " + user.getId());
        System.out.println("User jobn: "+ user.getJob());

    }



    @Test
     void createUserPojoTest(){

        CreateUserRequest user = new CreateUserRequest();
        user.setName("morpheus");
        user.setJob("leader");

        CreateUserResponse response = given()
                .when()
                .body(user)
                .post("users")
                .then()
                .statusCode(201)
                .contentType(equalTo("application/json; charset=utf-8"))
                .extract()
                .body()
                .as(CreateUserResponse.class);
        assertThat(response.getName(), equalTo(user.getName()));
        assertThat(response.getJob(), equalTo(user.getJob()));


    }
}
