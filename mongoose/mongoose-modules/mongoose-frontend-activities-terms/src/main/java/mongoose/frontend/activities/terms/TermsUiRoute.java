package mongoose.frontend.activities.terms;

import mongoose.frontend.activities.terms.routing.TermsRouting;
import webfx.framework.client.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class TermsUiRoute extends UiRouteImpl {

    public TermsUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(TermsRouting.getPath()
                , false
                , TermsActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }
}
