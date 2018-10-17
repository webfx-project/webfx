package mongoose.frontend.activities.fees;

import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class FeesUiRoute extends UiRouteImpl {

    public FeesUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(FeesRouting.getPath()
                , false
                , FeesActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
