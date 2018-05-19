package mongoose.activities.shared.book.cart;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import mongoose.entities.Document;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class CartRouting {

    private final static String PATH = "/book/cart/:cartUuid";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , CartActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }

    static String getCartPath(Object cartUuidOrDocument) {
        return MongooseRoutingUtil.interpolateCartUuidInPath(getCartUuid(cartUuidOrDocument), getPath());
    }

    public static Object getCartUuid(Object cartUuidOrDocument) {
        if (cartUuidOrDocument instanceof Document)
            return ((Document) cartUuidOrDocument).evaluate("cart.uuid");
        return cartUuidOrDocument;
    }
}
