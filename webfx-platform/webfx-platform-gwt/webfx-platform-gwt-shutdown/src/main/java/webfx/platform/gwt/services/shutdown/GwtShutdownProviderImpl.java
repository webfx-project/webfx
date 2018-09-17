package webfx.platform.gwt.services.shutdown;

import elemental2.dom.DomGlobal;
import webfx.platforms.core.services.shutdown.spi.ShutdownProvider;

/**
 * @author Bruno Salmon
 */
public class GwtShutdownProviderImpl implements ShutdownProvider {

    @Override
    public void addShutdownHook(Runnable hook) {
        DomGlobal.window.addEventListener("beforeunload", evt -> hook.run());
    }
}
