package naga.core;

/*
 * @author Bruno Salmon
 */

import naga.core.bus.call.BusCallService;
import naga.core.spi.platform.Platform;

public class Naga {

    public static final String VERSION_ADDRESS = "version";
    public static final String QUERY_READ_ADDRESS = "query.read";
    public static final String QUERY_WRITE_ADDRESS = "query.write";
    public static final String QUERY_READ_BATCH_ADDRESS = "query.read.batch";
    public static final String QUERY_WRITE_BATCH_ADDRESS = "query.write.batch";

    private static final Naga SINGLETON = new Naga();

    public static Naga naga() {
        return SINGLETON;
    }

    private Naga() {
    }

    public String getVersion() {
        return "Naga prototype version 0.1.0-SNAPSHOT";
    }

    public void startMicroservice() {
        // Registering java services so they can be called through the BusCallService
        BusCallService.registerCallableJavaService(VERSION_ADDRESS, this::getVersion);
        BusCallService.registerAsyncFunctionJavaService(QUERY_READ_ADDRESS, Platform.query()::read);
        BusCallService.registerAsyncFunctionJavaService(QUERY_WRITE_ADDRESS, Platform.query()::write);
        BusCallService.registerAsyncFunctionJavaService(QUERY_READ_BATCH_ADDRESS, Platform.query()::readBatch);
        BusCallService.registerAsyncFunctionJavaService(QUERY_WRITE_BATCH_ADDRESS, Platform.query()::writeBatch);

        // Starting the BusCallService by listening entry calls
        BusCallService.listenEntryCalls();
    }
}
