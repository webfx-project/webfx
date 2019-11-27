package mongoose.backend.application;

import mongoose.backend.controls.bookingdetailspanel.BookingDetailsPanel;
import mongoose.backend.controls.masterslave.MasterSlaveView;
import mongoose.client.application.MongooseClientActivity;

/**
 * @author Bruno Salmon
 */
final class MongooseBackendActivity extends MongooseClientActivity {

    private static final String DEFAULT_START_PATH = "/organizations";

    MongooseBackendActivity() {
        super(DEFAULT_START_PATH);
        MasterSlaveView.registerSlaveViewBuilder(BookingDetailsPanel::createAndBindIfApplicable);
    }

}
