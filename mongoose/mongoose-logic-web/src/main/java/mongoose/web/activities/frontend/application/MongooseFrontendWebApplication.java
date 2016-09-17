package mongoose.web.activities.frontend.application;

import mongoose.activities.frontend.application.MongooseFrontendApplication;
import mongoose.web.activities.shared.application.MongooseWebApplication;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendWebApplication extends MongooseWebApplication {

    @Override
    protected void startMongooseApplicationLogic() {
        MongooseFrontendApplication.main(null);
    }

}
