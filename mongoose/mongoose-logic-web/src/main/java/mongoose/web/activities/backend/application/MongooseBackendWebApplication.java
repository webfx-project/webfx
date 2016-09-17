package mongoose.web.activities.backend.application;

import mongoose.activities.backend.application.MongooseBackendApplication;
import mongoose.web.activities.shared.application.MongooseWebApplication;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendWebApplication extends MongooseWebApplication {

    @Override
    protected void startMongooseApplicationLogic() {
        MongooseBackendApplication.main(null);
    }

}
