package naga.core.activity;

/**
 * @author Bruno Salmon
 */

import naga.core.bus.call.BusCallService;
import naga.core.spi.platform.Platform;

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
        BusCallService.registerAsyncFunctionJavaService(QUERY_SERVICE_ADDRESS, Platform.query()::executeQuery);
        BusCallService.registerAsyncFunctionJavaService(UPDATE_SERVICE_ADDRESS, Platform.update()::executeUpdate);
        BusCallService.registerAsyncFunctionJavaService(QUERY_BATCH_SERVICE_ADDRESS, Platform.query()::executeQueryBatch);
        BusCallService.registerAsyncFunctionJavaService(UPDATE_BATCH_SERVICE_ADDRESS, Platform.update()::executeUpdateBatch);

        // Starting the BusCallService by listening entry calls
        BusCallService.listenEntryCalls();
    }
}
