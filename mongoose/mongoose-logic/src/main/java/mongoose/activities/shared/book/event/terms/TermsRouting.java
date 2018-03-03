package mongoose.activities.shared.book.event.terms;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class TermsRouting {

    final static String PATH = "/book/event/:eventId/terms";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , TermsPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

}
