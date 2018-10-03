package mongoose.backend.application;

import mongoose.client.application.MongooseClientApplication;
import webfx.framework.client.activity.Activity;
import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.platform.shared.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public final class MongooseBackendApplication extends MongooseClientApplication {

    private static final String DEFAULT_START_PATH = "/organizations";

    public MongooseBackendApplication() {
        super(DEFAULT_START_PATH);
    }

    @Override
    protected Factory<Activity<ViewDomainActivityContextFinal>> getContainerActivityFactory() {
        return MongooseBackendContainerActivity::new;
    }
}
