package mongoose.frontend.activities.startbooking;

import mongoose.frontend.activities.startbooking.routing.StartBookingRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class StartBookingUiRoute extends UiRouteImpl {

    public StartBookingUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(StartBookingRouting.getPath()
                , false
                , StartBookingActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
