package dev.webfx.platform.gwt.services.shutdown.spi.impl;

import elemental2.dom.DomGlobal;
import elemental2.dom.EventListener;
import dev.webfx.platform.shared.services.shutdown.spi.impl.ShutdownProviderBase;

/**
 * @author Bruno Salmon
 */
public final class GwtShutdownProvider extends ShutdownProviderBase<EventListener> {

    @Override
    protected EventListener createPlatformShutdownHook(Runnable hook) {
        return evt -> hook.run();
    }

    @Override
    protected void addPlatformShutdownHook(EventListener platformHook) {
        DomGlobal.window.addEventListener("beforeunload", platformHook);
    }

    @Override
    protected void removePlatformShutdownHook(EventListener platformHook) {
        DomGlobal.window.removeEventListener("beforeunload", platformHook);
    }

    @Override
    protected void exit(int exitStatus) {
        exit();
    }

    public native void exit() /*-{
        $wnd.close();
    }-*/;
}
