package mongoose.client.backend;

import mongoose.activities.container.BackendContainerActivity;
import mongoose.activities.event.bookings.BookingsActivity;
import mongoose.activities.event.letters.LettersActivity;
import mongoose.activities.monitor.MonitorActivity;
import mongoose.activities.tester.TesterActivity;
import mongoose.activities.tester.testset.TestSetActivity;
import mongoose.client.shared.MongooseApplication;
import naga.commons.util.function.Factory;
import naga.framework.activity.client.UiDomainActivityContext;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.Activity;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplication extends MongooseApplication {

    @Override
    protected Factory<Activity<UiDomainActivityContext>> getContainerActivityFactory() {
        return BackendContainerActivity::new;
    }

    @Override
    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return super.setupContainedRouter(containedRouter)
                .route("/event/:eventId/bookings", BookingsActivity::new)
                .route("/event/:eventId/letters", LettersActivity::new)
                .route("/monitor", MonitorActivity::new)
                .route("/tester", TesterActivity::new)
                .route("/testSet", TestSetActivity::new);
    }

    @Override
    public void onStart() {
        context.getUiRouter().setDefaultInitialHistoryPath("/organizations");
        super.onStart();
    }

    public static void main(String[] args) {
        launchApplication(new MongooseBackendApplication(), args);
    }

}
