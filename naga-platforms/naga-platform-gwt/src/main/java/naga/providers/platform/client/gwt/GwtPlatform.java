package naga.providers.platform.client.gwt;

import naga.platform.services.resource.ResourceService;
import naga.platform.spi.Platform;
import naga.providers.platform.abstr.web.WebPlatform;
import naga.providers.platform.client.gwt.services.resource.GwtBundle;
import naga.providers.platform.client.gwt.services.resource.GwtResourceServiceProviderImpl;
import naga.providers.platform.client.gwt.url.history.GwtWindowHistory;
import naga.providers.platform.client.gwt.url.location.GwtWindowLocation;

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
