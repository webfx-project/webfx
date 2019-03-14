package mongoose.frontend.activities.program;

import mongoose.frontend.activities.program.routing.ProgramRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class ProgramUiRoute extends UiRouteImpl {

    public ProgramUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(ProgramRouting.getPath()
                , false
                , ProgramActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
