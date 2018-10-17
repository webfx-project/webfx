package mongoose.backend.activities.operations;

import webfx.framework.client.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class OperationsUiRoute extends UiRouteImpl {

    public OperationsUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(OperationsRouting.getPath()
                , true
                , OperationsActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }
}
