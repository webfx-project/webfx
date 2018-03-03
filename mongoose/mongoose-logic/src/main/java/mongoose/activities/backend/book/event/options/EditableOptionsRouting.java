package mongoose.activities.backend.book.event.options;

import mongoose.activities.shared.book.event.options.OptionsRouting;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class EditableOptionsRouting {

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(OptionsRouting.PATH
                , false
                , EditableOptionsViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

}
