package mongoose.frontend.start;

import mongoose.client.application.MongooseClientApplicationModuleInitializer;

/**
 * @author Bruno Salmon
 */
public final class MongooseFrontendApplicationModuleInitializer extends MongooseClientApplicationModuleInitializer {

    public MongooseFrontendApplicationModuleInitializer() {
        super(new MongooseFrontendApplication());
    }

    @Override
    public String getModuleName() {
        return "mongoose-frontend-application";
    }
}
