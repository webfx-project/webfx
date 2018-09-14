package mongooses.backend;

import mongooses.core.activities.backend.MongooseBackendApplication;
import mongooses.sharedends.MongooseSharedEndsApplicationModuleInitializer;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplicationModuleInitializer extends MongooseSharedEndsApplicationModuleInitializer {

    public MongooseBackendApplicationModuleInitializer() {
        super(new MongooseBackendApplication());
    }

    @Override
    public String getModuleName() {
        return "mongooses-backend";
    }
}
