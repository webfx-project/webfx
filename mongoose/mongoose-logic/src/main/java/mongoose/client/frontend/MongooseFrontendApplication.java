package mongoose.client.frontend;

import mongoose.client.shared.MongooseApplication;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendApplication extends MongooseApplication {

    @Override
    public void onStart() {
        uiRouter.setDefaultInitialHistoryPath("/cart/a58faba5-5b0b-4573-b547-361e10c788dc");
        super.onStart();
    }

    public static void main(String[] args) {
        launchApplication(new MongooseFrontendApplication(), args);
    }


}
