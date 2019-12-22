package mongoose.frontend.activities.payment;

import mongoose.frontend.activities.payment.routing.PaymentRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class PaymentUiRoute extends UiRouteImpl {

    public PaymentUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PaymentRouting.getPath()
                , false
                , PaymentActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
