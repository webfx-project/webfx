package mongoose.activities.shared.book.event.options;

import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class OptionsRouting {

    public static final String PATH = "/book/event/:eventId/options";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , OptionsViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
