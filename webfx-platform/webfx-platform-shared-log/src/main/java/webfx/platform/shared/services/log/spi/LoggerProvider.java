package webfx.platform.shared.services.log.spi;

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
        System.out.println(message);
    }

    default void log(String message, Throwable error) {
        log(message + "\n    " + error.getClass().getName()+ ": " + error.getMessage() + "\n");
    }

}
