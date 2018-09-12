package mongooses.frontend;

import mongooses.core.activities.frontend.MongooseFrontendApplication;
import mongooses.sharedends.MongooseSharedEndsApplicationModule;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendApplicationModule extends MongooseSharedEndsApplicationModule {

    public MongooseFrontendApplicationModule() {
        super(new MongooseFrontendApplication());
    }

}
