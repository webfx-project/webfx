package mongoose.activities.shared.book.event.person;

import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class PersonRouting {

    final static String PATH = "/book/event/:eventId/person";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , PersonViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
