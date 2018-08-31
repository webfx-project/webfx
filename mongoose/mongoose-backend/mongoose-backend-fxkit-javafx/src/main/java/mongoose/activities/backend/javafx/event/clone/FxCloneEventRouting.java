package mongoose.activities.backend.javafx.event.clone;

import mongoose.activities.backend.cloneevent.CloneEventRouting;
import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class FxCloneEventRouting {

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(CloneEventRouting.getPath()
                , false
                , FxCloneEventPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

}
