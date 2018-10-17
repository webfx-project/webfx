package mongoose.backend.activities.authorizations;

import mongoose.backend.activities.authorizations.routing.AuthorizationsRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class AuthorizationsUiRoute extends UiRouteImpl {

    public AuthorizationsUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(AuthorizationsRouting.getPath()
                , true
                , AuthorizationsViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
