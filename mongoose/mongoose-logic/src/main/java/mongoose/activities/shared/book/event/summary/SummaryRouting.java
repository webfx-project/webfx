package mongoose.activities.shared.book.event.summary;

import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class SummaryRouting {

    final static String PATH = "/book/event/:eventId/summary";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , SummaryViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

}
