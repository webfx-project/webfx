package mongoose.activities.backend.operations;

import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
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
                , ViewDomainActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }
}
