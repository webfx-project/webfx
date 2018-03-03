package mongoose.activities.backend.letter.edit;

import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class EditLetterRouting {

    final static String PATH = "/letter/:letterId/edit";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , EditLetterViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

}
