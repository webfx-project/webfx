package mongoose.client.frontend;

import mongoose.activities.container.FrontendContainerActivity;
import mongoose.client.shared.MongooseApplication;
import naga.commons.util.function.Factory;
import naga.framework.activity.client.UiDomainActivityContext;
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
    public void onStart() {
        context.getUiRouter().setDefaultInitialHistoryPath("/cart/a58faba5-5b0b-4573-b547-361e10c788dc");
        super.onStart();
    }

    public static void main(String[] args) {
        launchApplication(new MongooseFrontendApplication(), args);
    }


}
