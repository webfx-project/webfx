package mongoose.backend.activities.statistics;

import mongoose.backend.activities.statistics.routing.StatisticsRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class StatisticsUiRoute extends UiRouteImpl {

    public StatisticsUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(StatisticsRouting.getPath()
                , false
                , StatisticsActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
