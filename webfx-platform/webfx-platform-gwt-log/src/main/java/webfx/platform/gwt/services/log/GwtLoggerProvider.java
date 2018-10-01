package webfx.platform.gwt.services.log;

import webfx.platforms.core.services.log.spi.LoggerProvider;

/**
 * @author Bruno Salmon
 */
public class GwtLoggerProvider implements LoggerProvider {

    @Override
    public void log(String message) {
        logConsole(message);
    }

    private static native void logConsole(String message) /*-{ $wnd.console.log(message); }-*/;

}
