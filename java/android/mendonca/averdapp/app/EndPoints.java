package android.mendonca.averdapp.app;

/**
 * Created by Lincoln on 06/01/16.
 */

public class EndPoints {

    // localhost url
    public static final String BASE_URL = "http://192.168.225.56/amart/Android";
   // public static final String BASE_URL = "https://wholesaleprice.info/amart/Android";
    public static final String PRODUCT = BASE_URL + "/json_item.php";
    public static final String CART = BASE_URL + "/json_addcart.php";
    public static final String CART_PRODUCT = BASE_URL + "/json_cart_product.php";
   public static final String HOME = BASE_URL + "/json_category.php";
   public static final String LOGIN = BASE_URL + "/json_login.php";
    public static final String PRODUCTS = BASE_URL + "/json_grid.php";
    public static final String SIGN_UP = BASE_URL + "/json_signup.php";
    public static final String PROFILE = BASE_URL + "/json_profile.php";
    public static final String ORDER = BASE_URL + "/json_order.php";
    public static final String ORDER_PRODUCT = BASE_URL + "/json_order_product.php";
}
