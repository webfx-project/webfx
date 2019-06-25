package mongoose.backend.activities.users;

import mongoose.backend.activities.users.routing.UsersRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;
import webfx.framework.shared.router.util.PathBuilder;

/**
 * @author Bruno Salmon
 */
public final class UsersUiRoute extends UiRouteImpl {

    public UsersUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.createRegex(PathBuilder.toRegexPath(UsersRouting.getPath())
                , false
                , UsersActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
