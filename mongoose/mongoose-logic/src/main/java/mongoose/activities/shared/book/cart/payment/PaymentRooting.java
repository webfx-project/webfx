package mongoose.activities.shared.book.cart.payment;

import mongoose.activities.shared.book.cart.CartRooting;
import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.router.UiRoute;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class PaymentRooting {

    private final static String PATH = CartRooting.PATH + "/payment";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , PaymentViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static void routeUsingDocument(Entity document, History history) {
        routeUsingCartUuid(CartRooting.getCartUuidFromDocument(document), history);
    }

    public static void routeUsingCartUuid(Object cartUuid, History history) {
        history.push(MongooseRoutingUtil.interpolateCartUuidInPath(cartUuid, PATH));
    }

}
