package naga.vertx.micro;

import io.vertx.core.AbstractVerticle;
import naga.core.Naga;
import naga.core.spi.bus.Bus;
import naga.core.spi.plat.Platform;
import naga.core.spi.plat.VertxPlatform;

/**
 * @author Bruno Salmon
 */
public class NagaMicroservice extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        VertxPlatform.register(vertx);

        Naga naga = new Naga();

        Bus bus = Platform.bus();
        bus.subscribe("version", event -> event.reply(naga.getVersion()));

        Platform.scheduleDelay(1000, ignore -> bus.send("version", "get", event -> Platform.log(event.body())));
    }
}
