package naga.providers.platform.server.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import naga.providers.platform.server.vertx.util.VertxRunner;

/**
 * @author Bruno Salmon
 */
public class WebVerticle extends AbstractVerticle {

    // Convenient method to run the microservice directly in the IDE
    public static void main(String[] args) {
        VertxRunner.runVerticle(WebVerticle.class);
    }

    @Override
    public void start() throws Exception {
        createHttpServer(9090, null); // for http
        createHttpServer(9191, new PemKeyCertOptions().setCertPath("fullchain.pem").setKeyPath("privkey.pem")); // Let's encrypt certificate for https
    }

    private void createHttpServer(int port, PemKeyCertOptions pemKeyCertOptions) {
        // Creating web server and its router
        HttpServerOptions httpServerOptions = new HttpServerOptions()
                .setMaxWebsocketFrameSize(65536 * 100) // Increasing the frame size to allow big client request
                .setCompressionSupported(true) // enabling gzip and deflate compression
                .setPort(port) // web port
                .setSsl(pemKeyCertOptions != null)
                .setPemKeyCertOptions(pemKeyCertOptions)
                ;
        HttpServer server = vertx.createHttpServer(httpServerOptions);
        Router router = Router.router(vertx);

        // Logging web requests
        router.route("/*").handler(LoggerHandler.create());

        // SockJS event bus bridge
        router.route("/eventbus/*").handler(SockJSHandler.create(vertx)
                .bridge(new BridgeOptions()
                        .addInboundPermitted(new PermittedOptions(new JsonObject()))
                        .addOutboundPermitted(new PermittedOptions(new JsonObject()))
                        // Uncomment to watch events on the bridge , event -> System.out.println(event.type())
                )
        );

        // GWT perfect caching
        router.routeWithRegex(".*\\.cache\\..*").handler(routingContext -> {
            routingContext.response().putHeader("cache-control", "public, max-age=31556926");
            routingContext.next();
        });

        // Serving static files under the webroot folder
        router.route("/*").handler(StaticHandler.create());

        // Binding the web port
        server.requestHandler(router::accept).listen();
    }
}
