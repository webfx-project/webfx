package mongoose.activities.backend.book.options;

import mongoose.activities.bothends.book.options.OptionsRouting;
import webfx.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class EditableOptionsRouting {

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(OptionsRouting.getPath()
                , false
                , EditableOptionsActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

}
