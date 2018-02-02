package mongoose.activities.backend.event.bookings;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class BookingsPresentationActivity extends DomainPresentationActivityImpl<BookingsPresentationModel> {

    public BookingsPresentationActivity() {
        super(BookingsPresentationViewActivity::new, BookingsPresentationLogicActivity::new);
    }
}
