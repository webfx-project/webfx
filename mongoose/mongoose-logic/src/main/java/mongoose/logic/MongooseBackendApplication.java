package mongoose.logic;

import mongoose.domainmodel.DomainModelSnapshotLoader;
import naga.core.activity.ActivityManager;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendApplication extends MongooseApplication {

    @Override
    public void onStart() {
        activityRouter.setDefaultInitialHistoryPath("/organizations");
        super.onStart();
    }

    public static void main(String[] args) {
        ActivityManager.launchApplication(new MongooseBackendApplication(), args, DomainModelSnapshotLoader.getDataSourceModel());
    }

}
