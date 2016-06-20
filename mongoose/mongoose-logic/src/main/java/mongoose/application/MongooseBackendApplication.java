package mongoose.application;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplication extends MongooseApplication {

    @Override
    public void onStart() {
        activityRouter.setDefaultInitialHistoryPath("/event/115/bookings");
        super.onStart();
    }

    public static void main(String[] args) {
        launchApplication(new MongooseBackendApplication(), args);
    }

}
