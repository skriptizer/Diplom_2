import Orders.Order;
import Orders.OrderMethods;
import Users.User;
import Users.UserData;
import Users.UserMethods;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Create order with authorization ok true")
    public void createOrderWithAuthorizationOkTrue() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD,UserData.NAME);
        UserMethods.createUser(user);
        UserMethods.logInUser(user);
        token = UserMethods.getAccessToken(user);

        ArrayList<String> ingredients = new ArrayList<>();
        int ingredientNumber = 0;
        ingredients.add(OrderMethods.getIngredientsId(ingredientNumber));
        Order order = new Order(ingredients);

        Response response = OrderMethods.createOrder(order, token);
        response.then().assertThat().statusCode(200).and()
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Create order without authorization Unauthorized")
    @Description("В этом тесте был обнаружен баг. По документации нельзя создать заказ будучи неавторизованным.")
    public void createOrderWithoutAuthorizationUnauthorized() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD,UserData.NAME);
        UserMethods.createUser(user);
        UserMethods.logInUser(user);
        token = UserMethods.getAccessToken(user);

        ArrayList<String> ingredients = new ArrayList<>();
        int ingredientNumber = 0;
        ingredients.add(OrderMethods.getIngredientsId(ingredientNumber));
        Order order = new Order(ingredients);

        Response response = OrderMethods.createOrder(order);
        response.then().assertThat().statusCode(401).and()
                .body("success", is(false));
    }

    @Test
    @DisplayName("Create order with authorization and without ingredient bad request")
    public void createOrderWithAuthorizationAndWithoutIngredientBadRequest() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD,UserData.NAME);
        UserMethods.createUser(user);
        UserMethods.logInUser(user);
        token = UserMethods.getAccessToken(user);

        ArrayList<String> ingredients = new ArrayList<>();
        Order order = new Order(ingredients);

        Response response = OrderMethods.createOrder(order, token);
        response.then().assertThat().statusCode(400).and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Create order with authorization and with wrong ingredient internal server error")
    public void createOrderWithAuthorizationAndWithWrongIngredientInternalServerError() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD,UserData.NAME);
        UserMethods.createUser(user);
        UserMethods.logInUser(user);
        token = UserMethods.getAccessToken(user);

        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("default123");
        Order order = new Order(ingredients);

        Response response = OrderMethods.createOrder(order, token);
        response.then().assertThat().statusCode(500);
    }

    @After
    public void deleteUser() {
        if (token != null) {
            UserMethods.deleteUser(token);
        }
    }
}
