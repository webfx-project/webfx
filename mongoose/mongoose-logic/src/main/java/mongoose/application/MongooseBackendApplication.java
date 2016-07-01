package mongoose.application;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplication extends MongooseApplication {

    @Override
    public void onStart() {
        activityRouter.setDefaultInitialHistoryPath("/monitor");
        super.onStart();
    }

    public static void main(String[] args) {
        launchApplication(new MongooseBackendApplication(), args);
    }

}
