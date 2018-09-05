/**
 * @author Bruno Salmon
 */

package java.util;

import webfx.fxkit.gwt.GwtFxKit;
import webfx.fxkits.core.spi.FxKit;

class GwtFxKitServiceLoader extends GwtPlatformServiceLoader {

    static  {
        registerService(FxKit.class, GwtFxKit::new);
    }

}