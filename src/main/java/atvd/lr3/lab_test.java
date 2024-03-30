package atvd.lr3;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.testng.annotations.*;

public class lab_test {
    private final String BASE_URL = "https://petstore.swagger.io/v2";
    private final String USER = "/user";
    private final String USER_USERNAME = USER + "/{username}";
    private final String USER_LOGIN = USER + "/login";
    private final String USER_LOGOUT = USER + "/logout";
    private String username = "AtamanchukOA";
    private String firstName = "Oleksandra";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }
}
