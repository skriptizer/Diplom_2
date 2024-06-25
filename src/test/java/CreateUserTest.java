import Users.User;
import Users.UserMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import Users.UserData;
import static org.hamcrest.Matchers.*;

public class CreateUserTest {
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Create unique user ok true")
    public void createUniqueUserOkTrue() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD, UserData.NAME);
        Response response = UserMethods.createUser(user);
        token = UserMethods.getAccessToken(user);

        response.then().assertThat().statusCode(200).and()
                .body("success", is(true))
                .body("user.email", equalTo(UserData.EMAIL))
                .body("user.name", equalTo(UserData.NAME))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Create copy user forbidden")
    public void createCopyUserForbidden() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD, UserData.NAME);
        UserMethods.createUser(user);
        token = UserMethods.getAccessToken(user);

        Response response = UserMethods.createUser(user);

        response.then().assertThat().statusCode(403).and()
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Create user without email forbidden")
    public void createUserWithoutEmailForbidden() {
        User user = new User(null, UserData.PASSWORD, UserData.NAME);
        Response response = UserMethods.createUser(user);

        response.then().assertThat().statusCode(403).and()
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user without password forbidden")
    public void createUserWithoutPasswordForbidden() {
        User user = new User(UserData.EMAIL, null, UserData.NAME);
        Response response = UserMethods.createUser(user);

        response.then().assertThat().statusCode(403).and()
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Create user without name forbidden")
    public void createUserWithoutNameForbidden() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD, null);
        Response response = UserMethods.createUser(user);

        response.then().assertThat().statusCode(403).and()
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void DeleteUser() {
        if (token != null) {
            UserMethods.deleteUser(token);
        }
    }
}
