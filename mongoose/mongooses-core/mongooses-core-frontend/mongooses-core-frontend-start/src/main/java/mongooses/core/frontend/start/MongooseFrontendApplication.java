package mongooses.core.frontend.start;

import mongooses.core.sharedends.start.MongooseSharedEndsApplication;
import webfx.framework.activity.Activity;
import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.platforms.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendApplication extends MongooseSharedEndsApplication {

    private static final String DEFAULT_START_PATH = "/book/event/357/start";

    public MongooseFrontendApplication() {
        super(DEFAULT_START_PATH);
    }

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return MongooseFrontendContainerActivity::new;
    }

}
