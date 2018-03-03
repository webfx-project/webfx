package mongoose.activities.shared.book.event.program;

import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class ProgramRouting {

    final static String PATH = "/book/event/:eventId/program";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , ProgramViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
