package hellonaga.logic;

import naga.core.Naga;
import naga.core.composite.buscall.BusCallService;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class HelloNagaLogic {

    public static void runClient() {
        BusCallService.call(Naga.VERSION_ADDRESS, "ignored").setHandler(asyncResult -> Platform.log(asyncResult.succeeded() ? asyncResult.result() : "Error: " + asyncResult.cause()));
    }
}
