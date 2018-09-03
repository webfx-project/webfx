package webfx.platforms.core.bus.call;

/**
 * @author Bruno Salmon
 */

import webfx.platforms.core.services.query.QueryService;
import webfx.platforms.core.services.query.push.QueryPushService;
import webfx.platforms.core.services.update.UpdateService;
import webfx.platforms.core.spi.server.ServerModule;
import webfx.platforms.core.spi.server.ServerPlatform;
import webfx.platforms.core.util.async.Future;

public class BusCallServerModule implements ServerModule {

    public static final String VERSION_ADDRESS = "version";
    public static final String QUERY_SERVICE_ADDRESS = "service/query";
    public static final String QUERY_BATCH_SERVICE_ADDRESS = "service/query/batch";
    public static final String QUERY_PUSH_SERVICE_ADDRESS = "service/querypush";
    public static final String UPDATE_SERVICE_ADDRESS = "service/update";
    public static final String UPDATE_BATCH_SERVICE_ADDRESS = "service/update/batch";

    public String getVersion() {
        return "Webfx prototype version 0.1.0-SNAPSHOT";
    }

    @Override
    public Future<Void> onStart() {
        // Registering java services so they can be called through the BusCallService
        BusCallService.registerJavaCallableAsCallableService(VERSION_ADDRESS, this::getVersion);
        BusCallService.registerJavaAsyncFunctionAsCallableService(QUERY_SERVICE_ADDRESS, QueryService::executeQuery);
        BusCallService.registerJavaAsyncFunctionAsCallableService(QUERY_PUSH_SERVICE_ADDRESS, QueryPushService::executeQueryPush);
        BusCallService.registerJavaAsyncFunctionAsCallableService(UPDATE_SERVICE_ADDRESS, UpdateService::executeUpdate);
        BusCallService.registerJavaAsyncFunctionAsCallableService(QUERY_BATCH_SERVICE_ADDRESS, QueryService::executeQueryBatch);
        BusCallService.registerJavaAsyncFunctionAsCallableService(UPDATE_BATCH_SERVICE_ADDRESS, UpdateService::executeUpdateBatch);

        // Starting the BusCallService by listening entry calls
        BusCallService.listenBusEntryCalls();
        return Future.succeededFuture();
    }

    // Static helper method to start this module on the server
    public static void start() {
        ServerPlatform.get().startServerModule(new BusCallServerModule());
    }

}
