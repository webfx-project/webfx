package mongooses.core.activities.sharedends.book.payment;

import mongooses.core.activities.sharedends.book.cart.CartRouting;
import mongooses.core.activities.sharedends.generic.routing.MongooseRoutingUtil;
import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class PaymentRouting {

    private final static String PATH = CartRouting.getPath() + "/payment";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , PaymentActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }

    public static String getPaymentPath(Object cartUuidOrDocument) {
        return MongooseRoutingUtil.interpolateCartUuidInPath(CartRouting.getCartUuid(cartUuidOrDocument), getPath());
    }
}
