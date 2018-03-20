package naga.platform.bus.call;

/**
 * @author Bruno Salmon
 */

import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;
import naga.platform.services.query.spi.QueryService;
import naga.platform.services.update.spi.UpdateService;

public class BusCallServerActivity implements Activity {

    public static final String VERSION_ADDRESS = "version";
    public static final String QUERY_SERVICE_ADDRESS = "service/query";
    public static final String QUERY_BATCH_SERVICE_ADDRESS = "service/query/batch";
    public static final String UPDATE_SERVICE_ADDRESS = "service/update";
    public static final String UPDATE_BATCH_SERVICE_ADDRESS = "service/update/batch";

    public String getVersion() {
        return "Naga prototype version 0.1.0-SNAPSHOT";
    }

    @Override
    public void onStart() {
        // Registering java services so they can be called through the BusCallService
        BusCallService.registerCallableJavaService(VERSION_ADDRESS, this::getVersion);
        BusCallService.registerAsyncFunctionJavaService(QUERY_SERVICE_ADDRESS, QueryService::executeQuery);
        BusCallService.registerAsyncFunctionJavaService(UPDATE_SERVICE_ADDRESS, UpdateService::executeUpdate);
        BusCallService.registerAsyncFunctionJavaService(QUERY_BATCH_SERVICE_ADDRESS, QueryService::executeQueryBatch);
        BusCallService.registerAsyncFunctionJavaService(UPDATE_BATCH_SERVICE_ADDRESS, UpdateService::executeUpdateBatch);

        // Starting the BusCallService by listening entry calls
        BusCallService.listenEntryCalls();
    }

    public static void startServerActivity() {
        ActivityManager.startServerActivity(new BusCallServerActivity(), null);
    }

}
