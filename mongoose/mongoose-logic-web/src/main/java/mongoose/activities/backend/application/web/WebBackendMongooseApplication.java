package mongoose.activities.backend.application.web;

import mongoose.activities.backend.application.BackendMongooseApplication;
import mongoose.activities.bothends.application.web.WebMongooseApplication;

/**
 * @author Bruno Salmon
 */
public class WebBackendMongooseApplication extends WebMongooseApplication {

    @Override
    protected void startMongooseApplicationLogic() {
        BackendMongooseApplication.main(null);
    }

}
