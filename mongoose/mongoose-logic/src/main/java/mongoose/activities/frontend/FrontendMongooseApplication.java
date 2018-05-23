package mongoose.activities.frontend;

import mongoose.activities.bothends.MongooseApplicationSharedByBothEnds;
import mongoose.activities.bothends.book.fees.FeesRouting;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.platform.activity.Activity;
import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class FrontendMongooseApplication extends MongooseApplicationSharedByBothEnds {

    private static final int TESTING_EVENT_ID = 125;
    private static final String DEFAULT_START_PATH = FeesRouting.getFeesPath(TESTING_EVENT_ID);

    private FrontendMongooseApplication() {
        super(DEFAULT_START_PATH);
    }

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return FrontendContainerActivity::new;
    }

    public static void main(String[] args) {
        launchApplication(new FrontendMongooseApplication(), args);
    }

}
