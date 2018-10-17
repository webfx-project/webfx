package mongoose.backend.activities.letter;

import mongoose.backend.activities.letter.routing.LetterRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class LetterUiRoute extends UiRouteImpl {

    public LetterUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(LetterRouting.getPath()
                , false
                , LetterActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
