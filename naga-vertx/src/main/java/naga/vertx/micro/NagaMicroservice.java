package naga.vertx.micro;

import io.vertx.core.AbstractVerticle;
import naga.core.Naga;
import naga.core.spi.bus.Bus;
import naga.core.spi.plat.Platform;
import naga.core.spi.plat.vertx.VertxPlatform;

/**
 * @author Bruno Salmon
 */
public class NagaMicroservice extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        // We can't use the automatic platform registration mechanism provided by the ServiceLoader here
        // because we need to pass the vertx instance (there is no default constructor for VertxPlatform)
        VertxPlatform.register(vertx); // So we use the explicit registration mechanism instead

        Naga naga = new Naga();

        Bus bus = Platform.bus();
        bus.subscribe("version", event -> event.reply(naga.getVersion()));

        Platform.scheduleDelay(1000, ignore -> bus.send("version", "get", event -> Platform.log(event.body())));
    }
}
