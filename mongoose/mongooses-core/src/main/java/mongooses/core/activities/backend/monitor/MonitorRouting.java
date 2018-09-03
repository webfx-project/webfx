package mongooses.core.activities.backend.monitor;

import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class MonitorRouting {

    private final static String PATH = "/monitor";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , true
                , MonitorActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }

}
