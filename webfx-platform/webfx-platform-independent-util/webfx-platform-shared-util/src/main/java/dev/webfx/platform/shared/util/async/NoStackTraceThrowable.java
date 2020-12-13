package dev.webfx.platform.shared.util.async;

/**
 * @author Bruno Salmon
 */
public final class NoStackTraceThrowable extends Throwable {

    public NoStackTraceThrowable(String message) {
        super(message); // J2ME CLDC , null, false, false);
    }
}
