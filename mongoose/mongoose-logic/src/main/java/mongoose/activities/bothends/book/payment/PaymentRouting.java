package mongoose.activities.bothends.book.payment;

import mongoose.activities.bothends.book.cart.CartRouting;
import mongoose.activities.bothends.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public class PaymentRouting {

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
