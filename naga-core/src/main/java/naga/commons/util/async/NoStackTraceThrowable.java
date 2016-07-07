package naga.commons.util.async;

/**
 * @author Bruno Salmon
 */
public class NoStackTraceThrowable extends Throwable {

    public NoStackTraceThrowable(String message) {
        super(message); // J2ME CLDC , null, false, false);
    }
}
