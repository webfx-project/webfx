package mongoose.activities.frontend.application;

import mongoose.activities.frontend.container.FrontendContainerActivity;
import mongoose.activities.frontend.event.fees.FeesActivity;
import mongoose.activities.shared.application.MongooseApplication;
import naga.commons.util.function.Factory;
import naga.framework.activity.client.UiDomainActivityContext;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.Activity;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendApplication extends MongooseApplication {

    @Override
    protected Factory<Activity<UiDomainActivityContext>> getContainerActivityFactory() {
        return FrontendContainerActivity::new;
    }

    @Override
    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return super.setupContainedRouter(containedRouter)
                .route("/event/:eventId/bookings", FeesActivity::new);
    }

    @Override
    public void onStart() {
        context.getUiRouter().setDefaultInitialHistoryPath("/cart/a58faba5-5b0b-4573-b547-361e10c788dc");
        super.onStart();
    }

    public static void main(String[] args) {
        launchApplication(new MongooseFrontendApplication(), args);
    }


}
