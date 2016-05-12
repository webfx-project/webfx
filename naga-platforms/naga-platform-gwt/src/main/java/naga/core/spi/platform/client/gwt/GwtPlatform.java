package naga.core.spi.platform.client.gwt;

import naga.core.json.JsonFactory;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.platform.client.ResourceService;
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
    private final GwtJsonObject jsonFactory = GwtJsonObject.create();
    private final WebSocketFactory webSocketFactory = new GwtWebSocketFactory();

    public GwtPlatform() {
        setWebLogger(GwtPlatform::logConsole);
    }

    private static native void logConsole(String message) /*-{ console.log(message); }-*/;

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
    public ResourceService resourceService() {
        return GwtResourceService.SINGLETON;
    }

    @Override
    public WebLocation getCurrentLocation() {
        return GwtLocation.current();
    }

    public static void registerBundle(GwtBundle bundle) {
        GwtResourceService.SINGLETON.register(bundle);
    }
}
