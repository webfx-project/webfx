package webfx.platform.gwt;

import webfx.platform.gwt.services.resource.GwtBundle;
import webfx.platform.gwt.services.resource.GwtResourceServiceProviderImpl;
import webfx.platform.gwt.url.history.GwtWindowHistory;
import webfx.platform.gwt.url.location.GwtWindowLocation;
import webfx.platforms.core.services.resource.ResourceService;
import webfx.platforms.core.spi.Platform;
import webfx.platforms.web.WebPlatform;

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
        super(/* json factory: */   GwtWindowLocation.current(), GwtWindowHistory.SINGLETON);
    }

    public static void registerBundle(GwtBundle bundle) {
        ((GwtResourceServiceProviderImpl) ResourceService.getProvider()).register(bundle);
    }
}
