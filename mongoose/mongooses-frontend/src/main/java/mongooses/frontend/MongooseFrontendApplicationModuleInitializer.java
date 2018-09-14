package mongooses.frontend;

import mongooses.core.activities.frontend.MongooseFrontendApplication;
import mongooses.sharedends.MongooseSharedEndsApplicationModuleInitializer;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendApplicationModuleInitializer extends MongooseSharedEndsApplicationModuleInitializer {

    public MongooseFrontendApplicationModuleInitializer() {
        super(new MongooseFrontendApplication());
    }

    @Override
    public String getModuleName() {
        return "mongooses-frontend";
    }
}
