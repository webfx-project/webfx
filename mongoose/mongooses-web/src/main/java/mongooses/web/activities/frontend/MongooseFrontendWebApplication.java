package mongooses.web.activities.frontend;


import mongooses.core.activities.frontend.MongooseFrontendApplication;
import mongooses.web.activities.sharedends.MongooseSharedEndsWebApplication;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendWebApplication extends MongooseSharedEndsWebApplication {

    @Override
    protected void startMongooseApplicationLogic() {
        MongooseFrontendApplication.main(null);
    }

}
