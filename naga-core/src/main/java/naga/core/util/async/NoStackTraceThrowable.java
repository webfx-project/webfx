package naga.core.util.async;

/**
 * @author Bruno Salmon
 */
public class NoStackTraceThrowable extends Throwable {

    public NoStackTraceThrowable(String message) {
        super(message, null, false, false);
    }
}
