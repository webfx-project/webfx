package mongoose.activities.backend.tester;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class TesterRouting {

    private final static String PATH = "/tester";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , TesterActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }
}
