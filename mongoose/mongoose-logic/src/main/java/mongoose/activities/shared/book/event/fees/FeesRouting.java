package mongoose.activities.shared.book.event.fees;

import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class FeesRouting {

    final static String PATH = "/book/event/:eventId/fees";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , FeesViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
