package mongoose.activities.backend.book.event.options;

import mongoose.activities.shared.book.event.options.OptionsRooting;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;
import naga.framework.ui.router.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public class EditableOptionsRouting {

    public static UiRoute<?> uiRoute() {
        return new UiRouteImpl<>(OptionsRooting.PATH, false,
                false, EditableOptionsViewActivity::new, ViewDomainActivityContextFinal::new, null
        );
    }

}
