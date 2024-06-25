package Orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderMethods {

    @Step("Send GET request to /api/ingredients")
    public static Response listOfIngredients() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/ingredients");
    }

    @Step("Send GET request to /api/orders with auth")
    public static Response getUserOrders(String token) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .when()
                .get("/api/orders");
    }

    @Step("Send GET request to /api/orders without auth")
    public static Response getUserOrders() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/orders");
    }

    @Step("Send POST request to /api/orders with auth")
    public static Response createOrder(Order order, String token) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .when()
                .body(order)
                .post("/api/orders");
    }

    @Step("Send POST request to /api/orders without auth")
    public static Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .body(order)
                .post("/api/orders");
    }

    @Step("Get ingredients id using GET request to /api/ingredients")
    public static String getIngredientsId(int id) {
        return listOfIngredients().getBody().path(String.format("data[%d]._id", id));
    }
}
