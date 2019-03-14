package mongoose.frontend.activities.options;

import mongoose.frontend.activities.options.routing.OptionsRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class OptionsUiRoute extends UiRouteImpl {

    public OptionsUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(OptionsRouting.getPath()
                , false
                , OptionsActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
