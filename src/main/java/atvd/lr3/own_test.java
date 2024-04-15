package atvd.lr3;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.opentelemetry.semconv.SemanticAttributes.SystemCpuStateValues.USER;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
class MapObject{
    public int id;
    public String name;
    public MapObject(int id, String name){
        this.id = id;
        this.name = name;
    }
}

public class own_test {
    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private static final int ORDER_ID = 1;
    private static final int QUANTITY = 122;
    private static final int PET_ID = 2;
    private static final String PET_PHOTO_URL = "https://cdn.britannica.com/70/234870-050-D4D024BB/Orange-colored-cat-yawns-displaying-teeth.jpg";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }
    @Test
    public void testPlaceOrder() {
        Map<String, ?> body = Map.of(
        "id", ORDER_ID,
        "petId", PET_ID,
        "quantity", QUANTITY,
        "shipDate", "2024-03-12T10:00:00.000Z",
        "status", "placed",
        "complete", true);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(BASE_URL + "/store/order");

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(ORDER_ID))
                .body("quantity", equalTo(QUANTITY));
    }
    @Test(dependsOnMethods = "testPlaceOrder")
    public void testGetInfo() {
        Response response = given().pathParam("id", ORDER_ID).get(BASE_URL + "/store/order/{id}");
        System.out.printf("\n%s\n", response.jsonPath().get().toString());

        response.then().statusCode(HttpStatus.SC_OK).and()
                .body("id", equalTo(ORDER_ID))
                .body("petId", equalTo(PET_ID))
                .body("quantity", equalTo(QUANTITY))
                .body("complete", equalTo(true));
    }
    @Test(dependsOnMethods = "testGetInfo")
    public void testDeleteOrder() {
        Response response = RestAssured.given().pathParam("id", ORDER_ID).delete(BASE_URL + "/store/order/{id}");
        System.out.printf("\n%s\n", response.jsonPath().get().toString());
        response.then().statusCode(HttpStatus.SC_OK);
    }
    @Test
    public void verifyCreatePet() {

        Map<String, Object> body = Map.of(
                "id", 2,
                "name", "Alex",
                "photoUrls", new String[]{PET_PHOTO_URL},
                "status", "available",
                "category", new MapObject(1, "cat"),
                "tags", new MapObject[]{new MapObject(1, "nice"), new MapObject(2, "smart")}
        );

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .post(BASE_URL + "/pet");

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo("Alex"))
                .body("category.name", equalTo("cat"))
                .body("photoUrls[0]", equalTo(PET_PHOTO_URL));
    }
    @Test(dependsOnMethods = "verifyCreatePet")
    public void testGetPetByID() {
        Response response = given()
                .pathParam("petId", PET_ID)
                .get(BASE_URL + "/pet/{petId}");

        System.out.printf("\n%s\n", response.jsonPath().get().toString());

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(PET_ID));
    }
    @Test(dependsOnMethods = "verifyCreatePet")
    public void testUpdatePet() {
        String newName = "Ataman";
        String newStatus = "pending";

        Map<String, ?> body = Map.of(
        "id", PET_ID,
        "name", newName,
        "status", newStatus);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .put(BASE_URL + "/pet");

        System.out.printf("\n%s\n", response.jsonPath().get().toString());

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo(newName))
                .body("status", equalTo(newStatus));
    }
}
