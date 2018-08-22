package naga.providers.platform.client.gwt.services.log;

import naga.platform.services.log.spi.LoggerProvider;

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
