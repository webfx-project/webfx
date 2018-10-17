package mongoose.frontend.activities.person;

import mongoose.frontend.activities.person.routing.PersonRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class PersonUiRoute extends UiRouteImpl {

    public PersonUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PersonRouting.getPath()
                , false
                , PersonActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
