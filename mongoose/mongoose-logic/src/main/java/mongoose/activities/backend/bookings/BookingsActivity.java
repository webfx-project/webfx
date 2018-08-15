package mongoose.activities.backend.bookings;

import naga.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class BookingsActivity extends DomainPresentationActivityImpl<BookingsPresentationModel> {

    BookingsActivity() {
        super(BookingsPresentationViewActivity::new, BookingsPresentationLogicActivity::new);
    }
}
