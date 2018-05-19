package mongoose.activities.backend.letter;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class LetterRouting {

    private final static String PATH = "/letter/:letterId";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , LetterViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    static String getEditLetterPath(Object letterId) {
        return MongooseRoutingUtil.interpolateLetterIdInPath(letterId, PATH);
    }
}
