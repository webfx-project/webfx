package webfx.platform.shared.services.log;

import webfx.platform.shared.util.serviceloader.SingleServiceLoader;
import webfx.platform.shared.services.log.spi.LoggerProvider;

/**
 * @author Bruno Salmon
 */
public final class Logger {

    public static LoggerProvider getProvider() {
        return SingleServiceLoader.loadService(LoggerProvider.class);
    }

    public static void log(Object message) {
        getProvider().log(message);
    }

    public static void log(String message) {
        getProvider().log(message);
    }

    public static void log(String message, Throwable error) {
        getProvider().log(message, error);
    }

}
