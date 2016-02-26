package naga.core.spi.platform.client.gwt;

import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.gwt.GwtJsonFactory;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.platform.client.WebSocketFactory;
import naga.core.spi.platform.client.web.WebLocation;
import naga.core.spi.platform.client.web.WebPlatform;

/**
 * @author Bruno Salmon
 */
public final class GwtPlatform extends WebPlatform {

    /**
     * Providing GwtPlatform.register() method if needed but this explicit call is normally not necessary
     * as this platform is recognized by the customized ServiceLoader provided in the super source located at
     * resources/super/java/util/ServiceLoader.java. But this will work only if the gwt.xml module includes
     * <super-source path="super"/>
     */
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

    @Override
    public WebLocation getCurrentLocation() {
        return GwtLocation.current();
    }
}
