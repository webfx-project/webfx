package naga.vertx.micro;

import io.vertx.core.AbstractVerticle;
import naga.core.Naga;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.vertx.VertxPlatform;

/**
 * @author Bruno Salmon
 */
public class NagaMicroservice extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        // We can't use the automatic platform registration mechanism provided by the ServiceLoader here
        // because we need to pass the vertx instance (there is no default constructor for VertxPlatform)
        VertxPlatform.register(vertx); // So we use the explicit registration mechanism instead

        Naga naga = new Naga(); // Getting an instance of Naga

        Platform.bus().subscribe("version", event -> event.reply(naga.getVersion()));
    }
}
