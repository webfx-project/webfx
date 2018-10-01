package webfx.platform.shared.services.log.spi.gwt;

import webfx.platform.shared.services.log.spi.LoggerProvider;

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
