package mongoose.client.backend;

import mongoose.client.shared.MongooseApplication;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplication extends MongooseApplication {

    @Override
    public void onStart() {
        uiRouter.setDefaultInitialHistoryPath("/monitor");
        super.onStart();
    }

    public static void main(String[] args) {
        launchApplication(new MongooseBackendApplication(), args);
    }

}
