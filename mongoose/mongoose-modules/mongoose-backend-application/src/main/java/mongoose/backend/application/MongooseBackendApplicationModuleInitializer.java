package mongoose.backend.application;

import mongoose.client.application.MongooseClientApplicationModuleInitializer;

/**
 * @author Bruno Salmon
 */
public final class MongooseBackendApplicationModuleInitializer extends MongooseClientApplicationModuleInitializer {

    public MongooseBackendApplicationModuleInitializer() {
        super(new MongooseBackendApplication());
    }

    @Override
    public String getModuleName() {
        return "mongoose-backend";
    }
}
