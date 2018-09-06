package webfx.platforms.core.services.log;

import webfx.platforms.core.services.log.spi.LoggerProvider;
import webfx.platforms.core.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class Logger {

    private static LoggerProvider PROVIDER;

    public static LoggerProvider getProvider() {
        if (PROVIDER == null) {
            registerProvider(ServiceLoaderHelper.loadService(LoggerProvider.class, ServiceLoaderHelper.NotFoundPolicy.RETURN_NULL));
            if (PROVIDER == null)
                registerProvider(new LoggerProvider() {});
        }
        return PROVIDER;
    }

    public static void registerProvider(LoggerProvider provider) {
        PROVIDER = provider;
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
