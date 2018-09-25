package mongoose.backend.javafx.activities.event.clone;

import mongooses.core.backend.activities.cloneevent.CloneEventRouting;
import webfx.framework.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
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
