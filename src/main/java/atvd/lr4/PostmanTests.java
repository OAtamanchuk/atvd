package atvd.lr4;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class PostmanTests {
    private final String BASE_URL = "https://9ac6e776-e3eb-4da5-9a06-9860ea3bae3f.mock.pstmn.io";
    private final String GET_SUCCESS = BASE_URL + "/AtamanchukOA/success";
    private final String GET_UNSUCCESS = BASE_URL + "/AtamanchukOA/unsuccess";
    private final String CREATE_PERMISSION = BASE_URL + "/createSomething?permission=yes";
    private final String CREATE_NOPERMISSION = BASE_URL + "/createSomething";
    private final String UPDATE_ME = BASE_URL + "/updateMe";
    private final String DELETE = BASE_URL + "/deleteWorld";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void testGet200() {
        Response response = RestAssured.given()
                .get(GET_SUCCESS);
        response.then().statusCode(HttpStatus.SC_OK);
        System.out.printf("\n%s\n", response.jsonPath().get().toString());
    }

    @Test
    public void testGet403() {
        Response response = given()
                .get(GET_UNSUCCESS);
        response.then().statusCode(HttpStatus.SC_FORBIDDEN);
        System.out.printf("\n%s\n", response.jsonPath().get().toString());
    }

    @Test
    public void testPost200() {
        Response response = given()
                .post(CREATE_PERMISSION);
        response.then().statusCode(HttpStatus.SC_OK);
        System.out.printf("\n%s\n", response.jsonPath().get().toString());
    }

    @Test
    public void testPost400() {
        Response response = given()
                .post(CREATE_NOPERMISSION);
        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);
        System.out.printf("\n%s\n", response.jsonPath().get().toString());
    }

    @Test
    public void testPut500() {
        Map<String, ?> body = Map.of(
                "name", "Oleksandra",
                "surname", "Atamanchuk"
        );
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .put(UPDATE_ME);
        response.then().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test(priority = 1)
    public void testDelete() {
        Response response = given()
                .delete(DELETE);
        response.then().statusCode(HttpStatus.SC_GONE);
        System.out.printf("\n%s\n", response.jsonPath().get().toString());
    }
}
