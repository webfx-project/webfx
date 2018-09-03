package mongooses.web.activities.backend;


import mongooses.core.activities.backend.MongooseBackendApplication;
import mongooses.web.activities.sharedends.MongooseSharedEndsWebApplication;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendWebApplication extends MongooseSharedEndsWebApplication {

    @Override
    protected void startMongooseApplicationLogic() {
        MongooseBackendApplication.main(null);
    }

}
