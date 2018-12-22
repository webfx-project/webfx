package mongoose.backend.application;

import mongoose.client.application.MongooseClientApplication;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplication extends MongooseClientApplication {

    public MongooseBackendApplication() {
        super(new MongooseBackendActivity());
    }
}
