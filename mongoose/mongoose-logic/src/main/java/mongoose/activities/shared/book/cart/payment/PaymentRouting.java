package mongoose.activities.shared.book.cart.payment;

import mongoose.activities.shared.book.cart.CartRouting;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class PaymentRouting {

    final static String PATH = CartRouting.PATH + "/payment";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , PaymentViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

}
