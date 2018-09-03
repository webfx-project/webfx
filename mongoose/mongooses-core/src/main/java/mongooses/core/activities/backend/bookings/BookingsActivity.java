package mongooses.core.activities.backend.bookings;

import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
final class BookingsActivity extends DomainPresentationActivityImpl<BookingsPresentationModel> {

    BookingsActivity() {
        super(BookingsPresentationViewActivity::new, BookingsPresentationLogicActivity::new);
    }
}
