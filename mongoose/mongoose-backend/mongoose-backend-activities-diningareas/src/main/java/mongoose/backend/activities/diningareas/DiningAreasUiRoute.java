package mongoose.backend.activities.diningareas;

import mongoose.backend.activities.diningareas.routing.DiningAreasRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;
import webfx.framework.shared.router.util.PathBuilder;

/**
 * @author Bruno Salmon
 */
public final class DiningAreasUiRoute extends UiRouteImpl {

    public DiningAreasUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.createRegex(PathBuilder.toRegexPath(DiningAreasRouting.getPath())
                , false
                , DiningAreasActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
