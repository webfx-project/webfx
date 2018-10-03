package mongoose.frontend.start;

import mongoose.client.application.MongooseClientApplication;
import webfx.framework.client.activity.Activity;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.platform.shared.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public final class MongooseFrontendApplication extends MongooseClientApplication {

    private static final String DEFAULT_START_PATH = "/book/event/357/application";

    public MongooseFrontendApplication() {
        super(DEFAULT_START_PATH);
    }

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return MongooseFrontendContainerActivity::new;
    }

}
