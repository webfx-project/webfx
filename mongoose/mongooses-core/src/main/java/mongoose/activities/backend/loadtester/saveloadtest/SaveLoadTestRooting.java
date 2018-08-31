package mongoose.activities.backend.loadtester.saveloadtest;

import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;
import webfx.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class SaveLoadTestRooting {

    private final static String PATH = "/save-load-test";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , SaveLoadTestActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static void route(History history) {
        history.push(PATH);
    }

}
