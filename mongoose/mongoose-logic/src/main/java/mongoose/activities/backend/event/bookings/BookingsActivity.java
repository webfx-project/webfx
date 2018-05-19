package mongoose.activities.backend.event.bookings;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class BookingsActivity extends DomainPresentationActivityImpl<BookingsPresentationModel> {

    BookingsActivity() {
        super(BookingsPresentationViewActivity::new, BookingsPresentationLogicActivity::new);
    }
}
