package mongoose.frontend.application;

import mongoose.client.application.MongooseClientActivity;

/**
 * @author Bruno Salmon
 */
final class MongooseFrontendActivity extends MongooseClientActivity {

    private static final String DEFAULT_START_PATH = "/book/event/357/start";

    MongooseFrontendActivity() {
        super(DEFAULT_START_PATH);
    }

}
