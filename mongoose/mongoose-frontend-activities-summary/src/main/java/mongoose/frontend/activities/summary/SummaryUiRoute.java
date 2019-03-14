package mongoose.frontend.activities.summary;

import mongoose.frontend.activities.summary.routing.SummaryRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class SummaryUiRoute extends UiRouteImpl {

    public SummaryUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(SummaryRouting.getPath()
                , false
                , SummaryActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
