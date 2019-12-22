package mongoose.backend.activities.roomsgraphic;

import mongoose.backend.activities.roomsgraphic.routing.RoomsGraphicRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;
import webfx.framework.shared.router.util.PathBuilder;

/**
 * @author Bruno Salmon
 */
public final class RoomsGraphicUiRoute extends UiRouteImpl {

    public RoomsGraphicUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.createRegex(PathBuilder.toRegexPath(RoomsGraphicRouting.getPath())
                , false
                , RoomsGraphicActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
