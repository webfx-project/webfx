package mongoose.backend.activities.events;

import mongoose.backend.activities.events.routing.EventsRouting;
import webfx.framework.client.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;
import webfx.framework.shared.router.util.PathBuilder;

/**
 * @author Bruno Salmon
 */
public final class EventsUiRoute extends UiRouteImpl {

    public EventsUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.createRegex(
                PathBuilder.toRegexPath(EventsRouting.getAnyPath())
                , false
                , EventsActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }
}
