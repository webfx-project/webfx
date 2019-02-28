package mongoose.backend.activities.options;

import mongoose.frontend.activities.options.routing.OptionsRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class EditableOptionsUiRoute extends UiRouteImpl<ViewDomainActivityContextFinal> {

    public EditableOptionsUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<ViewDomainActivityContextFinal> uiRoute() {
        return UiRoute.create(OptionsRouting.getPath()
                , false
                , EditableOptionsActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

}
