package webfx.providers.platform.client.gwt.services.log;

import webfx.platform.services.log.spi.LoggerProvider;

/**
 * @author Bruno Salmon
 */
public class GwtLoggerProviderImpl implements LoggerProvider {

    @Override
    public void log(String message) {
        logConsole(message);
    }

    private static native void logConsole(String message) /*-{ $wnd.console.log(message); }-*/;

}
