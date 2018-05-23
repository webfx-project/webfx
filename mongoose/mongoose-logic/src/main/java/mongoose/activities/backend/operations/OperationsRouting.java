package mongoose.activities.backend.operations;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class OperationsRouting {

    private final static String PATH = "/operations";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , true
                , OperationsActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }
}
