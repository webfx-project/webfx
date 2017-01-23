package mongoose.activities.frontend.application;

import mongoose.activities.frontend.container.FrontendContainerViewActivity;
import mongoose.activities.shared.application.SharedMongooseApplication;
import naga.commons.util.function.Factory;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRouter;
import naga.platform.activity.Activity;

/**
 * @author Bruno Salmon
 */
public class FrontendMongooseApplication extends SharedMongooseApplication {

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return FrontendContainerViewActivity::new;
    }

    @Override
    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return super.setupContainedRouter(containedRouter);
    }

    @Override
    public void onStart() {
        context.getUiRouter().setDefaultInitialHistoryPath("/event/125/fees");
        super.onStart();
    }

    public static void main(String[] args) {
        launchApplication(new FrontendMongooseApplication(), args);
    }

}
