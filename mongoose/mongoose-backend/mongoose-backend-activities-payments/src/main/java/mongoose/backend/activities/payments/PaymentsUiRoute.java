package mongoose.backend.activities.payments;

import mongoose.backend.activities.payments.routing.PaymentsRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;
import webfx.framework.shared.router.util.PathBuilder;

/**
 * @author Bruno Salmon
 */
public final class PaymentsUiRoute extends UiRouteImpl {

    public PaymentsUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.createRegex(PathBuilder.toRegexPath(PaymentsRouting.getPath())
                , false
                , PaymentsActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
