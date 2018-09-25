package mongooses.core.frontend.start;

import mongooses.core.sharedends.start.MongooseSharedEndsApplicationModuleInitializer;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendApplicationModuleInitializer extends MongooseSharedEndsApplicationModuleInitializer {

    public MongooseFrontendApplicationModuleInitializer() {
        super(new MongooseFrontendApplication());
    }

    @Override
    public String getModuleName() {
        return "mongooses-core-frontend-start";
    }
}
