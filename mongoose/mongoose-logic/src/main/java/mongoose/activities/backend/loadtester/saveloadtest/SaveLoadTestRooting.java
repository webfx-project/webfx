package mongoose.activities.backend.loadtester.saveloadtest;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.ui.uirouter.UiRoute;
import naga.platform.client.url.history.History;

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
