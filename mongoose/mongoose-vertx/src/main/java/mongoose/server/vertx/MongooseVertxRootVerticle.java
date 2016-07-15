package mongoose.server.vertx;

import mongoose.server.MongooseMetricsServerActivity;
import naga.providers.platform.server.vertx.util.VertxRunner;
import naga.providers.platform.server.vertx.verticles.RootVerticle;

/**
 * @author Bruno Salmon
 */
public class MongooseVertxRootVerticle extends RootVerticle {

    public static void main(String[] args) {
        VertxRunner.runVerticle(MongooseVertxRootVerticle.class);
    }

    @Override
    public void start() throws Exception {
        super.start();
        MongooseMetricsServerActivity.startActivity();
    }

}
