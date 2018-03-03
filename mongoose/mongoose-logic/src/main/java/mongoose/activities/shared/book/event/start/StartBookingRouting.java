package mongoose.activities.shared.book.event.start;

import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class StartBookingRouting {

    final static String PATH = "/book/event/:eventId/start";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , StartBookingViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

}
