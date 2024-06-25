package Users;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserMethods {
    @Step("Send POST request to /api/auth/register")
    public static Response createUser(User user){
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    @Step("Send POST request to /api/auth/login")
    public static Response logInUser(User user){
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/login");
    }

    @Step("Get accessToken using GET request to /api/auth/login")
    public static String getAccessToken(User user){
        return logInUser(user).getBody()
                .path("accessToken").toString().substring(7);
    }

    @Step("Send PATCH request to /api/auth/user with auth")
    public static Response changeUserData(User user, String token){
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .body(user)
                .when()
                .patch("/api/auth/user");
    }

    @Step("Send PATCH request to /api/auth/user without auth")
    public static Response changeUserData(User user){
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .patch("/api/auth/user");
    }

    @Step("Send DELETE request to /api/auth/user")
    public static Response deleteUser(String token){
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .when()
                .delete("/api/auth/user");
    }
}
