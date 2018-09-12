package mongooses.backend;

import mongooses.core.activities.backend.MongooseBackendApplication;
import mongooses.sharedends.MongooseSharedEndsApplicationModule;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplicationModule extends MongooseSharedEndsApplicationModule {

    public MongooseBackendApplicationModule() {
        super(new MongooseBackendApplication());
    }

}
