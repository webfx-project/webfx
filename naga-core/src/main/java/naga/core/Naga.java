package naga.core;

/*
 * @author Bruno Salmon
 */

import naga.core.buscall.BusCallService;
import naga.core.spi.platform.Platform;

public class Naga {

    public static final String VERSION_ADDRESS = "version";
    public static final String SQL_READ_ADDRESS = "sql.read";
    public static final String SQL_WRITE_ADDRESS = "sql.write";

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
        BusCallService.registerJavaService(VERSION_ADDRESS, this::getVersion);
        BusCallService.registerJavaService(SQL_READ_ADDRESS, Platform.sql()::read);
        BusCallService.registerJavaService(SQL_WRITE_ADDRESS, Platform.sql()::write);

        // Starting the BusCallService by listening entry calls
        BusCallService.listenEntryCalls();
    }
}
