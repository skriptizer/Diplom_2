import Users.User;
import Users.UserData;
import Users.UserMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class LoginUserTest {
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Log in real user ok true")
    public void logInRealUserOkTrue() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD,UserData.NAME);
        UserMethods.createUser(user);

        Response response = UserMethods.logInUser(user);
        token = response.getBody().path("accessToken").toString().substring(7);

        response.then().assertThat().statusCode(200).and()
                .body("success", is(true))
                .body("user.email", equalTo(UserData.EMAIL))
                .body("user.name", equalTo(UserData.NAME))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Log in with incorrect email unauthorized")
    public void logInWithIncorrectEmailUnauthorized() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD,UserData.NAME);
        UserMethods.createUser(user);

        Response response = UserMethods.logInUser(new User(UserData.DIFFERENT_EMAIL, UserData.PASSWORD));
        token = UserMethods.getAccessToken(user);

        response.then().assertThat().statusCode(401)
                .and().body("success", is(false))
                .and().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Log in with incorrect password unauthorized")
    public void logInWithIncorrectPasswordUnauthorized() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD,UserData.NAME);
        UserMethods.createUser(user);

        Response response = UserMethods.logInUser(new User(UserData.EMAIL, UserData.DIFFERENT_PASSWORD));
        token = UserMethods.getAccessToken(user);

        response.then().assertThat().statusCode(401)
                .and().body("success", is(false))
                .and().body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void deleteUser() {
        if (token != null) {
            UserMethods.deleteUser(token);
        }
    }
}
