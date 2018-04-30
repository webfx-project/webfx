package naga.platform.bus.call;

/**
 * @author Bruno Salmon
 */

import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;
import naga.platform.services.query.spi.QueryService;
import naga.platform.services.querypush.spi.QueryPushService;
import naga.platform.services.update.spi.UpdateService;

public class BusCallServerActivity implements Activity {

    public static final String VERSION_ADDRESS = "version";
    public static final String QUERY_SERVICE_ADDRESS = "service/query";
    public static final String QUERY_BATCH_SERVICE_ADDRESS = "service/query/batch";
    public static final String QUERY_PUSH_SERVICE_ADDRESS = "service/querypush";
    public static final String UPDATE_SERVICE_ADDRESS = "service/update";
    public static final String UPDATE_BATCH_SERVICE_ADDRESS = "service/update/batch";

    public String getVersion() {
        return "Naga prototype version 0.1.0-SNAPSHOT";
    }

    @Override
    public void onStart() {
        // Registering java services so they can be called through the BusCallService
        BusCallService.registerJavaCallableAsCallableService(VERSION_ADDRESS, this::getVersion);
        BusCallService.registerJavaAsyncFunctionAsCallableService(QUERY_SERVICE_ADDRESS, QueryService::executeQuery);
        BusCallService.registerJavaAsyncFunctionAsCallableService(QUERY_PUSH_SERVICE_ADDRESS, QueryPushService::executeQueryPush);
        BusCallService.registerJavaAsyncFunctionAsCallableService(UPDATE_SERVICE_ADDRESS, UpdateService::executeUpdate);
        BusCallService.registerJavaAsyncFunctionAsCallableService(QUERY_BATCH_SERVICE_ADDRESS, QueryService::executeQueryBatch);
        BusCallService.registerJavaAsyncFunctionAsCallableService(UPDATE_BATCH_SERVICE_ADDRESS, UpdateService::executeUpdateBatch);

        // Starting the BusCallService by listening entry calls
        BusCallService.listenBusEntryCalls();
    }

    public static void startServerActivity() {
        ActivityManager.startServerActivity(new BusCallServerActivity(), null);
    }

}
