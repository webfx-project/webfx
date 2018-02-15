package mongoose.activities.backend.letter.edit;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.router.UiRoute;
import naga.platform.client.url.history.History;

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

    public static void routeUsingLetter(Entity letter, History history) {
        MongooseRoutingUtil.routeUsingEntityId(letter, history, EditLetterRouting::routeUsingLetterId);
    }

    public static void routeUsingLetterId(Object letterId, History history) {
        history.push(MongooseRoutingUtil.interpolateLetterIdInPath(letterId, PATH));
    }
}
