package mongooses.core.backend.start;

import mongooses.core.sharedends.start.MongooseSharedEndsApplication;
import webfx.framework.activity.Activity;
import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.platforms.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplication extends MongooseSharedEndsApplication {

    private static final String DEFAULT_START_PATH = "/organizations";

    public MongooseBackendApplication() {
        super(DEFAULT_START_PATH);
    }

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return MongooseBackendContainerActivity::new;
    }
}
