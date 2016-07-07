package naga.platform.providers.gwt;

import naga.platform.spi.Platform;
import naga.platform.providers.abstr.web.WebPlatform;

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
        super(new GwtScheduler(), /* json factory: */ GwtJsonObject.create(), new GwtWebSocketFactory(), GwtResourceService.SINGLETON, GwtWindowLocation.current(), GwtWindowHistory.SINGLETON);
        setWebLogger(GwtPlatform::logConsole);
    }

    private static native void logConsole(String message) /*-{ console.log(message); }-*/;

    public static void registerBundle(GwtBundle bundle) {
        GwtResourceService.SINGLETON.register(bundle);
    }
}
