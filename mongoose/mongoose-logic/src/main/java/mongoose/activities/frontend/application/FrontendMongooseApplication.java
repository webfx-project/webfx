package mongoose.activities.frontend.application;

import mongoose.activities.frontend.container.FrontendContainerViewActivity;
import mongoose.activities.shared.application.SharedMongooseApplication;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.platform.activity.Activity;
import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class FrontendMongooseApplication extends SharedMongooseApplication {

    public FrontendMongooseApplication() {
        super("/book/event/125/fees");
    }

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return FrontendContainerViewActivity::new;
    }

    public static void main(String[] args) {
        launchApplication(new FrontendMongooseApplication(), args);
    }

}
