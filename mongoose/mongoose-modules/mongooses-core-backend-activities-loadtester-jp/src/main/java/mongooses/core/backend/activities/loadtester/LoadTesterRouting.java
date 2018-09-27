package mongooses.core.backend.activities.loadtester;

import webfx.framework.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;
import webfx.framework.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class LoadTesterRouting {

    private final static String PATH = "/load-tester";

    public static String getPath() {
        return PATH;
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , LoadTesterActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static final class ProvidedUiRoute extends UiRouteImpl {
        public ProvidedUiRoute() {
            super(uiRoute());
        }
    }

}
