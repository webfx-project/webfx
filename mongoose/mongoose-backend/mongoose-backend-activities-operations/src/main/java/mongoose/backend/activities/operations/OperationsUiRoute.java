package mongoose.backend.activities.operations;

import mongoose.backend.activities.operations.routing.OperationsRouting;
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
                , false
                , OperationsActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }
}
