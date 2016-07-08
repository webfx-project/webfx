package mongoose.application.backend.android;

import android.app.Activity;
import android.os.Bundle;
import mongoose.application.backend.MongooseBackendApplication;
import naga.toolkit.providers.android.AndroidToolkit;
import naga.platform.providers.android.AndroidPlatform;

public class MongooseBackendAndroidActivity extends Activity {

    static {
        AndroidPlatform.register(); // using explicit registration as the ServiceLoader has an issue on Android (META-INF is excluded from the apk)
        AndroidToolkit.register();
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidToolkit.currentActivity = this;
        MongooseBackendApplication.main(null);
    }

}
