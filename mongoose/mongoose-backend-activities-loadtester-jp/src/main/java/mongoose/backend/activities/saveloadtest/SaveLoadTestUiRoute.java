package mongoose.backend.activities.saveloadtest;

import mongoose.backend.activities.saveloadtest.routing.SaveLoadTestRooting;
import webfx.framework.client.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class SaveLoadTestUiRoute extends UiRouteImpl {

    public SaveLoadTestUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(SaveLoadTestRooting.getPath()
                , false
                , SaveLoadTestActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }
}
