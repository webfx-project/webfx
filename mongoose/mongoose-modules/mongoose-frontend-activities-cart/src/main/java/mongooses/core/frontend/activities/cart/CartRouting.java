package mongooses.core.frontend.activities.cart;

import mongooses.core.sharedends.activities.generic.routing.MongooseRoutingUtil;
import mongooses.core.shared.entities.Document;
import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;
import webfx.framework.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class CartRouting {

    private final static String PATH = "/book/cart/:cartUuid";

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

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , CartActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static final class ProvidedUiRoute extends UiRouteImpl {
        public ProvidedUiRoute() {
            super(uiRoute());
        }
    }
}
