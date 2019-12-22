package mongoose.backend.activities.bookings;

import mongoose.backend.activities.bookings.routing.BookingsRouting;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;
import webfx.framework.shared.router.util.PathBuilder;

/**
 * @author Bruno Salmon
 */
public final class BookingsUiRoute extends UiRouteImpl {

    public BookingsUiRoute() {
        super(uiRoute());
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.createRegex(PathBuilder.toRegexPath(BookingsRouting.getAnyPath())
                , false
                , BookingsActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }
}
