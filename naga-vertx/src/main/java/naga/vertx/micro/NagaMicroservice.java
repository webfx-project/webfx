package naga.vertx.micro;

import io.vertx.core.AbstractVerticle;
import naga.core.Naga;
import naga.core.spi.platform.vertx.VertxPlatform;
import naga.core.spi.sql.impl.ConnectionDetails;
import naga.core.spi.sql.impl.DBMS;
import naga.core.spi.sql.impl.LocalDataSourceRegistry;

/**
 * @author Bruno Salmon
 */
public class NagaMicroservice extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        // We can't use the automatic platform registration mechanism provided by the ServiceLoader here
        // because we need to pass the vertx instance (there is no default constructor for VertxPlatform)
        VertxPlatform.register(vertx); // So we use the explicit registration mechanism instead

        Naga.naga().registerMicroserviceHandlers();
    }
}
