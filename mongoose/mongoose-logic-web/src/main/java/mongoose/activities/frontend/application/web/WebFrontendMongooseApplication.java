package mongoose.activities.frontend.application.web;

import mongoose.activities.frontend.application.FrontendMongooseApplication;
import mongoose.activities.bothends.application.web.WebMongooseApplication;

/**
 * @author Bruno Salmon
 */
public class WebFrontendMongooseApplication extends WebMongooseApplication {

    @Override
    protected void startMongooseApplicationLogic() {
        FrontendMongooseApplication.main(null);
    }

}
