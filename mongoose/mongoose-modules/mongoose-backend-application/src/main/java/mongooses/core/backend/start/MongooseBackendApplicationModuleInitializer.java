package mongooses.core.backend.start;

import mongooses.core.sharedends.start.MongooseSharedEndsApplicationModuleInitializer;

/**
 * @author Bruno Salmon
 */
public final class MongooseBackendApplicationModuleInitializer extends MongooseSharedEndsApplicationModuleInitializer {

    public MongooseBackendApplicationModuleInitializer() {
        super(new MongooseBackendApplication());
    }

    @Override
    public String getModuleName() {
        return "mongooses-backend";
    }
}
