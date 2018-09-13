package mongooses.core.activities.frontend;

import mongooses.core.activities.sharedends.MongooseSharedEndsApplication;
import mongooses.core.activities.sharedends.book.start.StartBookingRouting;
import webfx.framework.activity.Activity;
import webfx.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.platforms.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendApplication extends MongooseSharedEndsApplication {

    private static final int TESTING_EVENT_ID = 357;
    private static final String DEFAULT_START_PATH = StartBookingRouting.getStartBookingPath(TESTING_EVENT_ID);

    public MongooseFrontendApplication() {
        super(DEFAULT_START_PATH);
    }

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return MongooseFrontendContainerActivity::new;
    }

}
