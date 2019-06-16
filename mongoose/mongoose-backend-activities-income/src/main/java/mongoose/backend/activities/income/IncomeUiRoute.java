package mongoose.backend.activities.income;

import mongoose.backend.activities.income.routing.IncomeRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class IncomeUiRoute extends UiRouteImpl {

    public IncomeUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(IncomeRouting.getPath()
                , true
                , IncomeActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
