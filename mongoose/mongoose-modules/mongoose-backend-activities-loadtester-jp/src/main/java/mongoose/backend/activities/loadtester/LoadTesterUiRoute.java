package mongoose.backend.activities.loadtester;

import webfx.framework.client.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class LoadTesterUiRoute extends UiRouteImpl {

    public LoadTesterUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(LoadTesterRouting.getPath()
                , false
                , LoadTesterActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }
}
