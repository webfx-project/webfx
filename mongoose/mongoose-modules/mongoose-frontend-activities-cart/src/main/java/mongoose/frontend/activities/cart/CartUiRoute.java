package mongoose.frontend.activities.cart;

import mongoose.frontend.activities.cart.routing.CartRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class CartUiRoute extends UiRouteImpl {

    public CartUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(CartRouting.getPath()
                , false
                , CartActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
