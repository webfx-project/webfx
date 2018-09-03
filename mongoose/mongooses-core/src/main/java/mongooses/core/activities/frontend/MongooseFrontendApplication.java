package mongooses.core.activities.frontend;

import mongooses.core.activities.sharedends.MongooseSharedEndsApplication;
import mongooses.core.activities.sharedends.book.fees.FeesRouting;
import webfx.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.activity.Activity;
import webfx.platforms.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendApplication extends MongooseSharedEndsApplication {

    private static final int TESTING_EVENT_ID = 125;
    private static final String DEFAULT_START_PATH = FeesRouting.getFeesPath(TESTING_EVENT_ID);

    private MongooseFrontendApplication() {
        super(DEFAULT_START_PATH);
    }

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return MongooseFrontendContainerActivity::new;
    }

    public static void main(String[] args) {
        launchApplication(new MongooseFrontendApplication(), args);
    }

}
