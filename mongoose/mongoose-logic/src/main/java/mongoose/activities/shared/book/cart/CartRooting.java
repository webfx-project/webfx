package mongoose.activities.shared.book.cart;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.router.UiRoute;
import naga.framework.ui.router.UiRouteImpl;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class CartRooting {

    public final static String PATH = "/book/cart/:cartUuid";

    public static UiRoute<?> uiRoute() {
        return new UiRouteImpl<>(PATH, false,
                false, CartViewActivity::new, ViewDomainActivityContextFinal::new, null
        );
    }

    public static void routeUsingDocument(Entity document, History history) {
        routeUsingCartUuid(getCartUuidFromDocument(document), history);
    }

    public static Object getCartUuidFromDocument(Entity document) {
        return document == null ? null : document.evaluate("cart.uuid");
    }

    public static void routeUsingCartUuid(Object cartUuid, History history) {
        if (cartUuid != null)
            history.push(MongooseRoutingUtil.interpolateCartUuidInPath(cartUuid, PATH));
    }
}
