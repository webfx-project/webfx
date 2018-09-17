/**
 * @author Bruno Salmon
 */

package java.util;

import webfx.fxkit.gwt.GwtFxKitProvider;
import webfx.fxkits.core.FxKitModuleInitializer;
import webfx.fxkits.core.spi.FxKitProvider;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;

class GwtFxKitServiceLoader extends GwtPlatformServiceLoader {

    static  {
        registerService(FxKitProvider.class, GwtFxKitProvider::new);
        registerService(ApplicationModuleInitializer.class, FxKitModuleInitializer::new);
    }

}