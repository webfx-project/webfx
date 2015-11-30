package naga.core.spi.plat.gwt;

import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.gwt.GwtJsonFactory;
import naga.core.spi.plat.Platform;
import naga.core.spi.plat.client.ClientPlatform;
import naga.core.spi.sched.Scheduler;
import naga.core.spi.sched.gwt.GwtScheduler;
import naga.core.spi.sock.WebSocketFactory;
import naga.core.spi.sock.gwt.GwtWebSocketFactory;

/**
 * @author Bruno Salmon
 */
public final class GwtPlatform implements ClientPlatform {

    public static void register() {
        Platform.register(new GwtPlatform());
    }

    private final Scheduler scheduler = new GwtScheduler();
    private final JsonFactory jsonFactory = new GwtJsonFactory();
    private final WebSocketFactory webSocketFactory = new GwtWebSocketFactory();

    @Override
    public Scheduler scheduler() {
        return scheduler;
    }

    @Override
    public JsonFactory jsonFactory() {
        return jsonFactory;
    }

    @Override
    public WebSocketFactory webSocketFactory() {
        return webSocketFactory;
    }

}
