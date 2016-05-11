package naga.core;

/*
 * @author Bruno Salmon
 */

import naga.core.json.buscall.BusCallService;
import naga.core.spi.platform.Platform;

public class Naga {

    public static final String VERSION_ADDRESS = "version";
    public static final String SQL_READ_ADDRESS = "sql.read";
    public static final String SQL_WRITE_ADDRESS = "sql.write";
    public static final String SQL_READ_BATCH_ADDRESS = "sql.read.batch";
    public static final String SQL_WRITE_BATCH_ADDRESS = "sql.write.batch";

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
        BusCallService.registerAsyncFunctionJavaService(SQL_READ_ADDRESS, Platform.sql()::read);
        BusCallService.registerAsyncFunctionJavaService(SQL_WRITE_ADDRESS, Platform.sql()::write);
        BusCallService.registerAsyncFunctionJavaService(SQL_READ_BATCH_ADDRESS, Platform.sql()::readBatch);
        BusCallService.registerAsyncFunctionJavaService(SQL_WRITE_BATCH_ADDRESS, Platform.sql()::writeBatch);

        // Starting the BusCallService by listening entry calls
        BusCallService.listenEntryCalls();
    }
}
