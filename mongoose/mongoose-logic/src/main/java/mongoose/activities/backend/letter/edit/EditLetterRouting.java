package mongoose.activities.backend.letter.edit;

import mongoose.activities.backend.events.EventsRouting;
import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.router.UiRoute;
import naga.framework.ui.router.UiRouteImpl;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class EditLetterRouting {

    private final static String PATH = "/letter/:letterId/edit";

    public static UiRoute<?> uiRoute() {
        return new UiRouteImpl<>(PATH,false,
                false, EditLetterViewActivity::new, ViewDomainActivityContextFinal::new, null
        );
    }

    public static void routeUsingLetter(Entity letter, History history) {
        MongooseRoutingUtil.routeUsingEntityPrimaryKey(letter, history, EditLetterRouting::routeUsingLetterId);
    }

    public static void routeUsingLetterId(Object letterId, History history) {
        history.push(MongooseRoutingUtil.interpolateLetterIdInPath(letterId, PATH));
    }
}
