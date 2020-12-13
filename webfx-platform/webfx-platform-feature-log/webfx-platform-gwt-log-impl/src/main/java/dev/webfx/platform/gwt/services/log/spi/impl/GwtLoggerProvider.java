package dev.webfx.platform.gwt.services.log.spi.impl;

import dev.webfx.platform.shared.services.log.spi.LoggerProvider;

/**
 * @author Bruno Salmon
 */
public class GwtLoggerProvider implements LoggerProvider {

    @Override
    public void log(String message, Throwable throwable) {
        if (message != null)
            logConsole(message);
        if (throwable != null)
            logConsole(throwable);
    }

    @Override
    public void logNative(Object nativeObject) {
        logConsole(nativeObject);
    }

    private static native void logConsole(Object message) /*-{ $wnd.console.log(message); }-*/;

}
