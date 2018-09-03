package mongooses.core.activities.sharedends.book.cart;

import mongooses.core.activities.sharedends.generic.routing.MongooseRoutingUtil;
import mongooses.core.entities.Document;
import webfx.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class CartRouting {

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

    public static String getCartPath(Object cartUuidOrDocument) {
        return MongooseRoutingUtil.interpolateCartUuidInPath(getCartUuid(cartUuidOrDocument), getPath());
    }

    public static Object getCartUuid(Object cartUuidOrDocument) {
        if (cartUuidOrDocument instanceof Document)
            return ((Document) cartUuidOrDocument).evaluate("cart.uuid");
        return cartUuidOrDocument;
    }
}
