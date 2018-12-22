package mongoose.frontend.application;

import mongoose.client.application.MongooseClientApplication;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendApplication extends MongooseClientApplication {

    public MongooseFrontendApplication() {
        super(new MongooseFrontendActivity());
    }
}
