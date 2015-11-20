package naga.vertx.micro;

import io.vertx.core.AbstractVerticle;
import naga.core.Naga;

/**
 * @author Bruno Salmon
 */
public class NagaMicroservice extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        System.out.println("Starting " + new Naga().getVersion());
    }
}
