package dev.webfx.platform.shared.services.log.spi;

/**
 * @author Bruno Salmon
 */
public interface LoggerProvider {

    default void log(Object message) {
        if (message instanceof Throwable)
            log(null, (Throwable) message);
        else
            log(message == null ? "null" : message.toString());
    }

    default void log(String message) {
        log(message, null);
    }

    default void log(String message, Throwable error) {
        if (message != null)
            System.out.println(message);
        if (error != null)
            error.printStackTrace(System.err);
    }

    default void logNative(Object nativeObject) {
        System.out.println(nativeObject);
    }

}
