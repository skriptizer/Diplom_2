import Users.User;
import Users.UserData;
import Users.UserMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ChangesUserDataTest {
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Change user data with authorization ok true")
    public void changeUserDataWithAuthorizationOk() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD, UserData.NAME);
        UserMethods.createUser(user);
        token = UserMethods.logInUser(user).getBody()
                .path("accessToken").toString().substring(7);

        user.setEmail(UserData.DIFFERENT_EMAIL);
        user.setName(UserData.DIFFERENT_NAME);

        Response response = UserMethods.changeUserData(user, token);

        response.then().assertThat().statusCode(200).and()
                .body("success", is(true))
                .body("user.email", equalTo(UserData.DIFFERENT_EMAIL))
                .body("user.name", equalTo(UserData.DIFFERENT_NAME));
    }

    @Test
    @DisplayName("Change user email with authorization ok true")
    public void changeUserEmailWithAuthorizationOk() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD, UserData.NAME);
        UserMethods.createUser(user);
        token = UserMethods.logInUser(user).getBody()
                .path("accessToken").toString().substring(7);

        user.setEmail(UserData.DIFFERENT_EMAIL);

        Response response = UserMethods.changeUserData(user, token);

        response.then().assertThat().statusCode(200).and()
                .body("success", is(true))
                .body("user.email", equalTo(UserData.DIFFERENT_EMAIL));
    }

    @Test
    @DisplayName("Change user name with authorization ok true")
    public void changeUserNameWithAuthorizationOk() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD, UserData.NAME);
        UserMethods.createUser(user);
        token = UserMethods.logInUser(user).getBody()
                .path("accessToken").toString().substring(7);

        user.setEmail(UserData.DIFFERENT_NAME);

        Response response = UserMethods.changeUserData(user, token);

        response.then().assertThat().statusCode(200).and()
                .body("success", is(true))
                .body("user.email", equalTo(UserData.DIFFERENT_NAME));
    }

    @Test
    @DisplayName("Change user data without authorization Unauthorized")
    public void changeUserDataWithoutAuthorizationUnauthorized() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD, UserData.NAME);
        UserMethods.createUser(user);
        token = UserMethods.getAccessToken(user);

        user.setEmail(UserData.DIFFERENT_EMAIL);
        user.setEmail(UserData.DIFFERENT_NAME);

        Response response = UserMethods.changeUserData(user);

        response.then().assertThat().statusCode(401).and()
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Change user email without authorization Unauthorized")
    public void changeUserEmailWithoutAuthorizationUnauthorized() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD, UserData.NAME);
        UserMethods.createUser(user);
        token = UserMethods.getAccessToken(user);

        user.setEmail(UserData.DIFFERENT_EMAIL);

        Response response = UserMethods.changeUserData(user);

        response.then().assertThat().statusCode(401).and()
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Change user name without authorization Unauthorized")
    public void changeUserNameWithoutAuthorizationUnauthorized() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD, UserData.NAME);
        UserMethods.createUser(user);
        token = UserMethods.getAccessToken(user);

        user.setEmail(UserData.DIFFERENT_NAME);

        Response response = UserMethods.changeUserData(user);

        response.then().assertThat().statusCode(401).and()
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteUser() {
        if (token != null) {
            UserMethods.deleteUser(token);
        }
    }
}
