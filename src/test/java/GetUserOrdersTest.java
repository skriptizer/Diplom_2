import Orders.Order;
import Orders.OrderMethods;
import Users.User;
import Users.UserData;
import Users.UserMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.hamcrest.Matchers.*;

public class GetUserOrdersTest {
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Get user orders with authorization ok true")
    public void getUserOrdersWithAuthorizationOkTrue() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD,UserData.NAME);
        UserMethods.createUser(user);
        UserMethods.logInUser(user);
        token = UserMethods.getAccessToken(user);

        ArrayList<String> firstOrderIngredients = new ArrayList<>();
        int firstOrderBun = 0;
        int firstOrderFilling = 1;

        firstOrderIngredients.add(OrderMethods.getIngredientsId(firstOrderBun));
        firstOrderIngredients.add(OrderMethods.getIngredientsId(firstOrderFilling));

        Order order = new Order(firstOrderIngredients);
        OrderMethods.createOrder(order, token);

        ArrayList<String> secondOrderIngredients = new ArrayList<>();
        int secondOrderBun = 0;
        int secondOrderFilling = 2;

        secondOrderIngredients.add(OrderMethods.getIngredientsId(secondOrderBun));
        secondOrderIngredients.add(OrderMethods.getIngredientsId(secondOrderFilling));

        Order secondOrder = new Order(secondOrderIngredients);
        OrderMethods.createOrder(secondOrder, token);

        Response response = OrderMethods.getUserOrders(token);
        response.then().assertThat().statusCode(200).and()
                .body("success", is(true))
                .body("orders[0]", notNullValue())
                .body("orders[1]", notNullValue())
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
    }

    @Test
    @DisplayName("Get user orders without authorization Unauthorized")
    public void getUserOrdersWithoutAuthorizationUnauthorized() {
        User user = new User(UserData.EMAIL, UserData.PASSWORD,UserData.NAME);
        UserMethods.createUser(user);
        UserMethods.logInUser(user);
        token = UserMethods.getAccessToken(user);

        ArrayList<String> firstOrderIngredients = new ArrayList<>();
        int firstOrderBun = 0;
        int firstOrderFilling = 1;

        firstOrderIngredients.add(OrderMethods.getIngredientsId(firstOrderBun));
        firstOrderIngredients.add(OrderMethods.getIngredientsId(firstOrderFilling));

        Order order = new Order(firstOrderIngredients);
        OrderMethods.createOrder(order, token);

        ArrayList<String> secondOrderIngredients = new ArrayList<>();
        int secondOrderBun = 0;
        int secondOrderFilling = 2;

        secondOrderIngredients.add(OrderMethods.getIngredientsId(secondOrderBun));
        secondOrderIngredients.add(OrderMethods.getIngredientsId(secondOrderFilling));

        Order secondOrder = new Order(secondOrderIngredients);
        OrderMethods.createOrder(secondOrder, token);

        Response response = OrderMethods.getUserOrders();
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
