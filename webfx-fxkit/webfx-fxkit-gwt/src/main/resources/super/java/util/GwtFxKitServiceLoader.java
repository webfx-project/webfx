/**
 * @author Bruno Salmon
 */

package java.util;

import webfx.fxkit.gwt.GwtFxKitProvider;
import webfx.fxkits.core.spi.FxKitProvider;

class GwtFxKitServiceLoader extends GwtPlatformServiceLoader {

    static  {
        registerService(FxKitProvider.class, GwtFxKitProvider::new);
    }

}