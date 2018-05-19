package mongoose.activities.shared.book.cart.payment;

import mongoose.activities.shared.book.cart.CartRouting;
import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class PaymentRouting {

    private final static String PATH = CartRouting.getPath() + "/payment";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , PaymentViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }

    static String getPaymentPath(Object cartUuidOrDocument) {
        return MongooseRoutingUtil.interpolateCartUuidInPath(CartRouting.getCartUuid(cartUuidOrDocument), getPath());
    }
}
