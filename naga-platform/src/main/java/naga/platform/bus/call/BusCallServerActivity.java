package naga.platform.bus.call;

/**
 * @author Bruno Salmon
 */

import naga.platform.activity.Activity;
import naga.platform.spi.Platform;

public class BusCallServerActivity implements Activity {

    public static final String VERSION_ADDRESS = "version";
    public static final String QUERY_SERVICE_ADDRESS = "query.read";
    public static final String QUERY_BATCH_SERVICE_ADDRESS = "query.read.batch";
    public static final String UPDATE_SERVICE_ADDRESS = "query.write";
    public static final String UPDATE_BATCH_SERVICE_ADDRESS = "query.write.batch";

    public String getVersion() {
        return "Naga prototype version 0.1.0-SNAPSHOT";
    }

    @Override
    public void onStart() {
        // Registering java services so they can be called through the BusCallService
        BusCallService.registerCallableJavaService(VERSION_ADDRESS, this::getVersion);
        BusCallService.registerAsyncFunctionJavaService(QUERY_SERVICE_ADDRESS, Platform.getQueryService()::executeQuery);
        BusCallService.registerAsyncFunctionJavaService(UPDATE_SERVICE_ADDRESS, Platform.getUpdateService()::executeUpdate);
        BusCallService.registerAsyncFunctionJavaService(QUERY_BATCH_SERVICE_ADDRESS, Platform.getQueryService()::executeQueryBatch);
        BusCallService.registerAsyncFunctionJavaService(UPDATE_BATCH_SERVICE_ADDRESS, Platform.getUpdateService()::executeUpdateBatch);

        // Starting the BusCallService by listening entry calls
        BusCallService.listenEntryCalls();
    }
}
