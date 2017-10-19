package naga.providers.platform.client.gwt;

import naga.platform.spi.Platform;
import naga.providers.platform.abstr.web.WebPlatform;
import naga.providers.platform.client.gwt.services.resource.GwtBundle;
import naga.providers.platform.client.gwt.services.resource.GwtResourceService;
import naga.providers.platform.client.gwt.url.history.GwtWindowHistory;
import naga.providers.platform.client.gwt.url.location.GwtWindowLocation;
import naga.providers.platform.client.gwt.websocket.GwtWebSocketFactory;

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

    public GwtPlatform() {
        super(/* json factory: */  new GwtWebSocketFactory(), GwtResourceService.SINGLETON, GwtWindowLocation.current(), GwtWindowHistory.SINGLETON);
        setWebLogger(GwtPlatform::logConsole);
    }

    private static native void logConsole(String message) /*-{ $wnd.console.log(message); }-*/;

    public static void registerBundle(GwtBundle bundle) {
        GwtResourceService.SINGLETON.register(bundle);
    }
}
