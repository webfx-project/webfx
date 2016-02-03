package hellonaga;

import naga.core.Naga;
import naga.core.buscall.BusCallService;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class HelloNagaLogic {

    public HelloNagaLogic() {}

    public void runClient() {
        BusCallService.call(Naga.VERSION_ADDRESS, "ignored").setHandler(asyncResult -> Platform.log("" + (asyncResult.succeeded() ? asyncResult.result() : asyncResult.cause())));
    }
}
