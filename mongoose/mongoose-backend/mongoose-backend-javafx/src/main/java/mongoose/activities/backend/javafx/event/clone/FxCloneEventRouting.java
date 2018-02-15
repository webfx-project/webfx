package mongoose.activities.backend.javafx.event.clone;

import mongoose.activities.backend.event.clone.CloneEventRouting;
import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class FxCloneEventRouting {

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(CloneEventRouting.PATH
                , false
                , FxCloneEventPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

}
