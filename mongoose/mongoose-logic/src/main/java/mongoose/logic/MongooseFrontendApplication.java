package mongoose.logic;

import naga.core.activity.ActivityManager;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendApplication extends MongooseApplication {

    @Override
    public void onStart() {
        router.defaultPath("/cart/a58faba5-5b0b-4573-b547-361e10c788dc");
        super.onStart();
    }

    public static void main(String[] args) {
        ActivityManager.launchApplication(new MongooseFrontendApplication(), args);
    }


}
