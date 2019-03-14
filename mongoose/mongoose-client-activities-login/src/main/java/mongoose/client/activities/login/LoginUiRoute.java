package mongoose.client.activities.login;

import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.ProvidedLoginUiRoute;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class LoginUiRoute extends UiRouteImpl implements ProvidedLoginUiRoute {

    public LoginUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(LoginRouting.getPath()
                , false
                , LoginViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
