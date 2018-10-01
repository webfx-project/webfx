package mongoose.backend.activities.saveloadtest;

import webfx.framework.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;
import webfx.framework.ui.uirouter.impl.UiRouteImpl;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public class SaveLoadTestRooting {

    private final static String PATH = "/save-load-test";

    public static void route(BrowsingHistory browsingHistory) {
        browsingHistory.push(PATH);
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , SaveLoadTestActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static final class ProvidedUiRoute extends UiRouteImpl {
        public ProvidedUiRoute() {
            super(uiRoute());
        }
    }
}
