package mongoose.activities.backend.letter.edit;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class EditLetterRouting {

    private final static String PATH = "/letter/:letterId/edit";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , EditLetterViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    static String getEditLetterPath(Object letterId) {
        return MongooseRoutingUtil.interpolateLetterIdInPath(letterId, PATH);
    }
}
