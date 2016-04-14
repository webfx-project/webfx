package mongoose.backend.swing;

import mongoose.logic.MongooseLogic;
import naga.core.spi.bus.client.WebSocketBusOptions;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendSwingApplication {

    public static void main(String[] args) {
        Platform.createBus(new WebSocketBusOptions().setServerHost("kadampabookings.org").setServerPort(9090));
        MongooseLogic.runBackendApplication();
    }
}
