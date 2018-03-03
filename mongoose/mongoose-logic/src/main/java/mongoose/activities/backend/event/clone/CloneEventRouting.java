package mongoose.activities.backend.event.clone;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class CloneEventRouting {

    public static final String PATH = "/event/:eventId/clone";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , CloneEventPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

}
