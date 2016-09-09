package mongoose.client.backend;

import mongoose.activities.monitor.MonitorActivity;
import mongoose.activities.tester.TesterActivity;
import mongoose.activities.tester.testset.TestSetActivity;
import mongoose.client.shared.MongooseApplication;
import naga.framework.ui.router.UiRouter;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplication extends MongooseApplication {

    @Override
    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return super.setupContainedRouter(containedRouter)
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
